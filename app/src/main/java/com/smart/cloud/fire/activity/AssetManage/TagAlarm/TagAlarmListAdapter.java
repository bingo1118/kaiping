package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.activity.AssetManage.Tag.TagListAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TagAlarmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<TagAlarmInfo> itemsList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
        notifyDataSetChanged();
        mList=new ArrayList<>();
    }

    private boolean showCheck=false;

    public List<String> getmList() {
        return mList;
    }

    private List<String> mList;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, TagAlarmInfo data);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public TagAlarmListAdapter(Context mContext, List<TagAlarmInfo> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tag_alarm_list_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TagAlarmInfo mPoint = itemsList.get(position);

        ((ItemViewHolder) holder).asset_name_tv.setText(mPoint.getNamed());
        ((ItemViewHolder) holder).mac_tv.setText("ID:"+mPoint.getAkey());
        ((ItemViewHolder) holder).alarmtype_name_tv.setText(mPoint.getAlarmName());
        ((ItemViewHolder) holder).tag_name_tv.setText("地址:"+mPoint.getTagName());
        ((ItemViewHolder) holder).address_tv.setText("告警时间:"+mPoint.getAlarmTime());
        ((ItemViewHolder) holder).checkuser_tv.setText("负责人:"+mPoint.getPrincapal());
        ((ItemViewHolder) holder).phone_tv.setText("联系方式:"+mPoint.getPhone());
        ((ItemViewHolder) holder).dealuser_tv.setText("处理人:"+mPoint.getDealUser());
        ((ItemViewHolder) holder).dealtime_tv.setText("处理时间:"+mPoint.getDealTime());
        ((ItemViewHolder) holder).dealmemo_tv.setText("处理情况:"+mPoint.getDealDesc());

        final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -0.1f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        ((ItemViewHolder) holder).moreinfo_line.setVisibility(View.GONE);
        ((ItemViewHolder) holder).more_tv.setText("展开");
        ((ItemViewHolder) holder).more_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= (String) ((ItemViewHolder) holder).more_tv.getText();
                if(s.equals("展开")){
                    ((ItemViewHolder) holder).moreinfo_line.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).moreinfo_line.startAnimation(mShowAction);
                    ((ItemViewHolder) holder).more_tv.setText("收起");
                }else{
                    ((ItemViewHolder) holder).moreinfo_line.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).more_tv.setText("展开");
                }
            }
        });

        if(mPoint.getIfDeal()==1){
            ((ItemViewHolder) holder).alarmstate_im.setImageResource(R.drawable.asset_alarm_green);
            ((ItemViewHolder) holder).rela.setOnClickListener(null);
        }else{
            ((ItemViewHolder) holder).alarmstate_im.setImageResource(R.drawable.asset_alarm_list_triangle);
            ((ItemViewHolder) holder).rela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputDialog(mPoint);
                }
            });
        }
        holder.itemView.setTag(mPoint);
    }

    private void showInputDialog(TagAlarmInfo mPoint) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.deal_tag_alarm_dialog,null);
        EditText editText = view.findViewById(R.id.memo_edit);

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(mContext);
        inputDialog.setView(view);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VolleyHelper helper = VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        String url = ConstantValues.SERVER_IP_NEW + "dealTagAlarm?akey="+mPoint.getAkey()
                                    + "&dealUser="+ MyApp.getUserID()
                                    + "&dealDesc=" + URLEncoder.encode(editText.getText().toString());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode = response.getInt("errorcode");
                                            String error = response.getString("error");
                                            if (errorCode == 0) {
                                                T.showShort(mContext, "处理成功");
                                                mPoint.setIfDeal(1);
                                                mPoint.setDealUser(MyApp.getUserID());
                                                mPoint.setDealDesc(editText.getText().toString());
                                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                                mPoint.setDealTime(df.format(new Date()));
                                                notifyDataSetChanged();
                                            } else {
                                                T.showShort(mContext, error);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                T.showShort(mContext, "网络错误");
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (TagAlarmInfo) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.asset_name_tv)
        TextView asset_name_tv;
        @Bind(R.id.alarmtype_name_tv)
        TextView alarmtype_name_tv;
        @Bind(R.id.mac_tv)
        TextView mac_tv;
        @Bind(R.id.tag_name_tv)
        TextView tag_name_tv;
        @Bind(R.id.address_tv)
        TextView address_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.checkuser_tv)
        TextView checkuser_tv;
        @Bind(R.id.phone_tv)
        TextView phone_tv;
        @Bind(R.id.dealuser_tv)
        TextView dealuser_tv;
        @Bind(R.id.dealtime_tv)
        TextView dealtime_tv;
        @Bind(R.id.dealmemo_tv)
        TextView dealmemo_tv;
        @Bind(R.id.more_tv)
        TextView more_tv;
        @Bind(R.id.alarmstate_im)
        ImageView alarmstate_im;

        @Bind(R.id.rela)
        RelativeLayout rela;
        @Bind(R.id.moreinfo_line)
        LinearLayout moreinfo_line;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


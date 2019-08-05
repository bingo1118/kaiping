package com.smart.cloud.fire.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.functions.Func1;

public class Bingo2DropDowmListView extends LinearLayout {


    @Bind(R.id.loading_prg_monitor)
    ProgressBar loading_prg_monitor;
    @Bind(R.id.clear_choice)
    ImageView clear_choice;
    @Bind(R.id.spinner)
    Spinner spinner;

    private List<BingoViewModel> dataList =  new ArrayList<>();
    private Context mContext;
    String selecedName;
    String selecedId;


    public String getSelecedName() {
        return selecedName;
    }

    public String getSelecedId() {
        return selecedId;
    }

    private BingoDropDowmListView.OnSelectedItem mOnSelectedItem;

    public void setmOnSelectedItem(BingoDropDowmListView.OnSelectedItem mOnSelectedItem) {
        this.mOnSelectedItem = mOnSelectedItem;
    }

    public interface OnSelectedItem{
        public void onSelectedItem(String selectrdItemId);
    }


    public Bingo2DropDowmListView(Context context) {
        this(context,null);
        initView(context,null);
    }
    public Bingo2DropDowmListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context,attrs);
    }
    public Bingo2DropDowmListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context,attrs);
    }

    public void initView(Context context, AttributeSet attrs){
        mContext = context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.BingoDropListView);
        String hintText=typedArray.getString(R.styleable.BingoDropListView_spinner_hint);
        typedArray.recycle();

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view  = layoutInflater.inflate(R.layout.bingo_drop_listview, this,true);
        ButterKnife.bind(this,view);

        clear_choice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.size() > 0) {
                    selecedName=null;
                    selecedId=null;
                    clear_choice.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showLoading(){
        loading_prg_monitor.setVisibility(View.VISIBLE);
    }

    public void closeLoading(){
        loading_prg_monitor.setVisibility(View.GONE);
    }
    /**
     * 打开下拉列表弹窗
     */
    public void initVIew() {
        if(dataList==null||dataList.size()==0){
            return;
        }
        // 声明一个ArrayAdapter用于存放简单数据
        BingoViewModelAdapter adapter = new BingoViewModelAdapter(mContext,dataList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecedId=dataList.get(position).getModelId();
                selecedName=dataList.get(position).getModelName();
                if(mOnSelectedItem!=null){
                    mOnSelectedItem.onSelectedItem(selecedId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    /**
     * 设置数据
     * @param list
     */
    public void setItemsData(List<BingoViewModel> list){
        dataList = list;
    }

    private static class ListItemView{
        TextView tv;
        LinearLayout layout;
    }

    public void addFinish(){
        clear_choice.setVisibility(View.GONE);
    }



    public void initData(String Type){
        Observable mObservable ;
        mObservable= BasePresenter.apiStores1.getAreaId(MyApp.getUserID(), MyApp.getPrivilege()+"","").map(new Func1<HttpAreaResult,ArrayList<Object>>() {
            @Override
            public ArrayList<Object> call(HttpAreaResult o) {
                return o.getSmoke();
            }
        });

        BasePresenter.addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<BingoViewModel>>() {
            @Override
            public void onSuccess(ArrayList<BingoViewModel> model) {
                if(model!=null&&model.size()>0){
                    dataList=model;
                    initVIew();
                }
            }
            @Override
            public void onFailure(int code, String msg) {

            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    public class BingoViewModelAdapter extends BaseAdapter{
        private List<BingoViewModel> mList;
        private Context context ;
        //创建有参构造，参数需要传入两个1.自定义类类型的List，上下文参数
        public BingoViewModelAdapter( Context context,List<BingoViewModel> mList) {
            super();
            this.mList = mList;
            this.context = context;
        }

        //返回长度位所定义List的长度
        @Override
        public int getCount() {
            return mList.size();
        }
        //返回位置为自定义list当前位置
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }
        //返回条目的ID
        @Override
        public long getItemId(int position) {
            return position;
        }

        //这里getView需方法需要重点说明和注意：
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //创建一个LayoutInflater并传入上下文
            LayoutInflater linflater = LayoutInflater.from(context);

            //添加自定义item布局到convertView；（item内容和样式通过自定义方式实现）
            convertView = linflater.inflate(R.layout.spinner_item, null);

            //初始化Item中控件
            TextView textView =(TextView) convertView.findViewById(R.id.text);

            //设置控件显示内容
            textView.setText(mList.get(position).getModelName());

            return convertView;
        }

    }


}

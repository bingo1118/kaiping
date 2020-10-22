package com.smart.cloud.fire.activity.AssetManage.DealAsset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AllAssetListAdapter;
import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.view.AreaChooceList2View;
import com.smart.cloud.fire.view.TypeChooceListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class SearchAssetActivity extends Activity {


    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.uid_edit)
    EditText uid_edit;
    @Bind(R.id.pid_edit)
    AreaChooceList2View pid_edit;
    @Bind(R.id.name_edit)
    EditText name_edit;
    @Bind(R.id.type2_choose)
    TypeChooceListView type_edit;
    @Bind(R.id.nfc_rela)
    RelativeLayout nfc_rela;
    @Bind(R.id.spinner_state)
    Spinner spinner_state;

    private Context mContext;
    private ArrayAdapter<String> adapter;
    String[] ctype = new String[]{"全部","正常", "报警", "维保","过期"};

    String state="";
    String stateName="";
    String mac="";
    String name="";
    String type="";
    String typeId="";
    String areaName="";
    String areaId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_asset);

        ButterKnife.bind(this);
        mContext = this;

        initView();
    }

    private void initView() {
        addFireDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mac=uid_edit.getText().toString();
                name=name_edit.getText().toString();
                if(type_edit.getChoocedArea()!=null){
                    type=type_edit.getChoocedArea().getAreaName();
                    typeId=type_edit.getChoocedArea().getAreaId();
                }
                if(pid_edit.getChoocedArea()!=null){
                    areaName=pid_edit.getChoocedArea().getAreaName();
                    areaId=pid_edit.getChoocedArea().getAreaId();
                }
                Intent i=new Intent(SearchAssetActivity.this, AssetListActivity.class);
                i.putExtra("state",state);
                i.putExtra("stateName",stateName);
                i.putExtra("mac",mac);
                i.putExtra("name",name);
                i.putExtra("type",type);
                i.putExtra("typeId",typeId);
                i.putExtra("areaName",areaName);
                i.putExtra("areaId",areaId);
                i.putExtra("isSearch",true);
                startActivity(i);
                finish();
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        spinner_state.setAdapter(adapter);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            private String positions;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positions = adapter.getItem(position);
                if(position==0){
                    state="";
                    stateName=ctype[position];
                }else{
                    state=(position-1)+"";
                    stateName=ctype[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

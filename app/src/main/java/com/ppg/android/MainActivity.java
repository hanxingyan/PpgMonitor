package com.ppg.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Home page
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    TextView mConnectTv;
    TextView mMonitorTv;
    TextView mDataTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getActivityTitle() {
        return "Home";
    }

    /**
     * evens added for view click
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectTv:
                startActivity(new Intent(this,ConnectActivity.class));
                break;
            case R.id.dataTv:
                startActivity(new Intent(this,DataActivity.class));
                break;
            case R.id.monitorTv:
                startActivity(new Intent(this,MonitorActivity.class));
                break;
        }
    }

    /**
     * view init
     */
    private void initView(){
        mConnectTv = (TextView)findViewById(R.id.connectTv);
        mMonitorTv = (TextView)findViewById(R.id.monitorTv);
        mDataTv = (TextView)findViewById(R.id.dataTv);

        mConnectTv.setOnClickListener(this);
        mMonitorTv.setOnClickListener(this);
        mDataTv.setOnClickListener(this);

    }
}

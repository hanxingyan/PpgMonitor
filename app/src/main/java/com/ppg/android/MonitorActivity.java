package com.ppg.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.ArrayList;

public class MonitorActivity extends BaseActivity{
    private TextView hrTv,rrTv,bpTv,spoTv;
    private ArrayList<String> entrys;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hrTv = (TextView)findViewById(R.id.hrTv);
        rrTv = (TextView)findViewById(R.id.rrTv);
        bpTv = (TextView)findViewById(R.id.bpTv);
        spoTv = (TextView)findViewById(R.id.spoTv);

        caluateTheResult();
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_monitor;
    }

    @Override
    public String getActivityTitle() {
        return "monitor";
    }

    private void caluateTheResult(){

    }
}

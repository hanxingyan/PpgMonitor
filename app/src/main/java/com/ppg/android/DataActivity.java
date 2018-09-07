package com.ppg.android;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataActivity extends BaseActivity implements View.OnClickListener{
    private final int SECOND = 60;
    private final float AXIS_MINIMUM_RED = 55000f;
    private final float AXIS_MAXIMUM_RED = 60000f;
    private final float AXIS_MINIMUM_INFREARED = 56000f;
    private final float AXIS_MAXIMUM_INFREARED = 80000f;

    private final int VISIBLE_POINT = 50;

    LineChart mPPGRedChart;
    LineChart mPPGInfraredChart;
    TextView mToogleStartTv;
    TextView mSaveTv;

    private int mCountSeconds = 1*SECOND;
    private int mHZ = 10;

    private boolean isStoping = true;

    private int index = 0;
    private MyHandler mHandler = new MyHandler();
    ProgressDialog mLoadingDialog;
    private ArrayList<String> mEntrys = new ArrayList<>();
    private String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        mPPGRedChart = (LineChart)findViewById(R.id.redChart);
        mPPGInfraredChart = (LineChart)findViewById(R.id.infraredChart);

        mToogleStartTv= (TextView)findViewById(R.id.startTv);
        mSaveTv = (TextView)findViewById(R.id.saveTv);

        mToogleStartTv.setOnClickListener(this);
        mSaveTv.setOnClickListener(this);
        initRedChart(AXIS_MINIMUM_RED,AXIS_MAXIMUM_RED);
        initInfraredChart(AXIS_MINIMUM_INFREARED,AXIS_MAXIMUM_INFREARED);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startTv:
                if(thread!=null){
                    if(isStoping){
                        mEntrys.clear();
                        thread.start();
                        thread.resumeThread();
                        isStoping = false;
                        mToogleStartTv.setText("stop");
                    }else{
                        isStoping = true;
                        thread.pauseThread();
                        mToogleStartTv.setText("start");
                    }
                }
                break;
            case R.id.saveTv:
                if(mEntrys.size()==0){
                    Toast.makeText(this,"no data to save",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mLoadingDialog == null){
                    mLoadingDialog = ProgressDialog.show(this, "tips:", "saveing data");
                }else{
                    mLoadingDialog.show();
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message msg = mHandler.obtainMessage();
                        try{
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");// HH:mm:ss
                            Date date = new Date(System.currentTimeMillis());

                            FileUtils.writeToFile(path,simpleDateFormat.format(date),mEntrys);
                            msg.what= 0x02;
                            msg.obj = path;
                        }catch (Exception e){
                            e.printStackTrace();
                            msg.what = 0x03;
                        }
                        mHandler.sendMessage(msg);
                    }
                }.start();
                break;
        }
    }

    private void initRedChart(float axisMinimum,float axisMaximum){
        mPPGRedChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("RAW PPG RED");
        mPPGRedChart.setDescription(description);
        // enable touch gestures
        mPPGRedChart.setTouchEnabled(true);

        // enable scaling and dragging
        mPPGRedChart.setDragEnabled(true);
        mPPGRedChart.setScaleEnabled(true);
        mPPGRedChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mPPGRedChart.setPinchZoom(true);

        // set an alternative background color
        mPPGRedChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mPPGRedChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mPPGRedChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mPPGRedChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mPPGRedChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(axisMaximum);
        leftAxis.setAxisMinimum(axisMinimum);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mPPGRedChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void initInfraredChart(float axisMinimum,float axisMaximum){
        mPPGInfraredChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("RAW PPG Infrared");
        mPPGInfraredChart.setDescription(description);
        // enable touch gestures
        mPPGInfraredChart.setTouchEnabled(true);

        // enable scaling and dragging
        mPPGInfraredChart.setDragEnabled(true);
        mPPGInfraredChart.setScaleEnabled(true);
        mPPGInfraredChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mPPGInfraredChart.setPinchZoom(true);

        // set an alternative background color
        mPPGInfraredChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mPPGInfraredChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mPPGInfraredChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mPPGInfraredChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mPPGInfraredChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(axisMaximum);
        leftAxis.setAxisMinimum(axisMinimum);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mPPGInfraredChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private MyThread thread = new MyThread();

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_data;
    }

    @Override
    public String getActivityTitle() {
        return "Data";
    }

    public void addEntry(int index,float red,float infrared) {

        mEntrys.add(index+","+(int)red+","+(int)infrared);
        LineData redData = mPPGRedChart.getData();
        LineData infraredData = mPPGInfraredChart.getData();

        if (redData != null) {

            ILineDataSet set = redData.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                redData.addDataSet(set);
            }
            redData.addEntry(new Entry(index,red), 0);
            redData.notifyDataChanged();

            // let the chart know it's data has changed
            mPPGRedChart.notifyDataSetChanged();
            // limit the number of visible entries
            mPPGRedChart.setVisibleXRangeMaximum(VISIBLE_POINT);
            // move to the latest entry
            mPPGRedChart.moveViewToX(redData.getEntryCount());
        }
        if(infraredData!=null){
            ILineDataSet set = infraredData.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                infraredData.addDataSet(set);
            }
            infraredData.addEntry(new Entry(index,infrared), 0);
            infraredData.notifyDataChanged();

            // let the chart know it's data has changed
            mPPGInfraredChart.notifyDataSetChanged();
            // limit the number of visible entries
            mPPGInfraredChart.setVisibleXRangeMaximum(VISIBLE_POINT);
            // move to the latest entry
            mPPGInfraredChart.moveViewToX(infraredData.getEntryCount());
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    class MyThread extends Thread{
        private final Object lock = new Object();
        private boolean pause = false;

        /**
         * 调用这个方法实现暂停线程
         */
        public void pauseThread() {
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        public void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    while (pause) {
                        onPause();
                    }
                    try {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0x01;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000/mHZ);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    index ++;
                    addEntry(index,(float) (Math.random() * 4000) + AXIS_MINIMUM_RED,(float) (Math.random() * 10000) + AXIS_MINIMUM_INFREARED);
                    break;
                case 0x02:
                    if(mLoadingDialog!=null){
                        mLoadingDialog.dismiss();
                    }
                    Toast.makeText(DataActivity.this,"write to file succsss\n storage path: "+(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    if(mLoadingDialog!=null){
                        mLoadingDialog.dismiss();
                    }
                    Toast.makeText(DataActivity.this,"write to file failure",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}

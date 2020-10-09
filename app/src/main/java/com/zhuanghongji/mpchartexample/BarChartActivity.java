
package com.zhuanghongji.mpchartexample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.zhuanghongji.mpchartexample.custom.DayAxisValueFormatter;
import com.zhuanghongji.mpchartexample.custom.MyAxisValueFormatter;
import com.zhuanghongji.mpchartexample.custom.XYMarkerView;
import com.zhuanghongji.mpchartexample.notimportant.DemoBase;
import com.zhuanghongji.mpchartexample.notimportant.MainActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;

import static com.zhuanghongji.mpchartexample.R.id.chart1;

public class BarChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private final String MY_UUIDSTR = "00001101-0000-1000-8000-00805F9B34FB";
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    //要连接的目标蓝牙设备。
    private final String TARGET_DEVICE_NAME = "XDEPU93F291PYGU";
    public static BluetoothSocket mmSocket;
    public static boolean CONNECT_STATUS = false;
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice device;
    private static final String TAG = "BarChartActivity";
    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.chart1)
    BarChart mChart;

    @BindView(R.id.seekBar1)
    SeekBar mSeekBarX;

    @BindView(R.id.seekBar2)
    SeekBar mSeekBarY;

    @BindView(R.id.tvXMax)
    TextView tvX;

    @BindView(R.id.tvYMax)
    TextView tvY;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mContext = this;
//        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        ConnectThread();

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        setData(12, 50);

        // setting data
        mSeekBarY.setProgress(50);
        mSeekBarX.setProgress(12);

        mSeekBarY.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        // mChart.setDrawLegend(false);

    }

    @Override
    protected void initViews() {
        setupToolbar(mToolbar,R.string.ci_2_name,R.string.ci_2_desc,R.menu.bar,true);
    }

    @Override
    protected void initEvents() {
        mChart.setOnChartValueSelectedListener(this);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionToggleValues: {
                        for (IDataSet set : mChart.getData().getDataSets())
                            set.setDrawValues(!set.isDrawValuesEnabled());

                        mChart.invalidate();
                        break;
                    }
                    case R.id.actionToggleHighlight: {
                        if (mChart.getData() != null) {
                            mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                            mChart.invalidate();
                        }
                        break;
                    }
                    case R.id.actionTogglePinch: {
                        if (mChart.isPinchZoomEnabled())
                            mChart.setPinchZoom(false);
                        else
                            mChart.setPinchZoom(true);

                        mChart.invalidate();
                        break;
                    }
                    case R.id.actionToggleAutoScaleMinMax: {
                        mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                        mChart.notifyDataSetChanged();
                        break;
                    }
                    case R.id.actionToggleBarBorders: {
                        for (IBarDataSet set : mChart.getData().getDataSets())
                            ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                        mChart.invalidate();
                        break;
                    }
                    case R.id.animateX: {
                        mChart.animateX(3000);
                        break;
                    }
                    case R.id.animateY: {
                        mChart.animateY(3000);
                        break;
                    }
                    case R.id.animateXY: {

                        mChart.animateXY(3000, 3000);
                        break;
                    }
                    case R.id.actionSave: {
                        if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                            Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                                    Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                                    .show();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_barchart;
    }

    /**
     * @param
     * @return
     * @description 获得和当前Android已经配对的蓝牙设备
     * @date: 2020/4/5 10:05
     * @author: lxf
     */
    private BluetoothDevice getPairedDevices() {

        // 获得和当前Android已经配对的蓝牙设备。
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            // 把已经取得配对的蓝牙设备名字和地址打印出来。
            Log.d(TAG, pairedDevice.getName() + " : " + pairedDevice.getAddress());
            if (TextUtils.equals(TARGET_DEVICE_NAME, pairedDevice.getName())) {
                Log.d(TAG, "已配对目标设备 -> " + TARGET_DEVICE_NAME);
                return pairedDevice;
            }
        }
        return null;
    }


    /**
     * 开启蓝牙
     */
    private void openBluetooth() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = getPairedDevices();
        if (device == null) {
            // 注册广播接收器。
            // 接收蓝牙发现讯息。
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(mBroadcastReceiver, filter);
//            if (mBluetoothAdapter.startDiscovery()) {
//                Log.d(TAG, "启动蓝牙扫描设备...");
//            }
        } else {
            try {
                connectService(device, MY_UUIDSTR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void connectService(BluetoothDevice device, String uuid) throws IOException {

        try (BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));) {
            Log.d(TAG, "连接服务端...");
            socket.connect();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            /**
             * 阻塞方法
             */
            dos.writeUTF("hello server, I am client");
            dos.flush();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            /**
             * 阻塞方法
             */
            String readUTF = dis.readUTF();
            System.out.println("接收到服务端信息：" + readUTF);
            TextView textViewmes = (TextView) findViewById(R.id.textViewmes);
            ArrayList<Float> list = new ArrayList<>();
            String[] split = readUTF.split(",");
            for(int i=0; i<split.length; i++){
                list.add(Float.parseFloat(split[i]));
            }
            setDatalist(list);
            mChart.invalidate();

            dos.close();
            dis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 连接设备
    public void ConnectThread() {
        ReceiveData receivethread = new ReceiveData();// 连接成功后开启接收数据服务
        receivethread.start();

    }

    // 读数据线程
    private class ReceiveData extends Thread {

        @Override
        public void run() {
            while (true) {
                openBluetooth();
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText("" + (mSeekBarX.getProgress() + 2));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress() + 1 , mSeekBarY.getProgress());
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }


    private void setDatalist(ArrayList list) {
        float start = 0f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + list.size(); i++) {
            float val = (float) (list.get(i));
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The Emotion");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    private void setData(int count, float range) {
        float start = 0f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The Emotion");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }
}

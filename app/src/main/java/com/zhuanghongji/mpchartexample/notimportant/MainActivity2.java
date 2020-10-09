package com.zhuanghongji.mpchartexample.notimportant;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.zhuanghongji.mpchartexample.AboutActivity;
import com.zhuanghongji.mpchartexample.AnotherBarActivity;
import com.zhuanghongji.mpchartexample.BarChartActivity;
import com.zhuanghongji.mpchartexample.BarChartActivityMultiDataset;
import com.zhuanghongji.mpchartexample.BarChartActivitySinus;
import com.zhuanghongji.mpchartexample.BarChartPositiveNegative;
import com.zhuanghongji.mpchartexample.BubbleChartActivity;
import com.zhuanghongji.mpchartexample.CandleStickChartActivity;
import com.zhuanghongji.mpchartexample.CombinedChartActivity;
import com.zhuanghongji.mpchartexample.CubicLineChartActivity;
import com.zhuanghongji.mpchartexample.DynamicalAddingActivity;
import com.zhuanghongji.mpchartexample.FilledLineActivity;
import com.zhuanghongji.mpchartexample.HalfPieChartActivity;
import com.zhuanghongji.mpchartexample.HorizontalBarChartActivity;
import com.zhuanghongji.mpchartexample.InvertedLineChartActivity;
import com.zhuanghongji.mpchartexample.LineChartActivity1;
import com.zhuanghongji.mpchartexample.LineChartActivity2;
import com.zhuanghongji.mpchartexample.LineChartActivityColored;
import com.zhuanghongji.mpchartexample.LineChartTime;
import com.zhuanghongji.mpchartexample.ListViewBarChartActivity;
import com.zhuanghongji.mpchartexample.ListViewMultiChartActivity;
import com.zhuanghongji.mpchartexample.MultiLineChartActivity;
import com.zhuanghongji.mpchartexample.PerformanceLineChart;
import com.zhuanghongji.mpchartexample.PieChartActivity;
import com.zhuanghongji.mpchartexample.PiePolylineChartActivity;
import com.zhuanghongji.mpchartexample.R;
import com.zhuanghongji.mpchartexample.RadarChartActivitry;
import com.zhuanghongji.mpchartexample.RealtimeLineChartActivity;
import com.zhuanghongji.mpchartexample.ScatterChartActivity;
import com.zhuanghongji.mpchartexample.ScrollViewActivity;
import com.zhuanghongji.mpchartexample.StackedBarActivity;
import com.zhuanghongji.mpchartexample.StackedBarActivityNegative;
import com.zhuanghongji.mpchartexample.fragments.SimpleChartDemo;
import com.zhuanghongji.mpchartexample.realm.RealmMainActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;

import static com.zhuanghongji.mpchartexample.notimportant.ListActivity.*;

public class MainActivity2 extends BaseActivity {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private final String MY_UUIDSTR = "00001101-0000-1000-8000-00805F9B34FB";
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    //要连接的目标蓝牙设备。
    private final String TARGET_DEVICE_NAME = "XDEPU93F291PYGU";
    public static BluetoothSocket mmSocket;
    public static OutputStream mmOutStream;
    public static boolean CONNECT_STATUS = false;
    private BluetoothAdapter mBtAdapter;
    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bt_conn)
    Button bt_conn;

//    @BindView(R.id.bt_tomain)
//    Button bt_tomain;

    @BindView(R.id.textViewmes)
    TextView textViewmes;

    @BindView(R.id.textView7)
    TextView textView7;

    private Context mContext;

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
//            TextView textViewmes = (TextView) findViewById(R.id.textViewmes);
            textViewmes.setText(readUTF);
            textViewmes.setTextColor(Color.RED);

            dos.close();
            dis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
        mContext = this;
//        Button bt_conn = (Button) findViewById(R.id.bt_conn);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // 判断蓝牙是否可用
        if (mBtAdapter == null) {
            Toast.makeText(this, "蓝牙是不可用的", Toast.LENGTH_LONG).show();
//            finish();
//            return;
        }

        bt_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                openBluetooth();//                TextView textViewmes = (TextView) findViewById(R.id.textViewmes);
//                textViewmes.setText();
            }
        });

//        Button bt_tomain = (Button) findViewById(R.id.bt_tomain);
//        bt_tomain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
////              intent.putExtra("username",username);
//                startActivity(intent);
////            startActivityForResult(intent,1);
//            }
//        });
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initViews() {
        setupToolbar(mToolbar,R.string.app_name,R.string.action_settings,R.menu.bluetooth,true);
//        mToolbar.setTitle("MPAndroidChart Example");
//        mToolbar.inflateMenu(R.menu.bluetooth);
    }

    @Override
    protected void initEvents() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.open:// 打开蓝牙设备
                        if (!mBtAdapter.isEnabled()) {
                            Intent enableIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                        } else {
                            Toast.makeText(MainActivity2.this, "蓝牙已打开", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        break;

                    case R.id.scan:// 扫描设备
                        if (!mBtAdapter.isEnabled()) {
                            Toast.makeText(MainActivity2.this, "未打开蓝牙", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Intent serverIntent = new Intent(MainActivity2.this, ListActivity.class);
                            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                        }
                        break;

                    case R.id.disconnect:// 断开连接
                        if (!CONNECT_STATUS) {
                            Toast.makeText(MainActivity2.this, "无连接", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(MainActivity2.this, "已断开连接", Toast.LENGTH_SHORT)
                                    .show();
                            cancelconnect();
                        }
                        break;
                }

                return true;
            }
        });
    }


    // 发送数据
    public static void write(String str) {
        if (CONNECT_STATUS) {
            byte[] buffer = str.getBytes();
            try {
                mmOutStream = mmSocket.getOutputStream();
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 连接设备
    public void ConnectThread(BluetoothDevice device) {
        try {
            mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            mBtAdapter.cancelDiscovery();
            mmSocket.connect();
            Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show();
            CONNECT_STATUS = true;
            // 接收数据进程
            ReceiveData receivethread = new ReceiveData();// 连接成功后开启接收数据服务
            receivethread.start();
        } catch (IOException e) {
            Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT)
                    .show();
            CONNECT_STATUS = false;
            try {
                mmSocket.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }
    }

    // 取消链接
    public void cancelconnect() {
        try {
            mmSocket.close();
            CONNECT_STATUS = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    // 创建菜单
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    // 菜单选项点击事件
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.open:// 打开蓝牙设备
//                if (!mBtAdapter.isEnabled()) {
//                    Intent enableIntent = new Intent(
//                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//                } else {
//                    Toast.makeText(MainActivity.this, "蓝牙已打开", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//
//            case R.id.scan:// 扫描设备
//                if (!mBtAdapter.isEnabled()) {
//                    Toast.makeText(MainActivity.this, "未打开蓝牙", Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    Intent serverIntent = new Intent(MainActivity.this, ListActivity.class);
//                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
//                }
//                break;
//
//            case R.id.disconnect:// 断开连接
//                if (!CONNECT_STATUS) {
//                    Toast.makeText(MainActivity.this, "无连接", Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    Toast.makeText(MainActivity.this, "已断开连接", Toast.LENGTH_SHORT)
//                            .show();
//                    cancelconnect();
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    // Intent接收器，返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            // 当DeviceListActivity返回与设备连接的消息
            if (resultCode == Activity.RESULT_OK) {
                // 得到链接设备的MAC
                String address = data.getExtras().getString(
                        EXTRA_DEVICE_ADDRESS, "");
                // 得到BLuetoothDevice对象
                if (!TextUtils.isEmpty(address)) {
                    BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
                    ConnectThread(device);
                }
            }
        }
    }


    // 读数据线程
    private class ReceiveData extends Thread {
        InputStream mmInStream;

        private ReceiveData() {
            try {
                mmInStream = mmSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                DataOutputStream dos = new DataOutputStream(mmSocket.getOutputStream());
                /**
                 * 阻塞方法
                 */
                dos.writeUTF("hello server, I am client");
                dos.flush();

                DataInputStream dis = new DataInputStream(mmSocket.getInputStream());
                /**
                 * 阻塞方法
                 */
                String readUTF = dis.readUTF();
                System.out.println("接收到服务端信息2：" + readUTF);
//                TextView textView7 = (TextView) findViewById(R.id.textView7);
                textView7.setText(readUTF);
                textView7.setTextColor(Color.RED);
                dos.close();
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            int bytes;
//            byte[] buffer = new byte[256];
//            while (true) {
//
//                try { // 接收数据
//                    bytes = mmInStream.read(buffer);
//                    final String readStr = new String(buffer, 0, bytes);
////                    Log.d(TAG,readStr);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}

package com.example.administrator.wifidemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    /**
     * Called when the activity is first created.
     */
    private TextView allNetWork;
    private Button scan;
    private Button start;
    private Button stop;
    private Button check;
    private WifiAdmin mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb = new StringBuffer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWifiAdmin = new WifiAdmin(MainActivity.this);
        init();
    }

    public void init() {
        allNetWork = (TextView) findViewById(R.id.allNetWork);
        scan = (Button) findViewById(R.id.scan);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        check = (Button) findViewById(R.id.check);
        scan.setOnClickListener(new MyListener());
        start.setOnClickListener(new MyListener());
        stop.setOnClickListener(new MyListener());
        check.setOnClickListener(new MyListener());
    }

    public void getList(View view) {
        String bssid = mWifiAdmin.getBSSID();
        Log.e("bssid", bssid);
        List<WifiConfiguration> configuration = mWifiAdmin.getConfiguration();
        Log.e("configuration", configuration.toString());
        int ipAddress = mWifiAdmin.getIpAddress();
        Log.e("ipAddress", ipAddress + "");
        String macAddress = mWifiAdmin.getMacAddress();
        Log.e("macAddress", macAddress.toString());
        int netWordId = mWifiAdmin.getNetWordId();
        Log.e("netWordId", netWordId + "");
        String wifiInfo = mWifiAdmin.getWifiInfo();
        Log.e("wifiInfo", wifiInfo.toString());
        List<ScanResult> wifiList = mWifiAdmin.getWifiList();
        Log.e("wifiList", wifiList.toString());
    }

    private class MyListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.scan://扫描网络
                    checkPermission();

                    break;
                case R.id.start://打开Wifi
                    mWifiAdmin.openWifi();
                    Toast.makeText(MainActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
                    break;
                case R.id.stop://关闭Wifi
                    mWifiAdmin.closeWifi();
                    Toast.makeText(MainActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
                    break;
                case R.id.check://Wifi状态
                    Toast.makeText(MainActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
                    break;
                default:
                    break;
            }
        }

    }

    public void getAllNetWorkList() {
        // 每次点击扫描之前清空上一次的扫描结果
        if (sb != null) {
            sb = new StringBuffer();
        }
        //开始扫描网络
        mWifiAdmin.startScan();
        list = mWifiAdmin.getWifiList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                //得到扫描结果
                mScanResult = list.get(i);
                sb = sb.append(mScanResult.BSSID + "  ").append(mScanResult.SSID + "   ")
                        .append(mScanResult.capabilities + "   ").append(mScanResult.frequency + "   ")
                        .append(mScanResult.level + "\n\n");
            }
            allNetWork.setText("扫描到的wifi网络：\n" + sb.toString());
        }
    }

    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        (permissions.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    getAllNetWorkList();
                    //list is still empty
                }
                else {
                    // Permission Denied
                    Toast.makeText(this, "1111", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

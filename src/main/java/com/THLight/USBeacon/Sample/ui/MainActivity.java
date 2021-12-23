/**
 * ==============================================================
 */
package com.THLight.USBeacon.Sample.ui;
/**
 * ==============================================================
 */

import java.io.Console;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.BatteryPowerData;
import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconData;
import com.THLight.USBeacon.App.Lib.USBeaconList;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.Sample.R;
import com.THLight.USBeacon.Sample.ScanIBeaconData;
import com.THLight.USBeacon.Sample.entity.ScanDeviceItemEntity;
import com.THLight.USBeacon.Sample.thLightApplication;
import com.THLight.USBeacon.Sample.THLConfig;
import com.THLight.USBeacon.Sample.service.ScannerService;
import com.THLight.USBeacon.Sample.ui.recyclerview.CustomLinearLayoutManager;
import com.THLight.USBeacon.Sample.ui.recyclerview.CustomRecyclerView;
import com.THLight.USBeacon.Sample.ui.recyclerview.RecyclerViewAdapter;
import com.THLight.USBeacon.Sample.ui.recyclerview.ScanDeviceViewType;
import com.THLight.Util.THLLog;


//-------------------------10/26新增-------------------------------
/*import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;*/
//-------------------------10/26新增-------------------------------

public class MainActivity extends Activity implements USBeaconConnection.OnResponse, CustomRecyclerView.CustomRecyclerViewScrollListener {

    /**
     * this UUID is generate by Server while register a new account.
     */
    private final UUID QUERY_UUID = UUID.fromString("BB746F72-282F-4378-9416-89178C1019FC");
    /**
     * server http api url.
     */
    private static final String HTTP_API = "http://www.usbeacon.com.tw/api/func";
    private static String STORE_PATH = Environment.getExternalStorageDirectory().toString() + "/USBeaconSample/";
    private static final int REQ_ENABLE_BT = 2000;
    private static final int MSG_SERVER_RESPONSE = 3000;
    private static final int TIME_BEACON_TIMEOUT = 30000;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private THLConfig Config = null;
    private List<ScanIBeaconData> scanIBeaconDataList = new ArrayList<>();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private USBeaconConnection usBeaconConnection = new USBeaconConnection();
    private RecyclerViewAdapter scanDeviceAdapter;
    private List<ScanDeviceItemEntity> scanDeviceItemEntityList = new ArrayList<>();
    private LocalServiceConnection localServiceConnection = new LocalServiceConnection();
    private ReceiveMessageHandler receiveMessageHandler = new ReceiveMessageHandler(this);
    private int index;
    private boolean isFirstPage = true;
    private boolean isNeedNotify = true;
    private boolean isFront = true;
    private CustomRecyclerView recyclerView;
    private ScannerService scannerService;
    private PowerManager.WakeLock serviceWakeLock;
    private String CHANNEL_ID = "Coder";
    public static boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Config = thLightApplication.Config;
        permissionCheck();
        CreateStoreFolder();
        networkCheck();
        bindRecyclerView();
        startScanDevice();

        //-------------------------10/26新增-------------------------------
        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        /**初始化介面控件與點擊事件*/
        /*Button btDefault,btCustom;
        btDefault = findViewById(R.id.button_DefaultNotification);
        btCustom = findViewById(R.id.button_CustomNotification);
        btDefault.setOnClickListener(onDefaultClick);
        btCustom.setOnClickListener(onCustomClick);*/
        Button btn_to_Login = (Button) findViewById(R.id.btn_to_Login);

        btn_to_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
        //-------------------------10/26新增-------------------------------

        //螢幕關閉, service 保持運作
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            serviceWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ScannerService.class.getName());
            serviceWakeLock.acquire();
        }
    }

    //-------------------------10/26新增-------------------------------

    //-------------------------10/26新增-------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFront = false;
        scannerService.startForegroundService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(localServiceConnection);
        if (serviceWakeLock != null) {
            serviceWakeLock.release();
            serviceWakeLock = null;
        }
    }

    private void bindRecyclerView() {   //第四個執行，作用:待研究
        recyclerView = findViewById(R.id.beacon_recyclerView);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        ArrayList<RecyclerViewAdapter.ViewTypeInterface> itemTypeList = new ArrayList<>();
        ScanDeviceViewType scanDeviceViewType = new ScanDeviceViewType(scanDeviceItemEntityList);
        itemTypeList.add(scanDeviceViewType);
        scanDeviceAdapter = new RecyclerViewAdapter(this, itemTypeList);
        recyclerView.setCustomRecyclerViewScrollListener(this);
        recyclerView.setAdapter(scanDeviceAdapter);
    }

    //Do scanning in service.
    private void startScanDevice() {    //第五個執行，作用:持續掃描
        //Check the BT is on or off on the phone.
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_ENABLE_BT);   // A request for open Bluetooth
        } else {
            Intent service = new Intent(this, ScannerService.class);
            bindService(service, localServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void CreateStoreFolder() {  //第二個執行，作用:創造儲存的資料夾
        /** create store folder. */
        File file = new File(STORE_PATH);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(this, "Create folder(" + STORE_PATH + ") failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void networkCheck() {   //第三個執行，作用:檢查網路連接
        /** check network is available or not. */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null == networkInfo || (!networkInfo.isConnected())) {
                dlgNetworkNotAvailable();     //Show a dialog to inform users to enable the network.
            } else {
                THLLog.d("debug", "NI not null");

                NetworkInfo niMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (null != niMobile) {
                    boolean isMobileInt = niMobile.isConnectedOrConnecting();

                    if (isMobileInt) {
                        dlgNetworkMobile();  //Show a dialog to make sure to use the Mobile Internet
                    } else {
                        USBeaconServerInfo usBeaconServerInfo = new USBeaconServerInfo();

                        usBeaconServerInfo.serverUrl = HTTP_API;
                        usBeaconServerInfo.queryUuid = QUERY_UUID;
                        usBeaconServerInfo.downloadPath = STORE_PATH;

                        usBeaconConnection.setServerInfo(usBeaconServerInfo, this);
                        usBeaconConnection.checkForUpdates();
                    }
                }
            }
        } else {
            THLLog.d("debug", "CM null");
        }
    }

    //Check the locate permission for BT scan in Android 6.0
    void permissionCheck() {    //第一個執行，作用:確認location access掃描權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }

            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs writing access");
                builder.setMessage("Please grant writing access.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    //不會動到
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("debug", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                break;
            }

            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("debug", "writing permission granted");
                    usBeaconConnection.checkForUpdates();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since writing access has not been granted, this app will not be able to update data.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                break;
        }
    }


    public void onResponse(int msg) {
        THLLog.d("debug", "Response(" + msg + ")");
        receiveMessageHandler.obtainMessage(MSG_SERVER_RESPONSE, msg, 0).sendToTarget();
    }

    //不會動到
    public void dlgNetworkNotAvailable() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setTitle("Network");
        alertDialog.setMessage("Please enable your network for updating beacon list.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * ==========================================================
     */
    //不會動到
    public void dlgNetworkMobile() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setTitle("3G");
        alertDialog.setMessage("App will send/recv data via Mobile Internet, this may result in significant data charges.");

        // To check yes or no of using mobile Internet.
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Allow", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Config.allow3G = true;
                alertDialog.dismiss();
                USBeaconServerInfo info = new USBeaconServerInfo();

                info.serverUrl = HTTP_API;
                info.queryUuid = QUERY_UUID;
                info.downloadPath = STORE_PATH;

                usBeaconConnection.setServerInfo(info, MainActivity.this);
                usBeaconConnection.checkForUpdates();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Config.allow3G = false;
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private static class ReceiveMessageHandler extends Handler {
        WeakReference<MainActivity> mainActivityWeakReference;

        ReceiveMessageHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        //不會動到
        public void handleMessage(Message msg) {
            if (mainActivityWeakReference == null) {
                return;
            }
            MainActivity activity = mainActivityWeakReference.get();
            switch (msg.what) {
                case MSG_SERVER_RESPONSE:
                    switch (msg.arg1) {
                        case USBeaconConnection.MSG_NETWORK_NOT_AVAILABLE:
                            break;

                        // Get the data from Server by the "QUERY_UUID"
                        case USBeaconConnection.MSG_HAS_UPDATE:
                            //Download beacon data to a zip file, and send MSG_DATA_UPDATE_FINISHED
                            activity.usBeaconConnection.downloadBeaconListFile();
                            Toast.makeText(activity, "HAS_UPDATE.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_HAS_NO_UPDATE:
                            Toast.makeText(activity, "No new BeaconList.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FINISHED:
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FAILED:
                            Toast.makeText(activity, "Download file failed!", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FINISHED: {
                            USBeaconList usBeaconList = activity.usBeaconConnection.getUSBeaconList();  //Get the beacon list that was from Server

                            if (null == usBeaconList) {
                                Toast.makeText(activity, "Data Updated failed.", Toast.LENGTH_SHORT).show();
                                THLLog.d("debug", "update failed.");
                            } else if (usBeaconList.getList().isEmpty()) {
                                Toast.makeText(activity, "Data Updated but empty.", Toast.LENGTH_SHORT).show();
                                THLLog.d("debug", "this account doesn't contain any devices.");
                            } else {
                                String BeaconData = "";
                                Toast.makeText(activity, "Data Updated(" + usBeaconList.getList().size() + ")", Toast.LENGTH_SHORT).show();

                                for (USBeaconData data : usBeaconList.getList()) {
                                    BeaconData = BeaconData + "Name(" + data.name + "), Ver(" + data.major + "." + data.minor + ")\n";
                                    THLLog.d("debug", "Name(" + data.name + "), Ver(" + data.major + "." + data.minor + ")");
                                }
                            }
                        }
                        break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FAILED:
                            Toast.makeText(activity, "UPDATE_FAILED!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    }

    @Override
    public void onRecyclerViewScrolled(CustomRecyclerView recyclerView, boolean isScrollBottom) {
        isNeedNotify = false;
        if (isScrollBottom) {
            if (!isFirstPage && scanIBeaconDataList != null) {
                addScanDeviceItemList();
            }
        }
    }

    @Override
    public void onRecyclerViewIdle() {
        isNeedNotify = true;
        removeIdleBeaconData();
    }

    private class LocalServiceConnection implements ServiceConnection, ScannerService.ScanResponseListener {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            scannerService = ((ScannerService.ScannerServiceBinder) iBinder).getService();
            scannerService.setScanResponseListener(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            scannerService = null;
        }

        @Override
        public void onScanDeviceResponse(iBeaconData iBeaconData) {
            System.out.println("onScanDeviceResponse");
            MainActivity.isConnect = true;
            isFirstPage = scanDeviceItemEntityList.size() < 20;
            addOrUpdateIBeaconDataList(iBeaconData);
            if (isFirstPage) {
                addScanDeviceItemList();
            }
            if (isNeedNotify && isFront) {
                scanDeviceAdapter.notifyItemRangeChanged(index, 20);
            }
        }

        @Override
        public void onBatteryPowerUpdate(BatteryPowerData batteryPowerData) {
            for (ScanDeviceItemEntity entity : scanDeviceItemEntityList) {
                if (entity.getMacAddress().equals(batteryPowerData.macAddress)) {
                    entity.setBatteryPower(String.valueOf(batteryPowerData.batteryPower));
                }
            }
        }
    }

    private void addOrUpdateIBeaconDataList(iBeaconData iBeaconData) {
        ScanIBeaconData data = ScanIBeaconData.copyOf(iBeaconData);
        data.lastUpdate = System.currentTimeMillis();

        index = 0;
        for (ScanDeviceItemEntity scanDeviceItemEntity : scanDeviceItemEntityList) {
            if (data.macAddress.equalsIgnoreCase(scanDeviceItemEntity.getMacAddress())) {
                scanDeviceItemEntity.setRssi(String.valueOf(data.rssi));
                scanDeviceItemEntity.setLastUpdateTime(data.lastUpdate);
                return;
            }
            index++;
        }
        for (ScanIBeaconData scanIBeaconData : scanIBeaconDataList) {
            if (data.macAddress.equalsIgnoreCase(scanIBeaconData.macAddress)) {
                return;
            }
        }
        scanIBeaconDataList.add(data);
    }

    public void removeIdleBeaconData() {
        ScanDeviceItemEntity entity;
        int length = scanDeviceItemEntityList.size();
        for (int i = length - 1; 0 <= i; i--) {
            entity = scanDeviceItemEntityList.get(i);
            if (entity != null && (System.currentTimeMillis() - entity.getLastUpdateTime()) > TIME_BEACON_TIMEOUT) {
                scanDeviceItemEntityList.remove(entity);
            }
        }
    }

    private void addScanDeviceItemList() {
        for (int i = 0; i < scanIBeaconDataList.size(); i++) {
            //Each time add 20 data at most to the UI list.
            if (i > 20) {
                return;
            }
            ScanIBeaconData data = scanIBeaconDataList.remove(i);
            ScanDeviceItemEntity entity = new ScanDeviceItemEntity(
                    data.beaconUuid.toUpperCase(),
                    String.valueOf(data.major),
                    String.valueOf(data.minor),
                    String.valueOf(data.rssi),
                    String.valueOf(data.batteryPower),
                    data.macAddress,
                    data.lastUpdate);
            scanDeviceItemEntityList.add(entity);
        }
    }
}
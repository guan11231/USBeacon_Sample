package com.THLight.USBeacon.Sample.ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;

import com.THLight.USBeacon.Sample.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String get_user_name = (String) intent.getExtras().getString("user");
        /**接收點擊事件*/
        if(MainActivity.isConnect == false) {
            Toast.makeText(context, "未偵測到Beacon", Toast.LENGTH_LONG).show();
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
        else {
            switch (intent.getAction()) {
                case "roll_call":
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            MysqlCon con = new MysqlCon();
                            con.run();
                            con.roll_call(get_user_name);
                        }
                    }).start();

                    Toast.makeText(context, "您已完成點名!", Toast.LENGTH_LONG).show();
                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                    break;
                /*case "Close":
                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                    break;*/
            }
        }
    }
}
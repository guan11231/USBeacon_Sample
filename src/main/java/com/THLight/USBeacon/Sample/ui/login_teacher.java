package com.THLight.USBeacon.Sample.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.THLight.USBeacon.Sample.R;

public class login_teacher extends Activity {

    private String CHANNEL_ID = "Coder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        TextView user_name = (TextView) findViewById(R.id.user_name);
        final String get_user_name = (String)getIntent().getExtras().getString("user");
        user_name.setText(get_user_name);
        final ScrollView layout = (ScrollView) findViewById(R.id.layout);
        Button btn_to_game = (Button) findViewById(R.id.btn_to_game);
        Button btn_to_create_sign_up_form = (Button) findViewById(R.id.btn_to_create_sign_up_form);
        Button btn_to_open_roll_call = (Button) findViewById(R.id.btn_to_open_roll_call);
        Button btn_to_close_roll_call = (Button) findViewById(R.id.btn_to_close_roll_call);
        Button btn_to_search_today = (Button) findViewById(R.id.btn_to_search_today);
        Button btn_to_search_student = (Button) findViewById(R.id.btn_to_search_student);
        Button btn_to_delete_table = (Button) findViewById(R.id.btn_to_delete_table);
        /*Bundle bundle = new Bundle();
        bundle.putString("user",get_user_name);
        intent.putExtras(bundle);*/

        btn_to_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, login_teacher.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_create_sign_up_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, classProduce.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_open_roll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, rollCall.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_close_roll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        MysqlCon con = new MysqlCon();
                        con.run();
                        con.setFlag0(get_user_name);
                        layout.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "已關閉課程點名", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

        btn_to_search_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, todayAttendantInquire.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_search_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, Attendant_teacher.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_to_delete_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_teacher.this, Attendant_teacher.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", get_user_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    /*
    // Disable back button
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }*/

}
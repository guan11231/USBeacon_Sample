package com.THLight.USBeacon.Sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.THLight.USBeacon.Sample.R;

public class sign_up extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("註冊頁面");
        setContentView(R.layout.activity_sign_up);

        Button btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        Button btn_to_login = (Button) findViewById(R.id.btn_to_login);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 取得 EditText 資料
                        final EditText account_text = (EditText) findViewById(R.id.student_id);
                        final EditText password_text = (EditText) findViewById(R.id.password);
                        final EditText user_name_text = (EditText) findViewById(R.id.name);
                        final EditText phone_number_text = (EditText) findViewById(R.id.phone);
                        String account = account_text.getText().toString();
                        String password = password_text.getText().toString();
                        String user_name = user_name_text.getText().toString();
                        String phone_number = phone_number_text.getText().toString();
                        MysqlCon con = new MysqlCon();
                        con.run();

                        if(account.equals("") || password.equals("") || user_name.equals("") || phone_number.equals("")) {
                            layout.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "輸入資料不可為空", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if(con.checkIfExistId(account) == true) {
                            layout.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "此學號已被註冊過", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            // 將資料寫入資料庫
                            con.insertData(account, password, user_name, phone_number);
                            layout.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "帳號註冊成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 清空 EditText
                            account_text.post(new Runnable() {
                                public void run() {
                                    account_text.setText("");
                                    password_text.setText("");
                                    user_name_text.setText("");
                                    phone_number_text.setText("");
                                }
                            });
                            Intent intent = new Intent();
                            intent.setClass(sign_up.this, login.class);
                            startActivity(intent);
                        }
                    }
                }).start();

            }
        });

        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(sign_up.this, login.class);
                startActivity(intent);
            }
        });

    }

    // Disable back button
    /*@Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }*/
}

package com.THLight.USBeacon.Sample.ui;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import com.THLight.USBeacon.Sample.R;

public class Attendant extends Activity {
    ArrayList<String> classData;
    String[] splittedClassData;
    String inquireClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendant);

        final String get_user_name = (String)getIntent().getExtras().getString("user");
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();
                classData = con.getData_class();

                int id = 1;
                int btnId = 100;

                for (int i = 0; i < classData.size(); i++) {
                    splittedClassData = classData.get(i).split(",");
                    TableRow tableRow = new TableRow(Attendant.this);

                    TextView textView1 = new TextView(Attendant.this);
                    textView1.setId(id);
                    textView1.setBackgroundColor(Color.WHITE);
                    textView1.setText(splittedClassData[0]);
                    textView1.setPadding(50,50,50,50);
                    textView1.setTextSize(20);
                    textView1.setTypeface(null, Typeface.BOLD);
                    textView1.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView1Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView1Params.setMargins(1,0,1,1);
                    textView1.setLayoutParams(TextView1Params);


                    TextView textView2 = new TextView(Attendant.this);
                    textView2.setText(splittedClassData[1]);
                    textView2.setId(id + 1);
                    textView2.setBackgroundColor(Color.WHITE);
                    textView2.setPadding(50,50,50,50);
                    textView2.setTextSize(20);
                    textView2.setTypeface(null, Typeface.BOLD);
                    textView2.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView2Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView2Params.setMargins(1,0,1,1);
                    textView2.setLayoutParams(TextView2Params);


                    TextView textView3 = new TextView(Attendant.this);
                    textView3.setText(splittedClassData[2]);
                    textView3.setId(id + 2);
                    textView3.setBackgroundColor(Color.WHITE);
                    textView3.setPadding(50,50,50,50);
                    textView3.setTextSize(20);
                    textView3.setTypeface(null, Typeface.BOLD);
                    textView3.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView3Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView3Params.setMargins(1,0,1,1);
                    textView3.setLayoutParams(TextView3Params);

                    TextView textView4 = new TextView(Attendant.this);
                    textView4.setText(splittedClassData[3]);
                    textView4.setId(id + 3);
                    textView4.setBackgroundColor(Color.WHITE);
                    textView4.setPadding(50,50,50,50);
                    textView4.setTextSize(20);
                    textView4.setTypeface(null, Typeface.BOLD);
                    textView4.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView4Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView4Params.setMargins(1,0,1,1);
                    textView4.setLayoutParams(TextView3Params);

                    TextView textView5 = new TextView(Attendant.this);
                    textView5.setText(splittedClassData[4]);
                    textView5.setId(id + 4);
                    textView5.setBackgroundColor(Color.WHITE);
                    textView5.setPadding(50,50,50,50);
                    textView5.setTextSize(20);
                    textView5.setTypeface(null, Typeface.BOLD);
                    textView5.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView5Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView5Params.setMargins(1,0,1,1);
                    textView5.setLayoutParams(TextView3Params);

                    Button okBtn = new Button(Attendant.this);

                    okBtn.setId(btnId);
                    okBtn.setText("查詢");
                    okBtn.setTextSize(20);
                    okBtn.setPadding(10,10,10,10);
                    TableRow.LayoutParams okBtnParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    okBtnParams.setMargins(1,0,1,1);
                    okBtnParams.gravity = Gravity.CENTER;
                    okBtn.setLayoutParams(okBtnParams);

                    okBtn.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inquireClassName =(String)textView1.getText();

                            Intent intent = new Intent();
                            intent.setClass(Attendant.this, Attendant_inquire.class);
                            Bundle bundle = new Bundle();bundle.putString("user", get_user_name);
                            bundle.putString("className", inquireClassName);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableRow.addView(textView3);
                    tableRow.addView(textView4);
                    tableRow.addView(textView5);
                    tableRow.addView(okBtn);

                    id += 5;
                    btnId += 1;

                    tableLayout.post(new Runnable() {
                        public void run() {
                            tableLayout.addView(tableRow);
                        }
                    });



                }

            }
        }).start();



    }
}

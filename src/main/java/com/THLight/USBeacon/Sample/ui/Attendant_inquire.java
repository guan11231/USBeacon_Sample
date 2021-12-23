package com.THLight.USBeacon.Sample.ui;

import android.app.Activity;

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

public class Attendant_inquire extends Activity {
    String inquireClass;
    String inquireStudent;
    ArrayList<String> temp;
    int inquireAccount;
    String[] splittedStudentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendant_inquire);

        final String get_inquire_className = (String)getIntent().getExtras().getString("className");
        final String get_user_name = (String)getIntent().getExtras().getString("user");

        TextView textview = (TextView) findViewById(R.id.title);
        TextView textview1 = (TextView) findViewById(R.id.name);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        inquireClass = get_inquire_className;
        inquireStudent = get_user_name;
        textview.setText("課名：" + inquireClass);
        textview1.setText("學生：" + inquireStudent);

        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();

                temp = con.getClass_Student(inquireClass, inquireStudent);
                splittedStudentData = temp.get(0).split(",");

                int id = 0;


                for (int i = 1; i <= 20; i++) {
                    TableRow tableRow = new TableRow(Attendant_inquire.this);

                    TextView textView1 = new TextView(Attendant_inquire.this);
                    textView1.setId(id + 1);
                    textView1.setBackgroundColor(Color.WHITE);
                    textView1.setText("第"+ i +"週");
                    textView1.setPadding(50,50,50,50);
                    textView1.setTextSize(20);
                    textView1.setTypeface(null, Typeface.BOLD);
                    textView1.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView1Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView1Params.setMargins(1,0,1,1);
                    textView1.setLayoutParams(TextView1Params);

                    TextView textView2 = new TextView(Attendant_inquire.this);

                    if(splittedStudentData[1 + i].equals("1")){
                        textView2.setText("已簽到");
                        textView2.setTextColor(Color.GREEN);
                    }else {
                        textView2.setText("未簽到");
                        textView2.setTextColor(Color.RED);
                    }

                    textView2.setId(id + 2);
                    textView2.setBackgroundColor(Color.WHITE);
                    textView2.setPadding(50,50,50,50);
                    textView2.setTextSize(20);
                    textView2.setTypeface(null, Typeface.BOLD);
                    textView2.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView2Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView2Params.setMargins(1,0,1,1);
                    textView2.setLayoutParams(TextView2Params);


                    tableRow.addView(textView1);
                    tableRow.addView(textView2);


                    tableLayout.post(new Runnable() {
                        public void run() {
                            tableLayout.addView(tableRow);
                        }
                    });

                    id += 2;

                }
            }
        }).start();
    }
}

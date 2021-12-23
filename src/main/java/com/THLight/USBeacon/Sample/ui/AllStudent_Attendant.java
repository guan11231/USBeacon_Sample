package com.THLight.USBeacon.Sample.ui;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import com.THLight.USBeacon.Sample.R;

public class AllStudent_Attendant extends Activity {
    String inquireClass;
    ArrayList<String> temp;
    String[] splittedAllStudentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allstudent_attendant);

        final String get_inquire_className = (String)getIntent().getExtras().getString("className");
        TextView textview = (TextView) findViewById(R.id.title);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        inquireClass = get_inquire_className;
        textview.setText("課名：" + inquireClass);


        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();
                temp = con.getClass_AllStudent(inquireClass);

                int id = 0;

                for (int i = 1; i < temp.size(); i++) {
                    splittedAllStudentData = temp.get(i).split(",");

                    TableRow tableRow = new TableRow(AllStudent_Attendant.this);

                    TextView textView1 = new TextView(AllStudent_Attendant.this);
                    textView1.setId(id + 1);
                    textView1.setBackgroundColor(Color.WHITE);
                    textView1.setText(splittedAllStudentData[0]);
                    textView1.setPadding(50,50,50,50);
                    textView1.setTextSize(20);
                    textView1.setTypeface(null, Typeface.BOLD);
                    textView1.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView1Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView1Params.setMargins(1,0,1,1);
                    textView1.setLayoutParams(TextView1Params);
                    tableRow.addView(textView1);


                    TextView textView2 = new TextView(AllStudent_Attendant.this);
                    textView2.setId(id + 2);
                    textView2.setBackgroundColor(Color.WHITE);
                    textView2.setText(splittedAllStudentData[1]);
                    textView2.setPadding(50,50,50,50);
                    textView2.setTextSize(20);
                    textView2.setTypeface(null, Typeface.BOLD);
                    textView2.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams TextView2Params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    TextView2Params.setMargins(1,0,1,1);
                    textView2.setLayoutParams(TextView2Params);
                    tableRow.addView(textView2);

                    for (int j = 2; j < 22; j++) {
                        TextView textViewAttendant = new TextView(AllStudent_Attendant.this);

                        if(splittedAllStudentData[j].equals("1")){
                            textViewAttendant.setText("已簽到");
                            textViewAttendant.setTextColor(Color.GREEN);
                        }else{
                            textViewAttendant.setText("未簽到");
                            textViewAttendant.setTextColor(Color.RED);
                        }

                        textViewAttendant.setId(id + j + 1);
                        textViewAttendant.setBackgroundColor(Color.WHITE);
                        textViewAttendant.setPadding(50,50,50,50);
                        textViewAttendant.setTextSize(20);
                        textViewAttendant.setTypeface(null, Typeface.BOLD);
                        textViewAttendant.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams textViewAttendantParams = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                        textViewAttendantParams.setMargins(1,0,1,1);
                        textViewAttendant.setLayoutParams(textViewAttendantParams);

                        tableRow.addView(textViewAttendant);
                    }

                    tableLayout.post(new Runnable() {
                        public void run() {
                            tableLayout.addView(tableRow);
                        }
                    });



                    id += 22;
                }

            }
        }).start();

    }
}

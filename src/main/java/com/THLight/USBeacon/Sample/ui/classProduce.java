package com.THLight.USBeacon.Sample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import com.THLight.USBeacon.Sample.R;

public class classProduce extends Activity {

    ArrayList<String> data = new ArrayList<String>();
    int id = 0;
    String[] NameRoomDayTime = new String[4];
    LinkedList studentTable = new LinkedList();

    RadioGroup RadioGroupClassroom;
    RadioButton RadioButtonClassroom1;
    RadioButton RadioButtonClassroom2;
    RadioButton RadioButtonClassroom3;
    RadioButton RadioButtonClassroom4;

    RadioGroup RadioGroupDay;
    RadioButton RadioButtonMonday;
    RadioButton RadioButtonTuesday;
    RadioButton RadioButtonWednesday;
    RadioButton RadioButtonThursday;
    RadioButton RadioButtonFriday;

    RadioGroup RadioGroupHour;
    RadioButton RadioButtonTime1;
    RadioButton RadioButtonTime2;
    RadioButton RadioButtonTime3;
    RadioButton RadioButtonTime4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classproduce);

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.check_add_layout);

        RadioButtonClassroom1 = (RadioButton)findViewById(R.id.radioButton4);
        RadioButtonClassroom2 = (RadioButton)findViewById(R.id.radioButton5);
        RadioButtonClassroom3 = (RadioButton)findViewById(R.id.radioButton6);
        RadioButtonClassroom4 = (RadioButton)findViewById(R.id.radioButton7);

        RadioButtonMonday = (RadioButton)findViewById(R.id.radioButton8);
        RadioButtonTuesday = (RadioButton)findViewById(R.id.radioButton9);
        RadioButtonWednesday = (RadioButton)findViewById(R.id.radioButton10);
        RadioButtonThursday = (RadioButton)findViewById(R.id.radioButton11);
        RadioButtonFriday = (RadioButton)findViewById(R.id.radioButton12);

        RadioButtonTime1 = (RadioButton)findViewById(R.id.radioButton13);
        RadioButtonTime2 = (RadioButton)findViewById(R.id.radioButton14);
        RadioButtonTime3 = (RadioButton)findViewById(R.id.radioButton15);
        RadioButtonTime4 = (RadioButton)findViewById(R.id.radioButton16);

        RadioGroupClassroom = (RadioGroup) findViewById(R.id.radioGroupClassroom);
        RadioGroupDay = (RadioGroup) findViewById(R.id.radioGroupDay);
        RadioGroupHour = (RadioGroup) findViewById(R.id.radioGroupTime);

        RadioGroupClassroom.setOnCheckedChangeListener(radioGroupRoomOnCheckedChange);
        RadioGroupDay.setOnCheckedChangeListener(radioGroupDayOnCheckedChange);
        RadioGroupHour.setOnCheckedChangeListener(radioGroupTimeOnCheckedChange);




        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();
                data = con.getData_accountUsernamePhonenumber();
                for (int i = 0; i < data.size(); i++) {
                    Log.v("OK",data.get(i));
                }
                for (int i = 0; i < data.size(); i++) {
                    id = i;
                    CheckBox checkBox = new CheckBox(classProduce.this);
                    checkBox.setId(id);
                    checkBox.setText(data.get(i) + id);
                    checkBox.setTextSize(16);

                    LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    checkParams.setMargins(10, 10, 10, 10);
                    checkParams.gravity = Gravity.LEFT;

                    checkBox.setOnCheckedChangeListener(checkBoxPerson);

                    parentLayout.post(new Runnable() {
                        public void run() {
                            parentLayout.addView(checkBox, checkParams);
                        }
                    });

                }
                Button okBtn = new Button(classProduce.this);
                int btnId = 100;
                okBtn.setId(btnId);
                okBtn.setText("確認");
                okBtn.setTextSize(20);
                LinearLayout.LayoutParams okBtnParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                okBtnParams.setMargins(10,10,10,10);
                okBtnParams.gravity = Gravity.CENTER;
                okBtn.setOnClickListener(okBtnEvent);
                parentLayout.post(new Runnable() {
                    public void run() {
                        parentLayout.addView(okBtn, okBtnParams);
                    }
                });
            }
        }).start();

    }

    private RadioGroup.OnCheckedChangeListener radioGroupRoomOnCheckedChange =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch (checkedId)
                    {
                        case R.id.radioButton4: //case RadioButtonClassroom1.getId():
                            NameRoomDayTime[1] = "308";
                            break;

                        case R.id.radioButton5: //case RadioButtonClassroom2.getId():
                            NameRoomDayTime[1] = "309";
                            break;

                        case R.id.radioButton6: //case RadioButtonClassroom3.getId():
                            NameRoomDayTime[1] = "310";
                            break;

                        case R.id.radioButton7: //case RadioButtonClassroom4.getId():
                            NameRoomDayTime[1] = "311";
                            break;
                    }
                }
            };

    private RadioGroup.OnCheckedChangeListener radioGroupDayOnCheckedChange =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch (checkedId)
                    {
                        case R.id.radioButton8: //case RadioButtonClassroom1.getId():
                            NameRoomDayTime[2] = "星期一";
                            break;

                        case R.id.radioButton9: //case RadioButtonClassroom2.getId():
                            NameRoomDayTime[2] = "星期二";
                            break;

                        case R.id.radioButton10: //case RadioButtonClassroom3.getId():
                            NameRoomDayTime[2] = "星期三";
                            break;

                        case R.id.radioButton11: //case RadioButtonClassroom4.getId():
                            NameRoomDayTime[2] = "星期四";
                            break;

                        case R.id.radioButton12: //case RadioButtonClassroom4.getId():
                            NameRoomDayTime[2] = "星期五";
                            break;
                    }
                }
            };

    private RadioGroup.OnCheckedChangeListener radioGroupTimeOnCheckedChange =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch (checkedId)
                    {
                        case R.id.radioButton13: //case RadioButtonClassroom1.getId():
                            NameRoomDayTime[3] = "09:10~12:00";
                            break;

                        case R.id.radioButton14: //case RadioButtonClassroom2.getId():
                            NameRoomDayTime[3] = "10:10~12:00";
                            break;

                        case R.id.radioButton15: //case RadioButtonClassroom3.getId():
                            NameRoomDayTime[3] = "13:30~16:20";
                            break;

                        case R.id.radioButton16: //case RadioButtonClassroom4.getId():
                            NameRoomDayTime[3] = "15:30~17:20";
                            break;
                    }
                }
            };

    private CompoundButton.OnCheckedChangeListener checkBoxPerson =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態
                    if(isChecked)//等於 buttonView.isChecked()
                    {
                        studentTable.add(buttonView.getText());
                    }
                    else
                    {
                        studentTable.remove(buttonView.getText());
                    }
                }
            };


    private Button.OnClickListener okBtnEvent = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText className_text = (EditText) findViewById(R.id.className);
            String className = className_text.getText().toString();
            NameRoomDayTime[0] = className;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 將資料寫入資料庫
                    MysqlCon con = new MysqlCon();


                    // 檢查className是否存在
                    ArrayList<String> temp = new ArrayList<String>();
                    temp = con.getData_className();
                    boolean existFlag = false;
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).equals(className)){
                            existFlag = true;
                        }
                    }

                    if (existFlag){
                        Looper.prepare();
                        Toast.makeText(classProduce.this,"簽到表創建失敗:課程名稱重複", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else if(NameRoomDayTime[1] == null || NameRoomDayTime[2] == null || NameRoomDayTime[3] == null){
                        Looper.prepare();
                        Toast.makeText(classProduce.this,"簽到表創建失敗:選項遺漏", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else{
                        con.insertClassData(NameRoomDayTime[0], NameRoomDayTime[1], NameRoomDayTime[2], NameRoomDayTime[3], studentTable.size(),0);
                        con.createAttendanceTable(NameRoomDayTime[0]);
                        con.insertAttendanceTableData(NameRoomDayTime[0], "410777000", "郭教授", "099999999");
                        for (int i = 0; i < studentTable.size(); i++) {
                            String studentStr = (String)studentTable.get(i);
                            String[] splitted = studentStr.split(",");
                            con.insertAttendanceTableData(NameRoomDayTime[0], splitted[0], splitted[1], splitted[2]);
                        }
                        Looper.prepare();
                        Toast.makeText(classProduce.this,"簽到表創建成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }
            }).start();

        }
    };
}

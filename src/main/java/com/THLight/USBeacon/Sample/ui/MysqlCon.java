package com.THLight.USBeacon.Sample.ui;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MysqlCon {
    public MysqlCon() {
    }

    // 資料庫定義
    String mysql_ip = "db4free.net";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "siriusproject";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "sirius410777010";
    String db_password = "ASDzxc410";

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");
            return;
        }

        // 連接資料庫
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
        }
    }

    //--------------------------------------------------------------------------------------
    //沐光的code
    public boolean checkIfExistAccount(String id, String password) {    //回傳帳密是否存在
        boolean result = false;
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,password FROM `userdata` WHERE account = ";
            sql += id;
            sql += " AND password = '";
            sql += password;
            sql += "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                result = true;
                return result;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return result;
    }

    public boolean checkIfExistId(String id) {    //回傳此學號是否存在
        boolean result = false;
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account FROM `userdata` WHERE account = '";
            sql += id;
            sql += "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                result = true;
                return result;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return result;
    }

    public String getUserName(String id) {  //回傳使用者名稱
        String result = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT user_name FROM `userdata` WHERE account = '" + id + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                result = rs.getString("user_name");;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return result;
    }

    public void setFlag(String className , String user) {
        if(getAccount(user).equals("410777000")) {
            try {
                Connection con = DriverManager.getConnection(url, db_user, db_password);
                //設定所有課程flag=0
                String sql_set_flag_0 = "UPDATE `class` SET flag = '0'";
                Statement st_0 = con.createStatement();
                st_0.executeUpdate(sql_set_flag_0);
                //設定選擇課程flag=1
                String sql_set_flag_1 = "UPDATE `class` SET flag = '1' WHERE classname = '" + className + "'";
                Statement st_1 = con.createStatement();
                st_1.executeUpdate(sql_set_flag_1);

                String sql_set_flag_week_teacher = "UPDATE `" + getClassName() + "` SET " + getWeek_2() + " = '1' WHERE "
                        + getWeek_2() + " IS NULL AND student_name = '郭教授'";
                Statement st_week = con.createStatement();
                st_week.executeUpdate(sql_set_flag_week_teacher);

                st_0.close();
                st_1.close();
                st_week.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("DB", "寫入資料失敗");
                Log.e("DB", e.toString());
            }
        }
    }

    public void setFlag0(String user) {
        if(getAccount(user).equals("410777000")) {
            try {
                Connection con = DriverManager.getConnection(url, db_user, db_password);
                //設定所有課程flag=0
                String sql_set_flag_0 = "UPDATE `class` SET flag = '0'";
                Statement st_0 = con.createStatement();
                st_0.executeUpdate(sql_set_flag_0);
                st_0.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("DB", "寫入資料失敗");
                Log.e("DB", e.toString());
            }
        }
    }

    public String getWeek_1() {   //回傳老師本週flag=1且下週flag=NULL的週次
        String week = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            for (int i = 1; i <= 19; i++) {
                String week1 = "week" + String.valueOf(i);
                String week2 = "week" + String.valueOf(i + 1);
                String sql = "SELECT student_name," + week1 + "," + week2 + " FROM `" + getClassName() + "` WHERE " + week1 +
                        " = '1' AND " + week2 + " IS NULL AND student_name = '郭教授'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    //week = rs.getString(week1);
                    week = week1;
                    break;
                }
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return week;
    }

    public String getClassName() {  //回傳flag=1的課程
        String getClass = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT className,flag FROM `class` WHERE flag = '1'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String className = "className";
            if(rs.next()) {
                getClass = rs.getString(className);;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return getClass;
    }

    public String getWeek_2() {
        String week_num = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            for (int i = 1; i <= 19; i++) {
                String week1 = "week" + String.valueOf(i);
                //String week2 = "week" + String.valueOf(i + 1);
                String sql = "SELECT student_name," + week1 + " FROM `" + getClassName() + "` WHERE " + week1
                        + " IS NULL AND student_name = '郭教授'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    //week_num = rs.getString(week1);
                    week_num = week1;
                    break;
                }
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return week_num;
    }

    public void roll_call(String userName) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "UPDATE `" + getClassName() + "` SET " + getWeek_1() + " = '1' WHERE "
                    + getWeek_1() + " IS NULL AND student_name = '" + userName + "'";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
    }

    public String getAccount(String user) {
        String account = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,user_name FROM `userdata` WHERE user_name = '" + user + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                account = rs.getString("account");;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return account;
    }

    public String getClassroom() {
        String classroom = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT classroom FROM `class` WHERE flag = '1'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                classroom = rs.getString("classroom");;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
        return classroom;
    }


    //---------------------------------------------------------------------------------------------
    //以下為家誠的code
    public ArrayList<String> getClass_Flag() {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT className,flag FROM class WHERE flag = 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);


            while (rs.next())
            {
                String className = rs.getString("className");
                String flag = rs.getString("flag");
                temp.add(className + "," + flag);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public ArrayList<String> getClassAllData_AllStudent(String inquireClass) {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,student_name,phone_number,week1,week2,week3,week4,week5,week6,week7,week8,week9,week10," +
                    "week11,week12,week13,week14,week15,week16,week17,week18,week19,week20 FROM " + inquireClass;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);


            while (rs.next())
            {
                String account = rs.getString("account");
                String student_name = rs.getString("student_name");
                String phone_number = rs.getString("phone_number");
                String week1 = rs.getString("week1");
                String week2 = rs.getString("week2");
                String week3 = rs.getString("week3");
                String week4 = rs.getString("week4");
                String week5 = rs.getString("week5");
                String week6 = rs.getString("week6");
                String week7 = rs.getString("week7");
                String week8 = rs.getString("week8");
                String week9 = rs.getString("week9");
                String week10 = rs.getString("week10");
                String week11 = rs.getString("week11");
                String week12 = rs.getString("week12");
                String week13 = rs.getString("week13");
                String week14 = rs.getString("week14");
                String week15 = rs.getString("week15");
                String week16 = rs.getString("week16");
                String week17 = rs.getString("week17");
                String week18 = rs.getString("week18");
                String week19 = rs.getString("week19");
                String week20 = rs.getString("week20");
                temp.add(account + "," + student_name + "," + phone_number + "," + week1 + "," + week2 + "," + week3 + "," + week4 + "," + week5
                        + "," + week6 + "," + week7 + "," + week8 + "," + week9 + "," + week10 + "," + week11 + "," + week12
                        + "," + week13 + "," + week14 + "," + week15 + "," + week16 + "," + week17 + "," + week18 + "," + week19
                        + "," + week20);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public ArrayList<String> getData_class() {
        ArrayList<String> temp = new ArrayList<String>();
        int count = 0;

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT className,classroom,day,time,quantity FROM class";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String className = rs.getString("className");
                String classroom = rs.getString("classroom");
                String day = rs.getString("day");
                String time = rs.getString("time");
                String quantity = rs.getString("quantity");
                temp.add(className + "," + classroom + "," + day + "," + time + "," + quantity);
                count++;
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public ArrayList<String> getClass_Student(String inquireClass, String inquireStudent) {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,student_name,week1,week2,week3,week4,week5,week6,week7,week8,week9,week10," +
                    "week11,week12,week13,week14,week15,week16,week17,week18,week19,week20 FROM " + inquireClass +
                    " WHERE student_name = '" + inquireStudent + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
//            SELECT account, student_name, week1, week2 FROM `Java` WHERE student_name = "David";

            while (rs.next())
            {
                String account = rs.getString("account");
                String student_name = rs.getString("student_name");
                String week1 = rs.getString("week1");
                String week2 = rs.getString("week2");
                String week3 = rs.getString("week3");
                String week4 = rs.getString("week4");
                String week5 = rs.getString("week5");
                String week6 = rs.getString("week6");
                String week7 = rs.getString("week7");
                String week8 = rs.getString("week8");
                String week9 = rs.getString("week9");
                String week10 = rs.getString("week10");
                String week11 = rs.getString("week11");
                String week12 = rs.getString("week12");
                String week13 = rs.getString("week13");
                String week14 = rs.getString("week14");
                String week15 = rs.getString("week15");
                String week16 = rs.getString("week16");
                String week17 = rs.getString("week17");
                String week18 = rs.getString("week18");
                String week19 = rs.getString("week19");
                String week20 = rs.getString("week20");
                temp.add(account + "," + student_name + "," + week1 + "," + week2 + "," + week3 + "," + week4 + "," + week5
                        + "," + week6 + "," + week7 + "," + week8 + "," + week9 + "," + week10 + "," + week11 + "," + week12
                        + "," + week13 + "," + week14 + "," + week15 + "," + week16 + "," + week17 + "," + week18 + "," + week19
                        + "," + week20);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public ArrayList<String> getData_accountUsernamePhonenumber() {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,user_name,phone_number FROM userdata";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String account = rs.getString("account");
                String user_name = rs.getString("user_name");
                String phone_number = rs.getString("phone_number");
                temp.add(account + ", " + user_name + ", " + phone_number);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    //撰寫方法時，需要注意insert的欄位是否都有填滿
    public void insertData(String account, String password, String user_name, String phone_number) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "INSERT INTO `userdata` (`account`,`password`,`user_name`,`phone_number`) VALUES (" + account + ",'" + password + "','" + user_name + "'," + phone_number + ")";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB", "寫入資料完成：" + account);
            Log.v("DB", "寫入資料完成：" + password);
            Log.v("DB", "寫入資料完成：" + user_name);
            Log.v("DB", "寫入資料完成：" + phone_number);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
    }

    public void insertClassData(String className, String classroom, String day, String time,int quantity, int flag) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "INSERT INTO `class` (`className`,`classroom`,`day`,`time`,`quantity`,`flag`) VALUES (" + "'" +className + "','" + classroom + "','" + day + "','" + time + "'," + quantity +  "," + flag +")";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB", "寫入資料完成：" + className);
            Log.v("DB", "寫入資料完成：" + classroom);
            Log.v("DB", "寫入資料完成：" + day);
            Log.v("DB", "寫入資料完成：" + time);
            Log.v("DB", "寫入資料完成：" + quantity);
            Log.v("DB", "寫入資料完成：" + flag);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
    }

    public void createAttendanceTable(String className) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "CREATE TABLE " + className +
                    " (account TEXT(20), " +
                    "student_name TEXT(20), " +
                    "phone_number TEXT(10), " +
                    "week1 INTEGER, " +
                    "week2 INTEGER, " +
                    "week3 INTEGER, " +
                    "week4 INTEGER, " +
                    "week5 INTEGER, " +
                    "week6 INTEGER, " +
                    "week7 INTEGER, " +
                    "week8 INTEGER, " +
                    "week9 INTEGER, " +
                    "week10 INTEGER, " +
                    "week11 INTEGER, " +
                    "week12 INTEGER, " +
                    "week13 INTEGER, " +
                    "week14 INTEGER, " +
                    "week15 INTEGER, " +
                    "week16 INTEGER, " +
                    "week17 INTEGER, " +
                    "week18 INTEGER, " +
                    "week19 INTEGER, " +
                    "week20 INTEGER)";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB", "創建簽到表完成：" + className);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "創建簽到表失敗");
            Log.e("DB", e.toString());
        }
    }

    public void insertAttendanceTableData(String className, String account, String student_name, String phone_number) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "INSERT INTO `" + className + "` (`account`,`student_name`,`phone_number`) VALUES (" + "'" + account + "','" + student_name + "','" + phone_number + "')";
//                    + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 +")";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB", "寫入資料完成：" + className);
            Log.v("DB", "寫入資料完成：" + account);
            Log.v("DB", "寫入資料完成：" + student_name);
            Log.v("DB", "寫入資料完成：" + phone_number);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
    }

    public ArrayList<String> getClass_AllStudent(String inquireClass) {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT account,student_name,week1,week2,week3,week4,week5,week6,week7,week8,week9,week10," +
                    "week11,week12,week13,week14,week15,week16,week17,week18,week19,week20 FROM " + inquireClass;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String account = rs.getString("account");
                String student_name = rs.getString("student_name");
                String week1 = rs.getString("week1");
                String week2 = rs.getString("week2");
                String week3 = rs.getString("week3");
                String week4 = rs.getString("week4");
                String week5 = rs.getString("week5");
                String week6 = rs.getString("week6");
                String week7 = rs.getString("week7");
                String week8 = rs.getString("week8");
                String week9 = rs.getString("week9");
                String week10 = rs.getString("week10");
                String week11 = rs.getString("week11");
                String week12 = rs.getString("week12");
                String week13 = rs.getString("week13");
                String week14 = rs.getString("week14");
                String week15 = rs.getString("week15");
                String week16 = rs.getString("week16");
                String week17 = rs.getString("week17");
                String week18 = rs.getString("week18");
                String week19 = rs.getString("week19");
                String week20 = rs.getString("week20");
                temp.add(account + "," + student_name + "," + week1 + "," + week2 + "," + week3 + "," + week4 + "," + week5
                        + "," + week6 + "," + week7 + "," + week8 + "," + week9 + "," + week10 + "," + week11 + "," + week12
                        + "," + week13 + "," + week14 + "," + week15 + "," + week16 + "," + week17 + "," + week18 + "," + week19
                        + "," + week20);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public ArrayList<String> getData_className() {
        ArrayList<String> temp = new ArrayList<String>();

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT className FROM class";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String className = rs.getString("className");
                temp.add(className);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

}
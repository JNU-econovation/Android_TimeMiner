package com.fifo.bellroot.swipe_listview;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public Context context;
    public static SQLiteDatabase db;
    ArrayList<String> namelists;
    ArrayList<String> dayLists;
    String name;
    String day;
    NameList nameList;
    DayTime dayTime;
    String day1, day2, day3, day4, day5, day6, day7;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            StringBuffer sb = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();
            StringBuffer sb3 = new StringBuffer();


            // TEST_TABLE 생성
            sb.append(" CREATE TABLE TEST_TABLE( ");
            sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(" NAME TEXT, ");
            sb.append(" DEADLINE_DATE TEXT, ");
            sb.append(" DEADLINE_TIME TEXT, ");
            sb.append(" GOAL_TIME TEXT, ");
            sb.append(" TIMER VALUE, ");
            sb.append(" LAST_TIME TEXT ); ");

            // DAYTABLE 생성
            sb2.append(" CREATE TABLE DAYTABLE( ");
            sb2.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb2.append(" NAME TEXT, ");
            sb2.append(" DAY1 TEXT, ");
            sb2.append(" DAY2 TEXT, ");
            sb2.append(" DAY3 TEXT, ");
            sb2.append(" DAY4 TEXT, ");
            sb2.append(" DAY5 TEXT, ");
            sb2.append(" DAY6 TEXT, ");
            sb2.append(" DAY7 TEXT ); ");


            // NAMELIST 생성
            sb3.append(" CREATE TABLE NAMELIST( ");
            sb3.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb3.append(" NAME TEXT, ");
            sb3.append(" TODAY TEXT ); ");

            db.execSQL(sb.toString());
            db.execSQL(sb3.toString());
            db.execSQL(sb2.toString());

        }

       // Toast.makeText(context, "db 생성", Toast.LENGTH_SHORT).show();

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Toast.makeText(context, "Version 올라감", Toast.LENGTH_SHORT).show();
    }

    public void writeDB(){
        SQLiteDatabase db = getWritableDatabase();
    }

    public void readDB() {
        SQLiteDatabase db = getReadableDatabase();
    }


    // TEST_TABLE에 데이터 입력
    public void addTime(TimeChecker timeChecker){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" NAME, DEADLINE_DATE, DEADLINE_TIME, GOAL_TIME, TIMER, LAST_TIME) ");
        sb.append(" VALUES (#NAME#, #DEADLINE_DATE#, #DEADLINE_TIME#, #GOAL_TIME#, #TIMER#, #LAST_TIME# )");

        String query = sb.toString();
        query = query.replace("#NAME#", "'"+timeChecker.getName()+"'");
        query = query.replace("#DEADLINE_DATE#", "'"+timeChecker.getDeadline_date()+"'");
        query = query.replace("#DEADLINE_TIME#", "'"+timeChecker.getDeadline_time()+"'");
        query = query.replace("#GOAL_TIME#", "'"+timeChecker.getGoal_time()+"'");
        query = query.replace("#TIMER#", "'"+timeChecker.getTimer()+"'");
        query = query.replace("#LAST_TIME#", "'"+timeChecker.getLast_time()+"'");

        db.execSQL(query);
    }

    // NAMELIST에 데이터 입력
    public  void NameList(NameList nameList){
        db = getWritableDatabase();

        StringBuffer sb3 = new StringBuffer();
        sb3.append(" INSERT INTO NAMELIST ( ");
        sb3.append(" NAME, TODAY ) ");
        sb3.append(" VALUES (#NAME#, #TODAY# )");

        String query3=sb3.toString();
        query3 = query3.replace("#NAME#", "'" + nameList.getName() + "'");
        query3 = query3.replace("#TODAY#", "'" + nameList.getToday() + "'");
        db.execSQL(query3);
    }

    // NAMELIST에서 데이터 불러오기
    public List getNameList() {
        String query = " SELECT * FROM NAMELIST ";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        namelists = new ArrayList<String>(); // name을 담는 ArrayList

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            nameList = new NameList();
            nameList.setId(cursor.getInt(0));
            nameList.setName(cursor.getString(1));
            nameList.setToday(cursor.getString(2));

            name = nameList.getName();

            namelists.add(name);
        }

        return namelists;
    }


    public List getNameListToday(String name) {
        String query = " SELECT * FROM NAMELIST WHERE NAME = '"+name+"'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        namelists = new ArrayList<String>(); // name을 담는 ArrayList

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            nameList = new NameList();
            nameList.setId(cursor.getInt(0));
            nameList.setName(cursor.getString(1));
            nameList.setToday(cursor.getString(2));

            day = nameList.getToday();

            namelists.add(day);
        }

        return namelists;
    }


    // DAYTABLE에 데이터 입력
    public void DayTime(DayTime dayTime) {
        db = getWritableDatabase();

        StringBuffer sb2 = new StringBuffer();
        sb2.append(" INSERT INTO DAYTABLE ( ");
        sb2.append(" NAME, DAY1, DAY2, DAY3, DAY4, DAY5, DAY6, DAY7) ");
        sb2.append(" VALUES (#NAME#, #DAY1#, #DAY2#, #DAY3#, #DAY4#, #DAY5#, #DAY6#, #DAY7# )");

        String query2 = sb2.toString();
        query2 = query2.replace("#NAME#", "'" + dayTime.getName() + "'");
        query2 = query2.replace("#DAY1#", "'" + dayTime.getDay1() + "'");
        query2 = query2.replace("#DAY2#", "'" + dayTime.getDay2() + "'");
        query2 = query2.replace("#DAY3#", "'" + dayTime.getDay3() + "'");
        query2 = query2.replace("#DAY4#", "'" + dayTime.getDay4() + "'");
        query2 = query2.replace("#DAY5#", "'" + dayTime.getDay5() + "'");
        query2 = query2.replace("#DAY6#", "'" + dayTime.getDay6() + "'");
        query2 = query2.replace("#DAY7#", "'" + dayTime.getDay7() + "'");
        db.execSQL(query2);
    }

    // DAYTABLE에서 데이터 불러오기
    public List getDayTime(String name) {
        String query = " SELECT * FROM DAYTABLE WHERE NAME = '"+name+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        dayLists = new ArrayList<String>(); // day1~day7 값을 담는 ArrayList

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            dayTime = new DayTime();
            dayTime.setId(cursor.getInt(0));
            dayTime.setName(cursor.getString(1));
            dayTime.setDay1(cursor.getString(2));
            dayTime.setDay2(cursor.getString(3));
            dayTime.setDay3(cursor.getString(4));
            dayTime.setDay4(cursor.getString(5));
            dayTime.setDay5(cursor.getString(6));
            dayTime.setDay6(cursor.getString(7));
            dayTime.setDay7(cursor.getString(8));

//            int hr = Integer.parseInt(dayTime.getDay1().substring(0,dayTime.getDay2().indexOf(":")));
//            int min = Integer.parseInt(dayTime.getDay1().substring(dayTime.getDay2().indexOf(":")+1);
//            int day = hr*60 + min;

            day1 = dayTime.getDay1();
            day2 = dayTime.getDay2();
            day3 = dayTime.getDay3();
            day4 = dayTime.getDay4();
            day5 = dayTime.getDay5();
            day6 = dayTime.getDay6();
            day7 = dayTime.getDay7();

            dayLists.add(day1);
            dayLists.add(day2);
            dayLists.add(day3);
            dayLists.add(day4);
            dayLists.add(day5);
            dayLists.add(day6);
            dayLists.add(day7);

        }
        return dayLists;
    }


    public void close(){
        db.close();

    }

    // DB 정보수정
    public void update(String name, String last_time){

        // DB 쓰기 모드 전환
        SQLiteDatabase db = getWritableDatabase();

        String query = " UPDATE TEST_TABLE SET LAST_TIME = '" + last_time +"'"+ "WHERE NAME = '"+ name + "'";
        db.execSQL(query);
        db.close();

    }

    // DAYTABLE db 정보수정
    public void updateDayTime(String name, String last_time, String days){

        // DB 쓰기 모드 전환
        SQLiteDatabase db = getWritableDatabase();

        String query = " UPDATE DAYTABLE SET "+ days +" = '" + last_time +"'"+ "WHERE NAME = '"+ name + "'";
        db.execSQL(query);
        db.close();

    }


    // DB 데이터 삭제
    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TEST_TABLE WHERE NAME = '" + name + "';");
        db.close();


    }

    // NAMELIST DB 데이터 삭제
    public void deleteNameList(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM NAMELIST WHERE NAME = '" + name + "';");
        db.close();
    }
}
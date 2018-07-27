package com.fifo.bellroot.swipe_listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ExpandAdapter extends BaseExpandableListAdapter {


    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<myGroup> DataList;
    private LayoutInflater myinf = null;
    int counter;
    MainActivity main ;
    String sHr;
    Integer progresshr;
    String sMin;
    Integer progessmin;
    String name;
    int Hr;
    int Min;
    int[] TotalTime={0, 0, 0, 0,0 ,0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0};;
    CountDownTimer countDownTimer = null;
    ImageView minor;
    DBHelper dbHelper;
    int count[]= {0, 0, 0, 0,0 ,0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Button[] button=new Button[20];
    TimeChecker timeChecker;
    Intent intent;Vibrator vibrator;




    public ExpandAdapter(Context context, int groupLay, int chlidLay, ArrayList<myGroup> DataList){
        this.DataList = DataList;
        this.groupLayout = groupLay;
        this.chlidLayout = chlidLay;
        this.context = context;
        this.myinf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub


        if(convertView == null){
            convertView = myinf.inflate(this.groupLayout, parent, false);

            counter=0;

            button[groupPosition] =(Button)convertView.findViewById(R.id.TimeButton);
            button[groupPosition].setText(DataList.get(groupPosition).Time);
            sHr=button[groupPosition].getText().toString().substring(0,
                    button[groupPosition].getText().toString().indexOf(":"));

            sMin=button[groupPosition].getText().toString()
                    .substring(button[groupPosition].getText().toString().indexOf(":")+1);

            Hr=Integer.parseInt(sHr);
            Min=Integer.parseInt(sMin);
            TotalTime[groupPosition]= Hr*60+Min;

        }



        // gif 이미지
        minor = convertView.findViewById(R.id.gif_image);
        final GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(minor);

       // 카운트다운
        button[groupPosition].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[groupPosition]++;

                if (count[groupPosition] % 2 == 1) {
                    Glide.with(context).load(R.drawable.piechart).crossFade(0).into(gifImage);
                    Toast.makeText(context, "시간을 채굴합니다", Toast.LENGTH_SHORT).show();
                    countDownTimer = new CountDownTimer(TotalTime[groupPosition]*60000,
                            60000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            TotalTime[groupPosition]--;
                            button[groupPosition].setText(TotalTime[groupPosition] / 60 + ":"
                                    + TotalTime[groupPosition] % 60);
                            notifyDataSetChanged(); //child 갱신
                            if(TotalTime[groupPosition]==0){
                                onFinish();

                            }
                        }

                        @Override
                        public void onFinish() {
                            //목표 완료시 이벤트
                            Glide.with(context).load(R.drawable.piechart_stop).crossFade(0).into(gifImage);

                            Intent complete = new Intent (context, Complete.class);
                            context.startActivity(complete);
                            dbHelper= new DBHelper(context, "DB",
                                    null, 1);
                            dbHelper.delete(DataList.get(groupPosition).groupName);

                            DataList.remove(groupPosition);
                            notifyDataSetChanged();
                            count[groupPosition]=0;


                        }
                    }.start();
                }
                else{
                    countDownTimer.cancel();
                    Toast.makeText(context, "정지", Toast.LENGTH_SHORT).show();
                    dbHelper= new DBHelper(context, "DB", null, 1);
                    dbHelper.update(DataList.get(groupPosition).groupName,
                            TotalTime[groupPosition] / 60 + ":" + TotalTime[groupPosition] % 60);

                    // DAYTABLE에 날짜별 데이터 담기
                    try {
                        // 현재 날짜
                        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-mm-dd", Locale.KOREA );
                        Date currentDay = new Date( );

                        // 선택한 목표의 시작일 추출
                        List today = dbHelper.getNameListToday(DataList.get(groupPosition).groupName);
                        String startdate = today.get(0).toString();
                        Date startDay = formatter.parse(startdate);

                        long term = currentDay.getTime() - startDay.getTime();
                        long day = term / (24*60*60*1000);

                        int days = (int) Math.abs(day) - 181;

                        // 목표시간 불러옴
                        SQLiteDatabase db= dbHelper.getReadableDatabase();

                        String query = " SELECT * FROM TEST_TABLE WHERE NAME = '"+DataList.get(groupPosition).groupName+"'";

                        Cursor cursor = db.rawQuery(query, null);

                        timeChecker = new TimeChecker();
                        String goalTime ="";
                        while(cursor.moveToNext()){

                            timeChecker.setid(cursor.getInt(0));
                            timeChecker.setName(cursor.getString(1));
                            timeChecker.setDeadline_date(cursor.getString(2));
                            timeChecker.setDeadline_time(cursor.getString(3));
                            timeChecker.setGoal_time(cursor.getString(4));
                            timeChecker.setTimer(cursor.getInt(5));
                            timeChecker.setLast_time(cursor.getString(6));

                            goalTime = timeChecker.getGoal_time();
                        }
                        // String타입의 목표시간을 분단위 int타입으로 변환
                        int hr = Integer.parseInt(goalTime.substring(0,goalTime.indexOf(":")));
                        int min = Integer.parseInt(goalTime.substring(goalTime.indexOf(":")+1));
                        int goaltime = hr*60 + min;
                        // 남은 시간
                        int lasttime = TotalTime[groupPosition];

                        //목표시간 - 남은시간
                        int todaytime = goaltime - lasttime;

                        switch (days){
                            case 0:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY1");
                                break;
                            case 1:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60 % 60, "DAY2");
                                break;
                            case 2:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY3");
                                break;
                            case 3:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY4");
                                break;
                            case 4:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY5");
                                break;
                            case 5:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY6");
                                break;
                            case 6:
                                dbHelper.updateDayTime(DataList.get(groupPosition).groupName,
                                        todaytime / 60 + ":" + todaytime % 60, "DAY7");
                                break;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Glide.with(context).load(R.drawable.piechart_stop).crossFade(0).into(gifImage);

                }
            }
        });


        // 목표 리스트 이름 호출
        TextView groupName = (TextView)convertView.findViewById(R.id.groupName);
        groupName.setText(DataList.get(groupPosition).groupName);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = myinf.inflate(this.chlidLayout, parent, false);
        }
        ProgressBar progressBar =(ProgressBar)convertView.findViewById(R.id.goalProgressBar);

        // 목표 시간 호출
        DBHelper dbHelpers = new DBHelper(context, "DB", null, 1);
        SQLiteDatabase db = dbHelpers.getReadableDatabase();

        String query = " SELECT * FROM TEST_TABLE WHERE NAME = '"+DataList.get(groupPosition).groupName+"'";
        Cursor cursor = db.rawQuery(query, null);


        TimeChecker timeChecker = new TimeChecker();
        while (cursor.moveToNext()){
            timeChecker.setid(cursor.getInt(0));
            timeChecker.setName(cursor.getString(1));
            timeChecker.setDeadline_date(cursor.getString(2));
            timeChecker.setDeadline_time(cursor.getString(3));
            timeChecker.setGoal_time(cursor.getString(4));
            timeChecker.setTimer(cursor.getInt(5));
            timeChecker.setLast_time(cursor.getString(6));
        }

        String goal = timeChecker.getGoal_time();
        db.close();
        //프로그레스바
        progresshr = Integer.valueOf(goal.substring(0, goal.indexOf(":"))) ;
        progessmin = Integer.valueOf(goal.substring(goal.indexOf(":")+1));
        progressBar.setProgress((((progresshr*60+progessmin)-TotalTime[groupPosition])*100)/(progresshr*60+progessmin));

        TextView goalTime =(TextView)convertView.findViewById(R.id.goalCumTime);
        goalTime.setText("목표 시간 : "+ goal);
        TextView goalLastTime =(TextView)convertView.findViewById(R.id.goalLastTime);
        goalLastTime.setText("남은 시간 : " + TotalTime[groupPosition] / 60 + ":" + TotalTime[groupPosition] % 60);

        TextView percent =(TextView)convertView.findViewById(R.id.percent) ;
        percent.setText("달성률 : "+(((progresshr*60+progessmin)-TotalTime[groupPosition])*100)/(progresshr*60+progessmin)+"%");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition).child.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition).child.size();
    }

    @Override
    public myGroup getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return DataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }
    public void add(myGroup myGroup){

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }




}

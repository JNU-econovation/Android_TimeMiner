package com.fifo.bellroot.swipe_listview;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    EditText et;
    String name;
    NumberPicker HR;
    NumberPicker MIN;
    int hr, min;
    TimePicker timePicker;
    DatePicker datePicker;
    int timepickerHr, timepickerMin;
    DBHelper dbHelper;
    TimeChecker timeChecker;
    int year, month, day;
    NameList nameList;
    DayTime dayTime;
    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 제목
        et = (EditText)findViewById(R.id.editText) ;


        // 목표 시간
        HR= (NumberPicker)findViewById(R.id.hr);
        HR.setMaxValue(50);
        HR.setMinValue(0);
        MIN= (NumberPicker)findViewById(R.id.min);
        MIN.setMaxValue(59);
        MIN.setMinValue(0);


        // datePicker
        datePicker = (DatePicker) findViewById(R.id.datepick);

         year = datePicker.getYear();
         month = datePicker.getMonth()+1;
         day = datePicker.getDayOfMonth();


        // timePicker
        timePicker =  (TimePicker)findViewById(R.id.timepick);
        timepickerHr=timePicker.getHour();
        timepickerMin= timePicker.getMinute();

        Button button2 =(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //목표 입력 목표 시간 입력 확인
                if (et.getText().toString().getBytes().length<=0 ) {
                    Toast.makeText(getApplicationContext(), "목표를 입력하세요", Toast.LENGTH_LONG).show();
                } else if (HR.getValue() == 0 && MIN.getValue() == 0) {
                    Toast.makeText(getApplicationContext(), "목표 시간을 입력하세요", Toast.LENGTH_LONG).show();
                } else {
                    Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(MenuActivity.this,
                            0, new Intent(getApplicationContext(), MainActivity.class)
                            , PendingIntent.FLAG_UPDATE_CURRENT);
                    name = et.getText().toString();
                    hr = HR.getValue();
                    min = MIN.getValue();

                    //TABLE 생성
                    dbHelper = new DBHelper(getApplicationContext(), "DB",
                            null, 1);
                    dbHelper.readDB();


                    //DB에 저장
                    timeChecker = new TimeChecker();
                    timeChecker.setName(name);
                    timeChecker.setDeadline_date(year + "/" + month + "/" + day);
                    timeChecker.setDeadline_time(timepickerHr + ":" + timepickerMin);
                    timeChecker.setGoal_time(hr + ":" + min);
                    timeChecker.setLast_time(timeChecker.getGoal_time());
                    dbHelper.addTime(timeChecker);

                    nameList = new NameList();
                    nameList.setName(name);

                    //시간 저장
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                    String str_date = df.format(new Date());
                    nameList.setToday(str_date);
                    dbHelper.NameList(nameList);

                    dayTime = new DayTime();
                    dayTime.setName(name);
                    dayTime.setDay1("0:0");
                    dayTime.setDay2("0:0");
                    dayTime.setDay3("0:0");
                    dayTime.setDay4("0:0");
                    dayTime.setDay5("0:0");
                    dayTime.setDay6("0:0");
                    dayTime.setDay7("0:0");
                    dbHelper.DayTime(dayTime);

                    String channelId = "channel";
                    String channelName = "Channel Name";


                    //  알람
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //오레오 버전 호환용
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);

                        notificationManager.createNotificationChannel(notificationChannel);

                    }
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder
                            (getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle(name)
                            .setContentText(hr + "시간 " + min + "분")
                            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                            .setLargeIcon(mLargeIconForNoti)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(false)
                            .setContentIntent(mPendingIntent);

                    notificationManager.notify(0, mBuilder.build());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("HOUR", Integer.parseInt(timeChecker.getLast_time().substring(0, timeChecker.getLast_time().indexOf(":"))));
                    returnIntent.putExtra("MIN", Integer.parseInt(timeChecker.getLast_time().substring(timeChecker.getLast_time().indexOf(":") + 1)));
                    returnIntent.putExtra("result", timeChecker.getName().toString());

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }



}

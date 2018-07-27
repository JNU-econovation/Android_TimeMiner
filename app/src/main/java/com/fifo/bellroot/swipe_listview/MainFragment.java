package com.fifo.bellroot.swipe_listview;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainFragment extends Fragment {
    DBHelper dbHelper;
    public static SQLiteDatabase db;
    ExpandAdapter adapter;
    ExpandableListView listView;
    ArrayList<myGroup> DataList;
    String a;
    myGroup temp;
    int width;
    SpinnerAdapter sadapter;
    int hr, min;long term,day;
    String time; int days;
    String name;
    ArrayList<String> spinnerList;
    TimeChecker timeChecker;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataList = new ArrayList<myGroup>();
        spinnerList = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        dbHelper = new DBHelper(getContext(), "DB",
                null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, NAME, DEADLINE_DATE, DEADLINE_TIME, GOAL_TIME, TIMER, LAST_TIME FROM TEST_TABLE ");

        Cursor cursor = db.rawQuery(sb.toString(), null);

        timeChecker = new TimeChecker();
        while (cursor.moveToNext()) {

            timeChecker.setid(cursor.getInt(0));
            timeChecker.setName(cursor.getString(1));
            timeChecker.setDeadline_date(cursor.getString(2));
            timeChecker.setDeadline_time(cursor.getString(3));
            timeChecker.setGoal_time(cursor.getString(4));
            timeChecker.setTimer(cursor.getInt(5));
            timeChecker.setLast_time(cursor.getString(6));

            name = timeChecker.getName();
            time = timeChecker.getLast_time();

            hr = Integer.valueOf(time.substring(0, time.indexOf(":")));
            min = Integer.valueOf(time.substring(time.indexOf(":") + 1));

            temp = new myGroup(name, hr, min);
            temp.child.add("");
            DataList.add(temp);

        }


        listView = (ExpandableListView) view.findViewById(R.id.mylist);

        adapter = new ExpandAdapter(getActivity(), R.layout.item_layout, R.layout.item_detailed_layout, DataList);
        sadapter = new SpinnerAdapter(getActivity(), spinnerList);
        listView.setGroupIndicator(null); // 리스트뷰 화살표 제거
        listView.setClickable(true);
        listView.setAdapter(adapter);


        //길게 눌러서 리스트 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                ad.setTitle("포기할거에요??ㅠㅠ");
                ad.setPositiveButton("포기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.delete(DataList.get(position).groupName);

                        dbHelper = new DBHelper(getContext(), "DB",
                                null, 1);
                        dbHelper.deleteNameList(DataList.get(position).groupName);
                        DataList.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                return false;
            }
        });
        // 현재 날짜
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.KOREA);
        Date currentDay = new Date();
        for (int i = DataList.size()-1; i>=0; i--) {
            // 선택한 목표의 시작일 추출
            List today = dbHelper.getNameListToday(DataList.get(i).groupName);
            String startdate = today.get(0).toString();
            Date startDay = null;
            try {
                startDay = formatter.parse(startdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            term = currentDay.getTime() - startDay.getTime();
            day = term / (24 * 60 * 60 * 1000);

            days = (int) Math.abs(day) - 181;
            if (days > 7) {
Intent fail = new Intent(getActivity(), Fail.class);
startActivity(fail);
                DataList.remove(i);
                adapter.notifyDataSetChanged();
            }
        }
        return view;

    }

    //프레그먼트 refresh
    public void refresh(String result, int hr, int min) {
        myGroup temp = new myGroup(result, hr, min);
        temp.child.add("child2");
        DataList.add(temp);
        adapter.notifyDataSetChanged();
    }

    public void deleterefresh(int a) {

    }

}


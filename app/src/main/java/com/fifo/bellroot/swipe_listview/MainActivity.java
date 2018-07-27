package com.fifo.bellroot.swipe_listview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.view.Display;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import static com.fifo.bellroot.swipe_listview.DBHelper.db;

public class MainActivity extends AppCompatActivity {
    Context context;
    Intent intent;
    ViewPager vp;
    FloatingActionButton fab;
    Button btn_main;
    Button btn_stat;
    String name;
    DBHelper dbHelper;
    ArrayList<Fragment> fragments = new ArrayList<>();
    int hr;
    int min;
    myGroup mg;
    int num = 0;
    Intent helppage;
    String time;
    List<TimeChecker> timeChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // loading 화면
        Intent intents = new Intent(this, LoadingActivity.class);
        startActivity(intents);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        dbHelper = new DBHelper(getApplicationContext(), "DB", null, 1);
        dbHelper.readDB();


        fragments.add(new MainFragment());
        fragments.add(new StatisticsFragment());




        vp = (ViewPager) findViewById(R.id.vp);
        btn_main = (Button) findViewById(R.id.btn_main);
        btn_stat = (Button) findViewById(R.id.btn_stat);

        vp.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);

        btn_main.setOnClickListener(movePageListner);
        btn_main.setTag(0);
        btn_stat.setOnClickListener(movePageListner);
        btn_stat.setTag(1);


        //목표 추가 버튼
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        fab.setTag(num);
        num++;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.help:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "도움말", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), HelpPage.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                name = data.getStringExtra("result");
                hr = data.getIntExtra("HOUR", 1);
                min = data.getIntExtra("MIN", 2);

//refresh
                ((MainFragment) fragments.get(0)).refresh(name, hr, min);
                ((StatisticsFragment) fragments.get(1)).refresh(name);
            }


            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }


    }


    View.OnClickListener movePageListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}


//뷰페이저 어댑터
class PagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}



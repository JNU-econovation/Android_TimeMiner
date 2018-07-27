package com.fifo.bellroot.swipe_listview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.fifo.bellroot.swipe_listview.MainFragment.db;

public class StatisticsFragment extends Fragment {
    LineChart lineChart;
    SpinnerAdapter adapterSpinner;
    Spinner spinner;
    DBHelper dbHelper;
    String name;
    ArrayList<String> spinnerList = new ArrayList<>();
    int day;
    int min;
    int hr;


    public StatisticsFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        dbHelper = new DBHelper(getContext(), "DB",null,1);

        List namelists = dbHelper.getNameList();


        // Spinner


        spinnerList.add("목표 선택");

        for(int i=0; i < namelists.size(); i++) {
            spinnerList.add(namelists.get(i).toString());
        }



        //spinner 객체 생성
        spinner = (Spinner) view.findViewById(R.id.spinner);
        //adapter
        adapterSpinner = new SpinnerAdapter(getContext(), spinnerList);
        //adapter 적용
        spinner.setAdapter(adapterSpinner);


        // 차트 생성
        lineChart = (LineChart) view.findViewById(R.id.chart);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = spinner.getItemAtPosition(position).toString();


                // 차트에 뿌려야할 데이터를 DAYTABLE에서 불러옴
                dbHelper = new DBHelper(getContext(), "DB",null,1);
                List dayLists = dbHelper.getDayTime(name); // DAYTABLE의 db중 NAME = name인 데이터만 불러옴.

                // x index에 대한 value
                ArrayList<Entry> entries = new ArrayList<Entry>();

                for(int i=0; i < dayLists.size(); i++) {
                    hr = Integer.parseInt(dayLists.get(i).toString().substring(0,dayLists.get(i).toString().indexOf(":")));
                    min = Integer.parseInt(dayLists.get(i).toString().substring(dayLists.get(i).toString().indexOf(":")+1));
                    day = hr*60 + min;

                    entries.add(new Entry((float)day, i));
                }

                LineDataSet dataset = new LineDataSet(entries, name);



                // x축
                ArrayList<String> labels = new ArrayList<String>();
                labels.add("day1");
                labels.add("day2");
                labels.add("day3");
                labels.add("day4");
                labels.add("day5");
                labels.add("day6");
                labels.add("day7");

                LineData data = new LineData(labels, dataset);

                dataset.setColor(Color.RED);
                dataset.setLineWidth(3);
                dataset.setCircleColor(Color.RED);
                dataset.setCircleSize(5);


                XAxis xAxis = lineChart.getXAxis();
                xAxis.setDrawGridLines(true);
                xAxis.setDrawAxisLine(true);

                lineChart.setData(data);
                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setDrawGridLines(true);
                rightAxis.setDrawAxisLine(true);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getAxisLeft().setDrawGridLines(false);
                lineChart.getXAxis().setDrawGridLines(false);


                // Grid 색상
                lineChart.setGridBackgroundColor(128);

                // 애니메이션
                lineChart.animateY(1000);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //길게 눌러서 리스트 삭제
//        spinner.setClickable(true);
//        spinner.setLongClickable(true);
//        spinner.performLongClick();
        spinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                ad.setTitle("삭제할까요?");
                ad.setPositiveButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteNameList(spinnerList.get(position));
                        spinnerList.remove(position);
                        adapterSpinner.notifyDataSetChanged();
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




        return view;

    }
    //프레그먼트 spinner 추가
    public void refresh(String a) {
        spinnerList.add(a);
        adapterSpinner.notifyDataSetChanged();
    }
    public void  refresh(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();

    }
}

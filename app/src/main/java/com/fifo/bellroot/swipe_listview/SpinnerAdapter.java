package com.fifo.bellroot.swipe_listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> data;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_header_layout, parent, false);
        }

        if(data!=null){
            String text = data.get(positon);
            ((TextView)convertView.findViewById(R.id.spinnerText)).setText(text);
        }

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.spinner_layout,parent, false);
        }

        String text = data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerText)).setText(text);


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}

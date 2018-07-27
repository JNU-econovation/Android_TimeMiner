package com.fifo.bellroot.swipe_listview;

import java.util.ArrayList;


public class myGroup {
    public ArrayList<String> child;
    public String groupName;
    public int hr;
    public int min;
    public String Time;


    myGroup(String name, int hr, int min) {
        groupName = name;
        this.hr=hr;
        this.min=min;
        Time=hr+":"+min;
        child = new ArrayList<String>();
    }
}
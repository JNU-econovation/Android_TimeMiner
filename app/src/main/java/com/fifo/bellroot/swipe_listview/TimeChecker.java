package com.fifo.bellroot.swipe_listview;

public class TimeChecker {
    private int id;

    private String name;
    private String deadline_date;
    private String deadline_time;
    private String goal_time;
    private int timer;
    private String last_time;

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getDeadline_time() {
        return deadline_time;
    }

    public void setDeadline_time(String deadline_time) {
        this.deadline_time = deadline_time;
    }

    public String getGoal_time() {
        return goal_time;
    }

    public void setGoal_time(String goal_time) {
        this.goal_time = goal_time;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }
}

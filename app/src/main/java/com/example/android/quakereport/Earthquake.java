package com.example.android.quakereport;

/**
 * Created by rdas6313 on 2/4/17.
 */

public class Earthquake {
    private String date,mag,place,time,url;
    public Earthquake(String d,String t,String m,String p,String u){
        date = d;
        mag = m;
        place = p;
        time = t;
        url = u;
    }
    public String getDate(){
        return date;
    }
    public String getMag(){
        return mag;
    }
    public String getTime(){
        return time;
    }
    public String getPlace(){
        return place;
    }
    public String getUrl(){
        return url;
    }
}

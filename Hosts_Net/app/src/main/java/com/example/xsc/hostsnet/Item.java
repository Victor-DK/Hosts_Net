package com.example.xsc.hostsnet;

/**
 * Created by Delcas on 2017/8/12.
 */

public class Item {
    private String city;
    private String ip;
    private String time;
    Item(String city,String ip,String time){
        this.city = city;
        this.ip = ip;
        this.time = time;
    }
    public String getCity() {
        return city;
    }

    public String getIp() {
        return ip;
    }

    public String getTime(){
        return time;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTime(String time){
        this.time = time;
    }
}

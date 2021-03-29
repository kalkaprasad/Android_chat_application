package com.lms.deepakchatapp.main;

public class Statusmodel {

    String imgurl;
    String name;

    public Statusmodel(String imgurl, String name, String day) {
        this.imgurl = imgurl;
        this.name = name;
        this.day = day;
    }

    String day;



    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

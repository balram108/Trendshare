package com.example.trendsetter;

import android.graphics.Bitmap;

public class MainData {
    String Name,WebLink,UID,INfo;
    Bitmap bitmap1,bitmap2;
   double ratingStar;




    public MainData(String name, String webLink, Bitmap bitmap1, Bitmap bitmap2, String uid , String info , double rs) {
        Name = name;
        WebLink = webLink;
        this.bitmap1 = bitmap1;
        this.bitmap2 = bitmap2;
        this.UID = uid;
        this.INfo = info;
        this.ratingStar = rs;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(double ratingStar) {
        this.ratingStar = ratingStar;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWebLink() {
        return WebLink;
    }

    public void setWebLink(String webLink) {
        WebLink = webLink;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public String getINfo() {
        return INfo;
    }

    public void setINfo(String INfo) {
        this.INfo = INfo;
    }
}

package com.example.trendsetter;

import android.graphics.Bitmap;

import com.google.firebase.database.ServerValue;

import java.util.Date;

public class CommentConstructor {
    String addinguserid;
    String comment;
    String gettinguserid;
    String pn;
    String pp;
    Date   timeStamp;

    public CommentConstructor(String addinguserid, String comment, String gettinguserid, String pn, String pp, Date timeStamp) {
        this.addinguserid = addinguserid;
        this.comment = comment;
        this.gettinguserid = gettinguserid;
        this.pn = pn;
        this.pp = pp;
        this.timeStamp = timeStamp;
    }

    public CommentConstructor() {
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getAddinguserid() {
        return addinguserid;
    }

    public void setAddinguserid(String addinguserid) {
        this.addinguserid = addinguserid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGettinguserid() {
        return gettinguserid;
    }

    public void setGettinguserid(String gettinguserid) {
        this.gettinguserid = gettinguserid;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}

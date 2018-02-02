package com.example.swetha_pt1880.blooddonar.database;

import java.io.Serializable;

/**
 * Created by swetha-pt1880 on 22/1/18.
 */

public class User  implements Serializable{
    public  String userId;
    public   String uName;
    public  String  uDOB;
    public  String uGender;
    public int donar;

    public int getDonar() {
        return donar;
    }

    public void setDonar(int donar) {
        this.donar = donar;
    }

    public User(String userId, String uName, String uDOB, String uGender, String uContact, String uPassWord, String uPriviledge, int donar) {
        this.userId = userId;
        this.uName = uName;
        this.uDOB = uDOB;
        this.uGender = uGender;
        this.uContact = uContact;
        this.uPassWord = uPassWord;
        this.uPriviledge = uPriviledge;
        this.donar = donar;
    }

    public  String  uContact;
    public  String uPassWord;
    public  String uPriviledge;

    public User(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuDOB() {
        return uDOB;
    }

    public void setuDOB(String uDOB) {
        this.uDOB = uDOB;
    }

    public String getuGender() {
        return uGender;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public String getuContact() {
        return uContact;
    }

    public void setuContact(String uContact) {
        this.uContact = uContact;
    }

    public String getuPassWord() {
        return uPassWord;
    }

    public void setuPassWord(String uPassWord) {
        this.uPassWord = uPassWord;
    }

    public String getuPriviledge() {
        return uPriviledge;
    }

    public void setuPriviledge(String uPriviledge) {
        this.uPriviledge = uPriviledge;
    }



}

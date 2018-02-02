package com.example.swetha_pt1880.blooddonar.database;

import java.io.Serializable;

/**
 * Created by swetha-pt1880 on 21/1/18.
 */

public class Donar implements Serializable {


    public  String dName;
    public  String dDOB;
    public  String dGender;
    public  String dContact;
    public  String dCurrentLoc;

    public Donar( String dName, String dDOB, String dGender, String dContact, String dCurrentLoc, String dBloodType, String dLastDonated, String dWeight) {

        this.dName = dName;
        this.dDOB = dDOB;
        this.dGender = dGender;
        this.dContact = dContact;
        this.dCurrentLoc = dCurrentLoc;
        this.dBloodType = dBloodType;
        this.dLastDonated = dLastDonated;
        this.dWeight = dWeight;
    }

    public  String dBloodType;
    public  String dLastDonated;
    public  String dWeight;

    public  Donar(){}

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdDOB() {
        return dDOB;
    }

    public void setdDOB(String dDOB) {
        this.dDOB = dDOB;
    }

    public String getdGender() {
        return dGender;
    }

    public void setdGender(String dGender) {
        this.dGender = dGender;
    }

    public String getdContact() {
        return dContact;
    }

    public void setdContact(String dContact) {
        this.dContact = dContact;
    }

    public String getdCurrentLoc() {
        return dCurrentLoc;
    }

    public void setdCurrentLoc(String dCurrentLoc) {
        this.dCurrentLoc = dCurrentLoc;
    }

    public String getdBloodType() {
        return dBloodType;
    }

    public void setdBloodType(String dBloodType) {
        this.dBloodType = dBloodType;
    }

    public String getdLastDonated() {
        return dLastDonated;
    }

    public void setdLastDonated(String dLastDonated) {
        this.dLastDonated = dLastDonated;
    }

    public String getdWeight() {
        return dWeight;
    }

    public void setdWeight(String dWeight) {
        this.dWeight = dWeight;
    }


}

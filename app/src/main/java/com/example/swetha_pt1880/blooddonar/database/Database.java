package com.example.swetha_pt1880.blooddonar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by swetha-pt1880 on 11/1/18.
 */

public class Database extends SQLiteOpenHelper {
    public static int dbVersion = 1;
    public static String dbName = "blooddonar.db";
    //tables in blood Donar
    public static String userTable = "Users";
    public static String donarTable = "Donars";

    //User table attributes

    public static String userId = "UserId";
    public static  String uName = "uName";
    public static String uDOB = "uDOB";
    public static String uGender = "uGender";
    public static String uContact = "uContact";
    public static String uPassword = "uPassWord";
    public static String uPriviledge = "uPriviledge";
    
    //Constants of User attributes
    
    public static int uIdCN = 0;
    public static int uNameCN = 1;
    public static int uDOBCN = 2;
    public static int uGenCN = 3;
    public static int uContactCN = 4;
    public static int uPassCN = 5;
    public static int uPrivCN = 6;
    
    
    

    //Donar table attributes

    public static String dId = "dId";
    public static String dName = "dName";
    public static String dDOB = "dDOB";
    public static String dGender = "dGender";
    public static String dContact = "dContact";
    public static String dCurrentLoc = "dCurrentLoc";
    public static String dBloodType = "dBloodType";
    public static String lastDonated = "dLastDonated";
    public static String dWeight = "dWeight";
    
    //Constants of donar attributes
    
    public static int dIdCN= 0;
    public static int dNameCN= 1;
    public static int dDOBCN= 2;
    public static int dGenderCN= 3;
    public static int dContactCN= 4;
    public static int dCLocCN= 5;
    public static int dBTCN= 6;
    public static int dlDonatedCN= 7;
    public static int dWeightCN= 8;
    
    

    public Database(Context context) {
        super(context,dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Users (userId TEXT PRIMARY KEY, uName TEXT NOT NULL, uDOB TEXT NOT NULL, uGender TEXT NOT NULL," +
                                    " uContact TEXT NOT NULL, uPassWord TEXT NOT NULL, uPriviledge TEXT NOT NULL)");
       sqLiteDatabase.execSQL("INSERT INTO Users VALUES('admin','SN','24/10/1996','Female','9491082374','admin','two')");

        sqLiteDatabase.execSQL("CREATE TABLE Donars ( dId INTEGER PRIMARY KEY AUTOINCREMENT, dName  TEXT NOT NULL, dDOB TEXT NOT NULL," +
                                "dGender TEXT NOT NULL,dContact TEXT NOT NULL, dCurrentLoc TEXT NOT NULL, dBloodType TEXT NOT NULL, " +
                                 "dLastDonated TEXT NOT NULL, dWeight INTEGER NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Donars");
    }


    //DataBase Operations


}

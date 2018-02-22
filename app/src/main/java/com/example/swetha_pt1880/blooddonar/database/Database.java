package com.example.swetha_pt1880.blooddonar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by swetha-pt1880 on 11/1/18.
 */

public class Database extends SQLiteOpenHelper {
    public static int dbVersion = 1;

    public String TAG = "Database ";
    public static String dbName = "blooddonar.db";
    //tables in blood Donar
    public static String userTable = "Users";
    public static String donarTable = "Donars";

    //User table attributes

    public static String userId = "userId";
    public static  String uName = "uName";
    public static String uDOB = "uDOB";
    public static String uGender = "uGender";
    public static String uContact = "uContact";
    public static String uPassword = "uPassWord";
    public static String uPriviledge = "uPriviledge";
    public static String uDonar = "donar";
    
    //Constants of User attributes
    
    public static int uIdCN = 0;
    public static int uNameCN = 1;
    public static int uDOBCN = 2;
    public static int uGenCN = 3;
    public static int uContactCN = 4;
    public static int uPassCN = 5;
    public static int uPrivCN = 6;
    public static int uDonarCN = 7;
    
    
    

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
    

    public static int dNameCN= 0;
    public static int dDOBCN= 1;
    public static int dGenderCN= 2;
    public static int dContactCN= 3;
    public static int dCLocCN= 4;
    public static int dBTCN= 5;
    public static int dlDonatedCN= 6;
    public static int dWeightCN= 7;
    
    

    public Database(Context context) {
        super(context,dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Users (userId TEXT PRIMARY KEY, uName TEXT NOT NULL, uDOB TEXT NOT NULL, uGender TEXT NOT NULL," +
                                    " uContact TEXT NOT NULL UNIQUE, uPassWord TEXT NOT NULL, uPriviledge TEXT NOT NULL, donar INTEGER)");
       //sqLiteDatabase.execSQL("INSERT INTO Users VALUES('admin','SN','24/10/1996','Female','9491082374','admin','two')");

        sqLiteDatabase.execSQL("CREATE TABLE Donars (  dName  TEXT NOT NULL, dDOB TEXT NOT NULL," +
                                "dGender TEXT NOT NULL,dContact TEXT PRIMARY KEY, dCurrentLoc TEXT NOT NULL, dBloodType TEXT NOT NULL, " +
                                 "dLastDonated TEXT NOT NULL, dWeight INTEGER NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Donars");
    }

    public  void delete(){
        SQLiteDatabase  database = this.getWritableDatabase();
        database.delete( userTable, null, null);
        database.delete( donarTable, null, null);
        database.close();


    }


    //DataBase Operations


    public int getMonths(String dob){
        Log.i(TAG , "dob " + dob);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG + "dob date", String.valueOf(date));
        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = date.getYear() + 1900;
        Log.i(TAG + "dyear,cyear", curYear + "  " +dobYear);
        int age = (curYear - dobYear) * 12;
        int curMonth = today.get(Calendar.MONTH) + 1;

        int dobMonth = date.getMonth() + 1;
        Log.i(TAG + "dmonth,cmonth", curMonth + "  " +dobMonth + " " );
        if (dobMonth > curMonth) { // this year can't be counted!
           age = age - (dobMonth - curMonth);

            Log.i(TAG + "month age", age + "");

        } else if (dobMonth == curMonth) { // same month? check for day

            int curDay = today.get(Calendar.DAY_OF_MONTH);

            int dobDay = date.getDay();
            Log.i(TAG + "ddate,cdate", curDay + "  " +dobDay);
            if (dobDay > curDay) { // this year can't be counted!

                age--;
                Log.i(TAG + "day age", age + "");

            }

        }
        Log.i(TAG + "final  age", age + "");
        return age;
    }

    public int getAge(String dob){
        Log.i(TAG , "dob " + dob);
        Date date = new Date(dob);
        Log.i(TAG + "dob date", String.valueOf(date));
        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = date.getYear() + 1899;
        Log.i(TAG + "dyear,cyear", curYear + "  " +dobYear);
        int age = curYear - dobYear;
        Log.i(TAG + "year age", age + "");
        int curMonth = today.get(Calendar.MONTH) + 1;

        int dobMonth = date.getMonth() + 1;
        Log.i(TAG + "dmonth,cmonth", curMonth + "  " +dobMonth);
        if (dobMonth > curMonth) { // this year can't be counted!

            age--;
            Log.i(TAG + "month age", age + "");

        } else if (dobMonth == curMonth) { // same month? check for day

            int curDay = today.get(Calendar.DAY_OF_MONTH);

            int dobDay = date.getDay();
            Log.i(TAG + "ddate,cdate", curDay + "  " +dobDay);
            if (dobDay > curDay) { // this year can't be counted!

                age--;
                Log.i(TAG + "day age", age + "");

            }

        }
        Log.i(TAG + "final  age", age + "");
        close();
        return age;


    }


}

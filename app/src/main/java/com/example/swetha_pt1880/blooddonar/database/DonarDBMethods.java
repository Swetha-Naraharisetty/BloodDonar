package com.example.swetha_pt1880.blooddonar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.swetha_pt1880.blooddonar.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by swetha-pt1880 on 15/1/18.
 */

public class DonarDBMethods {

    private SQLiteDatabase database, db;
    private Database dbHelper;

    public ArrayList<Donar> donarDetails = new ArrayList<>();

    String TAG = "DonarDBMethods :";

    public DonarDBMethods(Context context) {
        dbHelper = new Database(context);
    }



    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }
    public void readOpen(){
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //inserting data into Donar table
    public long addDonar( String dName, String dDOB, String dGen, String dContact, String dCurrentLoc, String blood, String lastDonated, String Weight) throws SQLException {
        open();
        ContentValues values = new ContentValues();
        //values.put(Database.dId, Donarid);
        values.put(Database.dName, dName);
        values.put(Database.dDOB, dDOB);
        values.put(Database.dGender, dGen);
        values.put(Database.dContact, dContact);
        values.put(Database.dCurrentLoc, dCurrentLoc);
        values.put(Database.dBloodType, blood);
        values.put(Database.lastDonated, lastDonated);
        values.put(Database.dWeight, Weight);
        long insertId = database.insert(Database.donarTable, null,values);
        close();
        return insertId;
    }
    //get   Donars details
    public Cursor getDonars()  {
        readOpen();
        Cursor cursor = db.rawQuery("select * from " + Database.donarTable, null );
        cursor.moveToNext();
        return cursor;
    }

    public Cursor getDonarProfile(int dId){
        readOpen();
        Cursor cursor = db.rawQuery("SELECT * from "+Database.donarTable +" where "+Database.dId+" = ? ",new String[] {"" + dId} );
        cursor.moveToNext();
        return cursor;
    }


    public String getDOB(int did){
        readOpen();
        Cursor cursor = db.rawQuery("select * from " + Database.donarTable + " where "+ Database.dId +"= ?", new String[] {"" + did} );
        cursor.moveToNext();
        return cursor.getString(2);
    }

    public ArrayList<Donar> getDonarsList(){
        readOpen();


        Cursor cursor = db.rawQuery("select * from " + Database.donarTable, null );
        cursor.moveToNext();
        if(cursor.getCount() > 0) {
            Log.i( TAG + "count", String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {

                do {
                    Log.i(TAG + "id", cursor.getString(1));
                    Donar donarinstance = new Donar();
                    donarinstance.setdId(cursor.getInt(Database.dIdCN));
                    donarinstance.setdName(cursor.getString(Database.dNameCN));
                    donarinstance.setdDOB(cursor.getString(Database.dDOBCN));
                    donarinstance.setdBloodType(cursor.getString(Database.dBTCN));
                    donarinstance.setdContact(cursor.getString(Database.dContactCN));
                    donarinstance.setdCurrentLoc(cursor.getString(Database.dCLocCN));
                    donarinstance.setdGender(cursor.getString(Database.dGenderCN));
                    donarinstance.setdLastDonated(cursor.getString(Database.dlDonatedCN));
                    donarinstance.setdWeight(cursor.getString(Database.dWeightCN));

                    donarDetails.add(donarinstance);


                } while (cursor.moveToNext());

            }
        }
        return donarDetails;
    }


    public int getAge(int did){
            Log.i(TAG + "dob", getDOB(did));
            Date date = new Date(getDOB(did));
            Log.i(TAG + "dob date", String.valueOf(date));
            Calendar today = Calendar.getInstance();
            int curYear = today.get(Calendar.YEAR);
            int dobYear = date.getYear() + 1899;
            Log.i(TAG + "dyear,cyear", curYear + "  " +dobYear);
            int age = curYear - dobYear;
            Log.i(TAG + "year age", age + "");
            int curMonth = today.get(Calendar.MONTH) + 1;

            int dobMonth = date.getMonth() - 1;
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

    public Cursor searchMenuItems(String searchTerm) {
        readOpen();
        Log.i(TAG +"search", searchTerm);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Database.donarTable);
        Cursor c = qb.query(db, null, "("+Database.dName+" LIKE '%"+searchTerm+"%') " +
                        "OR ("+Database.dBloodType+" LIKE '%" + searchTerm+"%')",
                null, null, null, null);
        return c;
    }
}

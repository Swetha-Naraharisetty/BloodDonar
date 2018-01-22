package com.example.swetha_pt1880.blooddonar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.swetha_pt1880.blooddonar.database.Database;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by swetha-pt1880 on 12/1/18.
 */

public class UserDBMethods  {
    private SQLiteDatabase database;
    private Database dbHelper;

    String TAG = "UserDBMethods :";

    public UserDBMethods(Context context) {
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //inserting data into user table
    public long addUser(String userId, String uName, String uDOB, String uGen, String uContact, String uPassword, String privs) throws SQLException {
        open();
        ContentValues values = new ContentValues();
        values.put(Database.userId, userId);
        values.put(Database.uName, uName);
        values.put(Database.uDOB, uDOB);
        values.put(Database.uGender, uGen);
        values.put(Database.uContact, uContact);
        values.put(Database.uPassword, uPassword);
        values.put(Database.uPriviledge, privs);
        long insertId = database.insert(Database.userTable, null,values);
        close();
        return insertId;
    }

    //validating the user credentials
    public String checkUser(String id, String pass) throws SQLException {

        Log.i(TAG + "credentials", id + pass);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.i("userId", id);
        Cursor cursor = db.rawQuery("SELECT * from "+Database.userTable +" where "+Database.userId+" = ? ",new String[] {"" +id} );
        Log.i(TAG + "count", cursor.getCount() + "");
        if(cursor.moveToFirst() ){
            //password = 6, priviledges = 7

            Log.i("pass", cursor.getString(6)  );
            if(cursor.getString(5).equals(pass)){
                db.close();
                return cursor.getString(6);
            }else
                return "three";
        }
        Log.i(TAG +"after cursor", "No data");
        db.close();
        return "zero";
    }

    //updating the user credentials
    public boolean updateUser(String phoneNo, String pass, String userId) throws SQLException {
        ContentValues contentValues = new ContentValues();
        //uName = 1, uDob = 2, gen = 3,  priv = 6
        String uName, uDob,ugen, upriv;
        Cursor  cursor = getProfile(userId);
        uName = cursor.getString(1);
        uDob = cursor.getString(2);
        ugen = cursor.getString(3);
        upriv = cursor.getString(6);

        contentValues.put(Database.userId, userId);
        contentValues.put(Database.uName,uName );
        contentValues.put(Database.uDOB, uDob);
        contentValues.put(Database.uGender, ugen);
        contentValues.put(Database.uContact,phoneNo);
        contentValues.put(Database.uPassword, pass);
        contentValues.put(Database.uPriviledge, upriv);
        open();
        int result = database.update(Database.userTable,contentValues, Database.userId + "=?", new String[]{userId} );
        Log.i("update result", String.valueOf(result));
        close();
        if(result > 0)
            return true;
        else
            return false;
    }



    //get  single user details
    public Cursor getUsers(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String id = "admin";
        Cursor cursor = db.rawQuery("select * from " + Database.userTable + " where " + Database.userId + " != ? ",new String[] {"" + id} );
        cursor.moveToNext();
        return cursor;
    }

    public Cursor getProfile(String userId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from "+Database.userTable +" where "+Database.userId+" = ? ",new String[] {"" + userId} );
        cursor.moveToNext();
        return cursor;
    }

    public String getDOB(String did){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Database.userTable + " where "+ Database.userId+"= ?", new String[] {"" + did} );
        cursor.moveToNext();
        return cursor.getString(2);
    }

    public boolean updatePreviledge(String uid, String operation) throws SQLException {

        ContentValues contentValues = new ContentValues();
        Cursor  cursor = getProfile(uid);
        contentValues.put(Database.userId, uid);
        contentValues.put(Database.uName,cursor.getString(1) );
        contentValues.put(Database.uDOB, cursor.getString(2));
        contentValues.put(Database.uGender, cursor.getString(3));
        contentValues.put(Database.uContact,cursor.getString(4));
        contentValues.put(Database.uPassword, cursor.getString(5));
        if(operation.equals("Grant")){
            contentValues.put(Database.uPriviledge,"one");
        }else if(operation.equals("Revoke")){
            contentValues.put(Database.uPriviledge,"zero");
        }

        open();
        int result = database.update(Database.userTable,contentValues, Database.userId + "=?", new String[]{uid} );
        Log.i("update result", String.valueOf(result));
        close();
        if(result > 0)
            return true;
        else
            return false;
    }


    public void delUser(String userId) throws SQLException {
        open();
        database.execSQL("DELETE from  "+Database.userTable+" where "+Database.userId+" = ?", new String[]{"" + userId});
        close();
    }



    public int getAge(String did){
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


}

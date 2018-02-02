package com.example.swetha_pt1880.blooddonar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.swetha_pt1880.blooddonar.database.Database.donarTable;

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
    public long addUser(String userId, String uName, String uDOB, String uGen, String uContact, String uPassword, String privs, int donar ) throws SQLException {
        open();
        ContentValues values = new ContentValues();
        values.put(Database.userId, userId);
        values.put(Database.uName, uName);
        values.put(Database.uDOB, uDOB);
        values.put(Database.uGender, uGen);
        values.put(Database.uContact, uContact);
        values.put(Database.uPassword, uPassword);
        values.put(Database.uPriviledge, privs);
        values.put(Database.uDonar, donar);
        long insertId = database.insert(Database.userTable, null,values);
        close();
        return insertId;
    }

    //validating the user credentials
    public String checkUser(String id, String pass) throws SQLException {

        Log.i(TAG + "credentials", id + pass);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
       // Cursor cursor = db.rawQuery("Select * from " + Database.userTable, null);

        Cursor cursor = db.rawQuery("Select * from "+Database.userTable , null);//+" where "+Database.userId+" = ?",new String[] {"" +id} );
        Log.i(TAG + "count", cursor.getCount() + "" + id + pass);
        if(cursor.moveToFirst()){
            //password = 6, priviledges = 7
           do {
               Log.i("pass", cursor.getString(5) + cursor.getString(0));
               if (cursor.getString(5).equals(pass) & cursor.getString(0).equals(id)) {
                   db.close();
                   return cursor.getString(6);
               }
           }while (cursor.moveToNext());
           // }else
               // return "three";
        }
        Log.i(TAG +"after cursor", "No data");
        db.close();
        return "zero";
    }

       public ArrayList<User> getUserList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        String id = "admin";
        Cursor cursor = db.rawQuery("select * from " + Database.userTable , null );
        cursor.moveToNext();
        if(cursor.getCount() > 0) {
            Log.i(TAG + "count", String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {

                do {
                    Log.i(TAG + "id", cursor.getString(Database.uIdCN) + cursor.getInt(Database.uDonarCN));
                    User newuser = new User();
                    newuser.setUserId(cursor.getString(Database.uIdCN));
                    newuser.setuName(cursor.getString(Database.uNameCN));
                    newuser.setuGender(cursor.getString(Database.uGenCN));
                    newuser.setuDOB(cursor.getString(Database.uDOBCN));
                    newuser.setuContact(cursor.getString(Database.uContactCN));
                    newuser.setuPassWord(cursor.getString(Database.uPassCN));
                    newuser.setuPriviledge(cursor.getString(Database.uPrivCN));
                    newuser.setDonar(cursor.getInt(Database.uDonarCN));
                    users.add(newuser);

                } while (cursor.moveToNext());
            }
        }

        return users;
    }


    public  void populateUsers(ArrayList<User> users) throws SQLException {
        open();
       // deleteUsers();
        for (User user: users){
                Log.i(TAG + "user donar ", user.getuName() + user.getDonar());

                long res = addUser(user.getUserId(), user.getuName(), user.getuDOB(), user.getuGender(), user.getuContact(),
                        user.getuPassWord(), user.getuPriviledge(), user.getDonar());

        }
        close();

    }


    public User getProfile(String userId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = new User();
        Log.i(TAG + "7th val", "trying to get" + "");
        Cursor cursor = db.rawQuery("SELECT * from "+Database.userTable +" where "+Database.userId+" = ? ",new String[] {"" + userId} );
        if(cursor.moveToFirst()) {
            Log.i(TAG + "7th val", cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4) + cursor.getString(5) + cursor.getString(6) + cursor.getInt(Database.uDonarCN));

            user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
        }
        return user;
    }

    public void delUser(String userId) throws SQLException {
        open();
        database.execSQL("DELETE from  "+Database.userTable+" where "+Database.userId+" = ?", new String[]{"" + userId});
        close();
    }

    public void deleteUsers() throws SQLException {
        open();
        database.delete( Database.userTable, null, null);
        close();
    }


    //update password
    public void updateUser(User user) throws SQLException {
        open();
        Log.i("+++++++donar", user.getDonar() + "");
        ContentValues cv = new ContentValues();
        cv.put(Database.userId, user.getUserId());
        cv.put(Database.uName, user.getuName());
        cv.put(Database.uDOB, user.getuDOB());
        cv.put(Database.uGender, user.getuGender());
        cv.put(Database.uContact, user.getuContact());
        cv.put(Database.uPassword, user.getuPassWord());
        cv.put(Database.uPriviledge, user.getuPriviledge());
        cv.put(Database.uDonar, user.getDonar());
        Log.i(TAG + "   =======donor===== ", user.getDonar()+ "");
        int result = database.update(Database.userTable,cv, Database.userId + "=?", new String[]{user.getUserId()} );
        close();
    }
}

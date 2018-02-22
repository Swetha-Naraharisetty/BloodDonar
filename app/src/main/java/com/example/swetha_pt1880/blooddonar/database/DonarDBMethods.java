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

    //updating donar details
    public void updateDonar(Donar donar) throws SQLException {
        open();
        Log.i(TAG + " updating donar ", donar.getdName() );
        ContentValues cv = new ContentValues();
        cv.put(Database.dName, donar.getdName());
        cv.put(Database.dBloodType, donar.getdBloodType());
        cv.put(Database.dContact, donar.getdContact());
        cv.put(Database.dCurrentLoc, donar.getdCurrentLoc());
        cv.put(Database.dDOB, donar.getdDOB());
        cv.put(Database.dGender, donar.getdGender());
        cv.put(Database.dWeight, donar.getdWeight());
        cv.put(Database.lastDonated, donar.getdLastDonated());
        Log.i(TAG + "   =======donor===== ", donar.getdName()+ "");
        int result = database.update(Database.donarTable,cv, Database.dContact + "=?", new String[]{donar.getdContact()} );

        close();

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
        long insertId = database.insert(donarTable, null,values);
        close();
        return insertId;
    }

    public ArrayList<Donar> getDonarsList(){
        readOpen();


        Cursor cursor = db.rawQuery("select * from " + donarTable, null );
        cursor.moveToNext();
        if(cursor.getCount() > 0) {
            Log.i( TAG + "count", String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {

                do {
                    Log.i(TAG + "id", cursor.getString(1));
                    Donar donarinstance = new Donar();
                    //donarinstance.setdId(cursor.getInt(Database.dIdCN));
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

    public void deleteDonars() throws SQLException {
        open();
        database.delete( donarTable, null, null);
        close();
    }

    public  void delDonar(String con) throws SQLException {
        open();
        database.execSQL("DELETE from  "+Database.donarTable+" where "+Database.dContact+" = ?", new String[]{"" + con});
        Log.i(TAG + " donar ", "deleted");
        close();
    }


    public Donar getDonarProfile(String contact){
        readOpen();

        Donar donarinstance = new Donar();
        Cursor cursor = db.rawQuery("select * from " + donarTable + " where " +Database.dContact + "=?", new String[]{contact} );
        cursor.moveToNext();
        if(cursor.moveToFirst()) {
            Log.i( TAG + " get count", String.valueOf(cursor.getCount()));

            Log.i(TAG + "get id", cursor.getString(Database.dContactCN));

            //donarinstance.setdId(cursor.getInt(Database.dIdCN));
            donarinstance.setdName(cursor.getString(Database.dNameCN));
            donarinstance.setdDOB(cursor.getString(Database.dDOBCN));
            donarinstance.setdBloodType(cursor.getString(Database.dBTCN));
            donarinstance.setdContact(cursor.getString(Database.dContactCN));
            donarinstance.setdCurrentLoc(cursor.getString(Database.dCLocCN));
            donarinstance.setdGender(cursor.getString(Database.dGenderCN));
            donarinstance.setdLastDonated(cursor.getString(Database.dlDonatedCN));
            donarinstance.setdWeight(cursor.getString(Database.dWeightCN));
        }
        return donarinstance;

    }


    public  void populateDonar(ArrayList<Donar> donars) throws SQLException {
        open();
        Log.i(TAG + " populating donar ", donars.toString());
        for(Donar donar: donars){
            Log.i(TAG + " populating donar ", donar.getdName() + "    ");
            addDonar(donar.getdName(), donar.getdDOB(), donar.getdGender(), donar.getdContact(),
                    donar.getdCurrentLoc(), donar.getdBloodType(), donar.getdLastDonated(),
                    donar.getdWeight());

        }
        close();
    }

}

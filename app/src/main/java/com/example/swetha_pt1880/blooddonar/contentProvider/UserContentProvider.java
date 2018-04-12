package com.example.swetha_pt1880.blooddonar.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;

/**
 * Created by swetha-pt1880 on 27/2/18.
 */

public class UserContentProvider extends ContentProvider {


    private SQLiteDatabase db;
   public Database dbHelper;
    public Context mContext;
    public UserDBMethods userDBMethods;


    private static final String AUTHORITY = "com.example.swetha_pt1880.blooddonar.contentProvider";

    public static final Uri CONTENT_URI_DETAIL_ITEM =
            Uri.parse("content://" + AUTHORITY + "/Users");

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "Users", 1);

    }
    public UserContentProvider(){}

   public UserContentProvider(Context context){
        mContext = context;
    }
    @Override
    public boolean onCreate() {

        dbHelper = new Database(mContext);
        userDBMethods = new UserDBMethods(getContext());
       return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable  String[] projection, @Nullable  String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String uriText = uri.toString();
        Log.i("query", uri + " ");
        Cursor cursor = userDBMethods.query(projection, selection, selectionArgs, sortOrder);
//        String tablename = uriText.substring(uriText.lastIndexOf("/")+1);
//
//        String groupBy = null;
//        String having = null;
//
//        return db.query(tablename, projection,
//                selection, selectionArgs,
//                groupBy, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

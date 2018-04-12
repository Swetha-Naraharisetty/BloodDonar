package com.example.swetha_pt1880.blooddonar.contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by swetha-pt1880 on 27/2/18.
 */

public class DonarContract {



    public static final String PATH_LOCATION = "donars";
    public static final String AUTHORITY =
            "com.example.swetha_pt1880.blooddonar.contract";
        public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);



    public static final class Donars implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        // Custom MIME types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + PATH_LOCATION;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + PATH_LOCATION;

        // Helper method
        public static Uri buildDonarUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}

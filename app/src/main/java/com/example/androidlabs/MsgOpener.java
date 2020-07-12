package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MsgOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "MessageDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "MESSAGE";
    public final static String COL_IS_SEND = "isSend";
    public final static String COL_MSG = "msg";
    public final static String COL_ID = "_id";
    public MsgOpener(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_IS_SEND + " INTEGER,"
                + COL_MSG  + " text);");  // add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }


    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

}

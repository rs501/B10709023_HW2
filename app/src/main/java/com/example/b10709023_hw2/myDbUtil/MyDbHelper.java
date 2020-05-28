package com.example.b10709023_hw2.myDbUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "waitList.db";
    private static final int DATABASE_VERSION = 1;

    public MyDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String FORMAT = "CREATE TABLE %s (   %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            "%s TEXT NOT NULL," +
                                            "%s INTEGER NOT NULL," +
                                            "%s TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        String sql = String.format(FORMAT,  MyDbContact.WaitList.TABLE_NAME,
                                            MyDbContact.WaitList._ID,
                                            
                                            MyDbContact.WaitList.COLUMN_SIZE,
                MyDbContact.WaitList.COLUMN_NAME,
                                            MyDbContact.WaitList.COLUMN_TIMESTAMP);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MyDbContact.WaitList.TABLE_NAME);
        onCreate(db);
    }

    public void printTable(){
        Cursor cursor = getWritableDatabase().query(MyDbContact.WaitList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        System.out.println("sout : print table");

        while(cursor.moveToNext()){
            System.out.println( "sout : " +
                    cursor.getString(0)+"\t"+
                    cursor.getString(1)+"\t"+
                    cursor.getString(2));
        }
        cursor.close();
    }
}

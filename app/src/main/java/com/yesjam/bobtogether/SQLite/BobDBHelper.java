package com.yesjam.bobtogether.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class BobDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Bob.db";
    private static final String DATABASE_TABLE_NAME = "bob";
    private static final int DATABASE_VERSION = 1;

    public BobDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + DATABASE_TABLE_NAME + " (number integer priary key" +
                        ", title text not null" +
                        ", place text not null" +
                        ", time text not null" +
                        ", tNum integer not null" +
                        ", nNum integer not null" +
                        ", email text not null" +
                        ", picUrl text not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE_NAME + "'");
        onCreate(sqLiteDatabase);
    }

    public void setBob(String title, String place, String time, int tNum, int nNum, String email, String picUrl) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("place", place);
        contentValues.put("time", time);
        contentValues.put("tNum", tNum);
        contentValues.put("nNum", nNum );
        contentValues.put("email", email);
        contentValues.put("picUrl", picUrl);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME, null, contentValues);
    }

    public int numOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int numOfRows = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, DATABASE_TABLE_NAME);
        return numOfRows;
    }


}

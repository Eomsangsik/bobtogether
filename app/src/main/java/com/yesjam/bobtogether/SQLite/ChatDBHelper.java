package com.yesjam.bobtogether.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ChatDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Chat.db";
    private static final String DATABASE_TABLE_NAME = "chat";

    public ChatDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table chat (number integer priary key, roomUid text not null, message text not null, sender text not null"
                        + ", date text not null, ISee integer, email text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat");
        onCreate(sqLiteDatabase);
    }

    public boolean setChat(String roomUid, String message, String sender, String date, int iSee, String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("RoomUid", roomUid);
        contentValues.put("message", message);
        contentValues.put("sender", sender);
        contentValues.put("date", date);
        contentValues.put("ISee", iSee);
        contentValues.put("email", email);

        sqLiteDatabase.insert(DATABASE_TABLE_NAME, null, contentValues);

        return true;
    }

    public int numOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int numOfRows = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, DATABASE_TABLE_NAME);
        return numOfRows;
    }

    public void deleteChat(String roomUid) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from chat where roomUid = '" + roomUid + "'");
    }

    public void reChat() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<String> getMessage(String roomUid) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> messageList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from chat where roomUid = '" + roomUid + "'", null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            messageList.add(cursor.getString(cursor.getColumnIndex("message")));
            cursor.moveToNext();
        }

        return messageList;
    }

    public ArrayList<String> getSender(String roomUid) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> senderList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from chat where roomUid = '" + roomUid + "'", null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            senderList.add(cursor.getString(cursor.getColumnIndex("sender")));
            cursor.moveToNext();
        }

        return senderList;
    }

    public ArrayList<String> getDate(String roomUid) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> dateList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from chat where roomUid = '" + roomUid + "'", null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            dateList.add(cursor.getString(cursor.getColumnIndex("date")));
            cursor.moveToNext();
        }

        return dateList;
    }

    public ArrayList<String> getEmail(String roomUid) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> emailList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from chat where roomUid = '" + roomUid + "'", null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            emailList.add(cursor.getString(cursor.getColumnIndex("email")));
            cursor.moveToNext();
        }

        return emailList;
    }
}

package com.yesjam.bobtogether.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class ChatRoomDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChatRoom.db";
    private static final String DATABASE_TABLE_NAME = "chatRoom";
    private static final int DATABASE_VERSION = 1;

    public ChatRoomDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + DATABASE_TABLE_NAME + " (number integer priary key" +
                        ", chatRoomUid text not null" +
                        ", chatRoomName text not null" +
                        ", tNum integer not null" +
                        ", nNum integer not null" +
                        ", picUrl text not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE_NAME + "'");
        onCreate(sqLiteDatabase);
    }

    public void setChatRoom(String roomUid, String chatRoomName, int tNum, int nNum, String picUrl) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("chatRoomUid", roomUid);
        contentValues.put("chatRoomName", chatRoomName);
        contentValues.put("tNum", tNum);
        contentValues.put("nNum", nNum);
        contentValues.put("picUrl", picUrl);

        sqLiteDatabase.insert(DATABASE_TABLE_NAME, null, contentValues);
    }

    public int numOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int numOfRows = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, DATABASE_TABLE_NAME);
        return numOfRows;
    }

//    public void reChatRoom() {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + DATABASE_NAME + "'");
//        onCreate(sqLiteDatabase);
//    }

    public String getChatRoomUid(int position) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        ArrayList<String> roomUidList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME + " where number = '" + position + "'", null);
        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            roomUidList.add(cursor.getString(cursor.getColumnIndex("roomUid")));
//            cursor.moveToNext();
//        }
        return cursor.getString(cursor.getColumnIndex("chatRoomUid"));
    }

    public String getChatRoomName(int position) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME + " where number = '" + position + "'", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("chatRoomName"));
    }

    public int getTNum(int position) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME + " where number = '" + position + "'", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("pNum"));
    }

    public int getNNum(int position) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME + " where number = '" + position + "'", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("nNum"));
    }

    public String getPicUrl(int position) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME + " where number = '" + position + "'", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("picUrl"));
    }


    public ArrayList<String> getAllRoomUid() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> chatRoomUidList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            chatRoomUidList.add(cursor.getString(cursor.getColumnIndex("chatRoomUid")));
            cursor.moveToNext();
        }
        return chatRoomUidList;
    }

    public ArrayList<String> getAllRoomName() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> chatRoomNameList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            chatRoomNameList.add(cursor.getString(cursor.getColumnIndex("chatRoomName")));
            cursor.moveToNext();
        }
        return chatRoomNameList;
    }

    public ArrayList<Integer> getAllTNum() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> tNumList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            tNumList.add(cursor.getInt(cursor.getColumnIndex("tNum")));
            cursor.moveToNext();
        }
        return tNumList;
    }

    public ArrayList<Integer> getAllNNum() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> nNumList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            nNumList.add(cursor.getInt(cursor.getColumnIndex("nNum")));
            cursor.moveToNext();
        }
        return nNumList;
    }

    public ArrayList<String> getAllPicUrl() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> picUrlList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            picUrlList.add(cursor.getString(cursor.getColumnIndex("picUrl")));
            cursor.moveToNext();
        }
        return picUrlList;
    }
}

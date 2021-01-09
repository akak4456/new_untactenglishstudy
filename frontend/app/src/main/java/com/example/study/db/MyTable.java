package com.example.study.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyTable extends MyDBHelper {
    private SQLiteDatabase db;
    public MyTable(Context context) {
        super(context);
        db = getWritableDatabase();
    }

    public void insert(int vocaid,int itemid){
        db.execSQL("INSERT INTO vocatablecheckbox(vocaid,itemid) VALUES("+vocaid+","+itemid+")");
    }

    public void delete(int vocaid,int itemid){
        db.execSQL("DELETE FROM vocatablecheckbox WHERE vocaid="+vocaid+" AND itemid="+itemid);
    }

    public ArrayList<MyTableList> select(int vocaid) {
        // 테이블의 모든 데이터 선택
        Cursor mCursor = db.rawQuery("SELECT * FROM vocatablecheckbox WHERE vocaid="+vocaid, null);
        ArrayList<MyTableList> list = new ArrayList<>();
        if(mCursor.moveToFirst()) {
            do {
                list.add(new MyTableList(mCursor.getInt(0), mCursor.getInt(1)));
            } while(mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }
}

package com.example.ivanp.book.LocalStorage;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;

public class UserRepository {
    SQLiteDatabase db;
    public UserRepository(){
//        db = SQLiteDatabase.openOrCreateDatabase("bookscript", Context.MODE_PRIVATE, null);
//        db = openOrCerateDatabase("bookscript", MODE_PRIVATE, null);
    }

    public boolean isTokenValid(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
//        db.close();
//        return count>0;
        return false;
    }

    public String getToken(){

        return "";
    }
}

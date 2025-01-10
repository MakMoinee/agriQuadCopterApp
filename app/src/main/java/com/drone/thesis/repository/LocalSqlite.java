package com.drone.thesis.repository;

import android.content.Context;

import com.github.MakMoinee.library.services.MSqliteDBHelper;

public class LocalSqlite extends MSqliteDBHelper {
    private static final String dbName = "quadcopter.db";
    private static LocalSqlite instance;

    public LocalSqlite(Context context) {
        super(context, dbName);
    }

    public static LocalSqlite getInstance(Context context) {
        if (instance == null) {
            instance = new LocalSqlite(context);
        }
        return instance;
    }



}

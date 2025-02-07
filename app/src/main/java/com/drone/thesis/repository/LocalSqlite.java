package com.drone.thesis.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstName TEXT NOT NULL, " +
                "middleName TEXT, " +
                "lastName TEXT NOT NULL, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS drones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "droneName TEXT NOT NULL, " +
                "userID INTEGER NOT NULL, " +
                "ip TEXT NOT NULL, " +
                "status TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS drones");
        onCreate(db);
    }
}

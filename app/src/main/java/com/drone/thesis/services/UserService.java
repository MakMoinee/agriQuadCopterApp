package com.drone.thesis.services;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.drone.thesis.models.Users;
import com.drone.thesis.repository.LocalSqlite;
import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.services.HashPass;
import com.google.gson.Gson;

public class UserService {
    private LocalSqlite sqlite;
    private final String TABLE_USER = "users";

    HashPass hashPass = new HashPass();

    public UserService(Context mContext) {
        sqlite = LocalSqlite.getInstance(mContext);
    }

    public void insertUser(Users users, DefaultBaseListener listener) {
        SQLiteDatabase db = sqlite.getWritableDatabase();
        ContentValues values = MapForm.toContentValues(users);
        values.remove("id");
        try {
            long count = db.insert(TABLE_USER, null, values);
            if (count != -1) {
                listener.onSuccess("success add");
            } else {
                listener.onError(new Error("failed to add error"));
            }
        } catch (Exception e) {
            Log.e("error_insert", e.getLocalizedMessage());
            listener.onError(new Error(e.getMessage()));
        } finally {
            db.close();
        }
    }

    public void insertUniqueUser(Users users, DefaultBaseListener listener) {
        this.checkUser(users, new DefaultBaseListener() {
            @Override
            public <T> void onSuccess(T t) {
                listener.onError(new Error("user exist"));
            }

            @Override
            public void onError(Error error) {
                insertUser(users, new DefaultBaseListener() {
                    @Override
                    public <T> void onSuccess(T t) {
                        listener.onSuccess("success add user");
                    }

                    @Override
                    public void onError(Error error) {
                        listener.onError(new Error("failed to add user"));
                    }
                });
            }
        });
    }

    @SuppressLint("Range")
    public void checkUser(Users users, DefaultBaseListener listener) {
        SQLiteDatabase db = sqlite.getWritableDatabase();
        String[] columns = {"username", "password"};
        String selection = "username=?";
        String[] selectionArgs = {users.getUsername()};
        String result = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("username"));
                if (result != null) {
                    listener.onSuccess("user exist");
                }
            } else {

                listener.onError(new Error("Invalid username or password."));
            }

        } catch (Exception e) {
            listener.onError(new Error("Error checking user: " + e.getMessage()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

    }

    @SuppressLint("Range")
    public Users getUserRecord(String username, String password) {
        Users user = null;
        SQLiteDatabase db = sqlite.getReadableDatabase();
        String[] columns = {"id", "username", "password", "firstName", "middleName", "lastName"};  // Include necessary columns
        String selection = "username=?";
        String[] selectionArgs = {username};
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String uname = cursor.getString(cursor.getColumnIndex("username"));
                    String pass = cursor.getString(cursor.getColumnIndex("password"));
                    String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
                    String middleName = cursor.getString(cursor.getColumnIndex("middleName"));
                    String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
                    Log.e("id >>>>", Integer.toString(id));
                    if (hashPass.verifyPassword(password, pass)) {
                        user = new Users.UserBuilder()
                                .setId(id)
                                .setFirstName(firstName)
                                .setMiddleName(middleName)
                                .setLastName(lastName)
                                .setUsername(uname)
                                .build();
                        break;
                    }


                } while (cursor.moveToNext());


            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error retrieving user record: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }


}

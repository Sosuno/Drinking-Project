package com.drinkingTeam.drinkingProject.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drinkingTeam.drinkingProject.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static com.drinkingTeam.drinkingProject.tables.UserReaderContract.UserTable.*;
import static com.drinkingTeam.drinkingProject.tables.UserReaderContract.UserTable.TABLE_NAME;

public class UserDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "UserReader.db";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT," +
                    COLUMN_NAME_PASSWORD + " TEXT," +
                    COLUMN_NAME_EMAIL + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Long addUser(SQLiteDatabase db, UserEntity userEntity){

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, userEntity.getUsername());
        values.put(COLUMN_NAME_PASSWORD, userEntity.getPassword());
        values.put(COLUMN_NAME_EMAIL, userEntity.getEmail());
        return db.insert(TABLE_NAME,null,values);
    }

    public void removeUser(SQLiteDatabase db) {
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public List<UserEntity> getUser(SQLiteDatabase db){
        String[] projection = {
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_PASSWORD,
                COLUMN_NAME_EMAIL
        };
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<UserEntity> ingredients = new ArrayList<>();
        while(cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_EMAIL));

            ingredients.add(new UserEntity(null, username,password,email));
        }
        cursor.close();
        return ingredients;
    }
}

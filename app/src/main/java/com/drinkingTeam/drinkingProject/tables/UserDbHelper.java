package com.drinkingTeam.drinkingProject.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drinkingTeam.drinkingProject.entities.UserEntity;

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
            "CREATE TABLE " + UserReaderContract.UserTable.TABLE_NAME + " (" +
                    UserReaderContract.UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserReaderContract.UserTable.COLUMN_NAME_USERNAME + " TEXT," +
                    UserReaderContract.UserTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    UserReaderContract.UserTable.COLUMN_NAME_EMAIL + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + IngredientsReaderContract.IngredientsTable.TABLE_NAME;

    public Long addToIngredients(SQLiteDatabase db, UserEntity userEntity){

        ContentValues values = new ContentValues();
        values.put(UserReaderContract.UserTable.COLUMN_NAME_USERNAME, userEntity.getUsername());
        values.put(UserReaderContract.UserTable.COLUMN_NAME_PASSWORD, userEntity.getPassword());
        values.put(UserReaderContract.UserTable.COLUMN_NAME_EMAIL, userEntity.getEmail());
        return db.insert(DrinksReaderContract.DrinksTable.TABLE_NAME,null,values);
    }

    public int removeUser(SQLiteDatabase db ,long id) {
        String selection = UserReaderContract.UserTable._ID + " = ?";
        String[] selectionArgs = { id+"" };
        return db.delete(UserReaderContract.UserTable.TABLE_NAME,selection,selectionArgs);
    }

}

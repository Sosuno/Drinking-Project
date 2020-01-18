package com.drinkingTeam.drinkingProject.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.drinkingTeam.drinkingProject.tables.DrinksReaderContract;
import com.drinkingTeam.drinkingProject.tables.DrinksReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class DrinkEntity {
    private long id;
    private String name;
    private byte[] image;
    private String recipe;
    private String description;

    public DrinkEntity() {}

    public DrinkEntity(long id, String name, byte[] image, String recipe, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.recipe = recipe;
        this.description = description;
    }

    public long addToFavourites(DrinkEntity drinkEntity, Context context){
        DrinksReaderDbHelper dbHelper = new DrinksReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME, drinkEntity.getName());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE, drinkEntity.getImage());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE, drinkEntity.getRecipe());
        return db.insert(DrinksReaderContract.DrinksTable.TABLE_NAME,null,values);
    }

    public int removeFromFavourites(long id, Context context) {
        DrinksReaderDbHelper dbHelper = new DrinksReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DrinksReaderContract.DrinksTable._ID + " = ?";
        String[] selectionArgs = { id+"" };
        return db.delete(DrinksReaderContract.DrinksTable.TABLE_NAME,selection,selectionArgs);
    }

    public List<DrinkEntity> getAllFavourites(Context context){
        DrinksReaderDbHelper dbHelper = new DrinksReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE
        };

        Cursor cursor = db.query(
                DrinksReaderContract.DrinksTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<DrinkEntity> drinks = new ArrayList<>();
        while(cursor.moveToNext()) {
            long drinkId = cursor.getLong(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable._ID));
            String drinkName = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE));
            String recipe = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION));

            drinks.add(new DrinkEntity(drinkId, drinkName,image,recipe, desc));
        }
        cursor.close();
        return drinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

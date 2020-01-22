package com.drinkingTeam.drinkingProject.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.drinkingTeam.drinkingProject.types.Drink;
import com.drinkingTeam.drinkingProject.entities.DrinkEntity;

import java.util.ArrayList;
import java.util.List;

public class DrinksDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DrinksReader.db";
    private IngredientsDbHelper ingredientsDb;

    public DrinksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ingredientsDb = new IngredientsDbHelper(context);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void newUser(SQLiteDatabase db){
        ingredientsDb.newUser(db);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DrinksReaderContract.DrinksTable.TABLE_NAME + " (" +
                    DrinksReaderContract.DrinksTable._ID + " INTEGER PRIMARY KEY," +
                    DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME + " TEXT," +
                    DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE + " TEXT," +
                    DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE + " TEXT,"+
                    DrinksReaderContract.DrinksTable.COLUMN_NAME_GLASS + " Text," +
                    DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION + " Text )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DrinksReaderContract.DrinksTable.TABLE_NAME;

    public Long addToFavourites(SQLiteDatabase db, DrinkEntity drinkEntity){

        ContentValues values = new ContentValues();
        values.put(DrinksReaderContract.DrinksTable._ID,drinkEntity.getId());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME, drinkEntity.getName());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE, drinkEntity.getImage());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE, drinkEntity.getRecipe());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_GLASS, drinkEntity.getGlass());
        values.put(DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION, drinkEntity.getDescription());
        return db.insert(DrinksReaderContract.DrinksTable.TABLE_NAME,null,values);
    }

    public int removeFromFavourites(SQLiteDatabase db ,long id) {
        ingredientsDb.removeDrinkIngredients(db,id);
        String selection = DrinksReaderContract.DrinksTable._ID + " = ?";
        String[] selectionArgs = { id+"" };
        return db.delete(DrinksReaderContract.DrinksTable.TABLE_NAME,selection,selectionArgs);
    }

    public List<Drink> getAllFavourites(SQLiteDatabase db){
        String[] projection = {
                BaseColumns._ID,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION,
                DrinksReaderContract.DrinksTable.COLUMN_NAME_GLASS
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
        List<Drink> drinks = new ArrayList<>();
        while(cursor.moveToNext()) {
            Long drinkId = cursor.getLong(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable._ID));
            String drinkName = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE));
            String recipe = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION));
            String glass = cursor.getString(cursor.getColumnIndexOrThrow(DrinksReaderContract.DrinksTable.COLUMN_NAME_GLASS));

            drinks.add(new Drink(drinkId, drinkName,desc,recipe, image, glass, ingredientsDb.getIngredientsForDrink(db,drinkId)));
        }
        cursor.close();
        return drinks;
    }

}

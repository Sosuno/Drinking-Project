package com.drinkingTeam.drinkingProject.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drinkingTeam.drinkingProject.Ingredient;
import com.drinkingTeam.drinkingProject.entities.IngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class IngredientsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "IngredientReader.db";

    public IngredientsDbHelper(Context context) {
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

    public void newUser(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + IngredientsReaderContract.IngredientsTable.TABLE_NAME + " (" +
                    IngredientsReaderContract.IngredientsTable._ID + " INTEGER PRIMARY KEY," +
                    IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME + " TEXT," +
                    IngredientsReaderContract.IngredientsTable.COLUMN_NAME_QUANTITY + " TEXT," +
                    IngredientsReaderContract.IngredientsTable.COLUMN_NAME_UNITS + " TEXT,"+
                    IngredientsReaderContract.IngredientsTable.COLUMN_NAME_DRINK_ID + " BIGINT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + IngredientsReaderContract.IngredientsTable.TABLE_NAME;

    public Long addToIngredients(SQLiteDatabase db, IngredientEntity ingredientEntity){

        ContentValues values = new ContentValues();
        values.put(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME, ingredientEntity.getName());
        values.put(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_QUANTITY, ingredientEntity.getQuantity());
        values.put(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_UNITS, ingredientEntity.getUnits());
        values.put(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_DRINK_ID, ingredientEntity.getDrinkId());
        return db.insert(IngredientsReaderContract.IngredientsTable.TABLE_NAME,null,values);
    }

    public int removeDrinkIngredients(SQLiteDatabase db ,long id) {
        String selection = IngredientsReaderContract.IngredientsTable._ID + " = ?";
        String[] selectionArgs = { id+"" };
        return db.delete(IngredientsReaderContract.IngredientsTable.TABLE_NAME,selection,selectionArgs);
    }

    public List<Ingredient> getIngredientsForDrink(SQLiteDatabase db, Long drinkId){
        String[] projection = {
                IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME,
                IngredientsReaderContract.IngredientsTable.COLUMN_NAME_QUANTITY,
                IngredientsReaderContract.IngredientsTable.COLUMN_NAME_UNITS,
        };
        String selection = IngredientsReaderContract.IngredientsTable.COLUMN_NAME_DRINK_ID + " = ?";
        String[] args = { drinkId+""};

        Cursor cursor = db.query(
                IngredientsReaderContract.IngredientsTable.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
        );
        List<Ingredient> ingredients = new ArrayList<>();
        while(cursor.moveToNext()) {
            Long ingredient = cursor.getLong(cursor.getColumnIndexOrThrow(IngredientsReaderContract.IngredientsTable._ID));
            String drinkName = cursor.getString(cursor.getColumnIndexOrThrow(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME));
            String quantity = cursor.getString(cursor.getColumnIndexOrThrow(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_QUANTITY));
            String units = cursor.getString(cursor.getColumnIndexOrThrow(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_UNITS));

            ingredients.add(new Ingredient(ingredient, drinkName,quantity,units));
        }
        cursor.close();
        return ingredients;
    }
}

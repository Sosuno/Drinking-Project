package com.drinkingTeam.drinkingproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;


import com.drinkingTeam.drinkingProject.entities.DrinkEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLIntegrityConstraintViolationException;

import static junit.framework.TestCase.assertNotNull;


public class DrinkDbHelperTest {
    private Context context = ApplicationProvider.getApplicationContext();
    private DrinksDbHelper dbHelper = new DrinksDbHelper(context);
    private String query;
    private SQLiteDatabase db;

    @Before
    public void init() {
        db = dbHelper.getWritableDatabase();
        query = "INSERT INTO drinks" +
                "(name,image,recipe,description,glass) VALUES " +
                "(\"aaa\",null,\"aaa\",\"aaa\",\"aaa\")," +
                "(\"bbb\",null,\"bbb\",\"bbb\",\"bbb\")," +
                "(\"ccc\",null,\"ccc\",\"ccc\",\"ccc\")," +
                "(\"bbb\",null,\"bbb\",\"bbb\",\"bbb\");";
        db.execSQL(query);
    }
    @Test
    public void successfulAddToFavouriteTest() {
        DrinkEntity drink = new DrinkEntity(null,"aa","aaa","aa","aaa","aa");
        Long id = dbHelper.addToFavourites(dbHelper.getWritableDatabase(),drink);
        assertNotNull(id);
    }


    @Test
    public void failed() throws SQLIntegrityConstraintViolationException {
        DrinkEntity drink = new DrinkEntity(99L,"aa","aaa","aa","aaa","aa");
        DrinkEntity drink2 = new DrinkEntity(99L,"aa","aaa","aa","aaa","aa");

        dbHelper.addToFavourites(dbHelper.getWritableDatabase(), drink);
        dbHelper.addToFavourites(dbHelper.getWritableDatabase(), drink2);

    }



}

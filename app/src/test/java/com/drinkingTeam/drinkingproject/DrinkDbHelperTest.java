package com.drinkingTeam.drinkingproject;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.drinkingTeam.drinkingProject.entities.DrinkEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DrinkDbHelperTest {
    private Context cxt = ApplicationProvider.getApplicationContext();
    private DrinksDbHelper dbHelper = new DrinksDbHelper(cxt);
    private String query;

    @Before
    public void init() {
        query = "INSERT INTO drinks" +
                "(name,image,recipe,description,glass) VALUES " +
                "(\"aaa\",null,\"aaa\",\"aaa\",\"aaa\")," +
                "(\"bbb\",null,\"bbb\",\"bbb\",\"bbb\")," +
                "(\"ccc\",null,\"ccc\",\"ccc\",\"ccc\")," +
                "(\"bbb\",null,\"bbb\",\"bbb\",\"bbb\");";
        dbHelper.getWritableDatabase().execSQL(query);
    }

    @Test
    public void addToFavouriteTest() {
        DrinkEntity drink = new DrinkEntity(null,"aa","aaa","aa","aaa","aa");
        Long id = dbHelper.addToFavourites(dbHelper.getWritableDatabase(),drink);
        assertNotNull(id);

    }

    @Test
    public void getAllfavsTest() {

        query = "SELECT * FROM drinks";


    }

}
//     public DrinkEntity(long id, String name,String image, String recipe, String description, String glass) {
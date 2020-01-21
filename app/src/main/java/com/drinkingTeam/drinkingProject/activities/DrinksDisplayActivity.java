package com.drinkingTeam.drinkingProject.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drinkingTeam.drinkingProject.Drink;
import com.drinkingTeam.drinkingProject.R;
import com.google.gson.Gson;

public class DrinksDisplayActivity extends AppCompatActivity {

    private Drink drink;
    private TextView textViewDrinkName;
    private ImageView drinkImg;
    private TextView description;
    private TextView glass;
    private TextView recipe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        String studentDataObjectAsAString = getIntent().getStringExtra("Drink");
        boolean isFavourite = getIntent().getBooleanExtra("favourite", false);
        drink = gson.fromJson(studentDataObjectAsAString, Drink.class);
        setContentView(R.layout.recipe);
        textViewDrinkName.setText(drink.getName());

        drinkImg = findViewById(R.id.imageView);
        byte[] base64converted = Base64.decode(drink.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(base64converted, 0, base64converted.length);
        drinkImg.setImageBitmap(bitmap);
        description = findViewById(R.id.Description);
        glass = findViewById(R.id.glass);
        recipe = findViewById(R.id.glass);


        ///ingredients_list


    }
}

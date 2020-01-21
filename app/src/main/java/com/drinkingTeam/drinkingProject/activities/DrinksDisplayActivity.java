package com.drinkingTeam.drinkingProject.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drinkingTeam.drinkingProject.Drink;
import com.drinkingTeam.drinkingProject.IngredientsListAdapter;
import com.drinkingTeam.drinkingProject.R;
import com.google.gson.Gson;

public class DrinksDisplayActivity extends AppCompatActivity {

    private Drink drink;
    private ImageButton fav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        String studentDataObjectAsAString = getIntent().getStringExtra("Drink");
        boolean isFavourite = getIntent().getBooleanExtra("favourite", false);
        drink = gson.fromJson(studentDataObjectAsAString, Drink.class);
        setContentView(R.layout.recipe);
        TextView textViewDrinkName = findViewById(R.id.textViewDrinkName);
        textViewDrinkName.setText(drink.getName());
        ImageView drinkImg = findViewById(R.id.drinkImg);
        byte[] base64converted = Base64.decode(drink.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(base64converted, 0, base64converted.length);
        drinkImg.setImageBitmap(bitmap);
        TextView description = findViewById(R.id.Description);
        description.setText(drink.getDescription());
        TextView glass = findViewById(R.id.glass);
        glass.setText(drink.getGlass());
        TextView recipe = findViewById(R.id.textViewRecipe);
        recipe.setText(drink.getRecipe());
        fav = findViewById(R.id.favourites_Button);
        if(isFavourite) {
            fav.setActivated(true);
        }
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,R.layout.ingredient_list,drink.getIngredients());
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ListView ingredients = findViewById(R.id.ingredients_list);

        ingredients.setAdapter(adapter);
        ///ingredients_list


    }
}

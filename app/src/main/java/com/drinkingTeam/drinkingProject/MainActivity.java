package com.drinkingTeam.drinkingProject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drinkingTeam.drinkingProject.entities.DrinkEntity;
import com.drinkingTeam.drinkingProject.entities.IngredientEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.drinkingTeam.drinkingProject.tables.IngredientsDbHelper;
import com.drinkingTeam.drinkingProject.tables.IngredientsReaderContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static com.drinkingTeam.Singleton.GET_DRINKS;
import static com.drinkingTeam.Singleton.GET_DRINKS_REQUEST_TAG;
import static com.drinkingTeam.Singleton.HOST;
import static com.drinkingTeam.Singleton.VERY_SECRET_PASSWORD;
import static com.drinkingTeam.Singleton.error;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.COLUMN_NAME_DESCRIPTION;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.COLUMN_NAME_GLASS;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.COLUMN_NAME_IMAGE;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.COLUMN_NAME_NAME;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.COLUMN_NAME_RECIPE;
import static com.drinkingTeam.drinkingProject.tables.DrinksReaderContract.DrinksTable.TABLE_NAME;
import static com.drinkingTeam.drinkingProject.tables.IngredientsReaderContract.IngredientsTable.COLUMN_NAME_QUANTITY;
import static com.drinkingTeam.drinkingProject.tables.IngredientsReaderContract.IngredientsTable.COLUMN_NAME_UNITS;


public class MainActivity extends AppCompatActivity {


    private List<Drink> drinks = new ArrayList<>();
    private List<Drink> favdrinks = new ArrayList<>();
    private MyListAdapter adapter;
    private MyListAdapter adapter2;
    private ListView listView;
    private DrinksDbHelper drinkDb;
    private IngredientsDbHelper ingredientsDb;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    drinks = getDrinks();
                    System.out.println(drinks.size());
                    if(drinks.size() == 0){
                        error(getApplicationContext(),R.string.no_connection);
                    }else {
                    adapter.setDrinkList(drinks);
                    listView.setAdapter(adapter);
                    }

                    return true;

                case R.id.navigation_dashboard:
                    if(favdrinks.size() == 0) {
                        error(getApplicationContext(), R.string.no_favourites);
                    }

                    adapter2.setDrinkList(favdrinks);
                    listView.setAdapter(adapter2);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        drinkDb = new DrinksDbHelper(this);
        ingredientsDb = new IngredientsDbHelper(this);

        adapter2 = new MyListAdapter(this, R.layout.my_custom_list, favdrinks,favdrinks);
        listView = (ListView) findViewById(R.id.bubu);
        adapter = new MyListAdapter(this, R.layout.my_custom_list, drinks,favdrinks);
        drinks = getDrinks();


    }

    public List<Drink> getDrinks() {
        drinksFromJson(this);
        return drinks;
    }

    private void drinksFromJson(final Context context) {
        final RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = HOST + GET_DRINKS;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            drinks.clear();
                            JSONArray  jsonArray = response.getJSONArray(TABLE_NAME);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                Drink drink = new Drink();
                                drink.setDescription(json.getString(COLUMN_NAME_DESCRIPTION));
                                drink.setId(json.getLong(_ID));
                                drink.setName(json.getString(COLUMN_NAME_NAME));
                                drink.setGlass(json.getString(COLUMN_NAME_GLASS));
                                drink.setImage(json.getString(COLUMN_NAME_IMAGE));
                                drink.setRecipe(json.getString(COLUMN_NAME_RECIPE));
                                JSONArray ingredientsArray = json.getJSONArray(IngredientsReaderContract.IngredientsTable.TABLE_NAME);
                                List<Ingredient> ingredients = new ArrayList<>();
                                System.out.println(ingredientsArray.length());
                                for (int j = 0; j < ingredientsArray.length(); j++) {
                                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(j);
                                    Ingredient ingredient = new Ingredient();
                                    ingredient.setId(jsonIngredient.getLong(_ID));
                                    ingredient.setName(jsonIngredient.getString(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME));
                                    ingredient.setQuantity(jsonIngredient.getString(COLUMN_NAME_QUANTITY));
                                    ingredient.setUnits(jsonIngredient.getString(COLUMN_NAME_UNITS));
                                    ingredients.add(ingredient);
                                }
                                drink.setIngredients(ingredients);
                                addToDrinks(drink);
                            }
                            adapter.setDrinkList(drinks);
                            listView.setAdapter(adapter);
                            tempAddTofavs(drinks.get(0));
                            if(drinks.size() == 0) {
                                error(context, R.string.no_connection);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mQueue.cancelAll(GET_DRINKS_REQUEST_TAG);
                if(drinks.size() == 0) {
                    error(context,R.string.no_connection);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", VERY_SECRET_PASSWORD);
                return headers;
            }
        };
        request.setTag(GET_DRINKS_REQUEST_TAG);
        request.setShouldRetryServerErrors(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10, 1, 2));
        mQueue.add(request);
    }

    private void addToDrinks(Drink d){
        drinks.add(d);
    }

    private void tempAddTofavs(Drink drink) {
        favdrinks.clear();
        DrinkEntity d = new DrinkEntity(drink);
        for (Ingredient i: drink.getIngredients()){
            IngredientEntity entity = new IngredientEntity(i);
            entity.setDrinkId(drink.getId());
            ingredientsDb.addToIngredients(ingredientsDb.getWritableDatabase(), entity);
        }
        drinkDb.addToFavourites(drinkDb.getWritableDatabase(),d);
        favdrinks.add(new Drink(drinkDb.getAllFavourites(drinkDb.getWritableDatabase()).get(0)));
    }



}

package com.drinkingTeam.drinkingProject;

import android.content.Context;
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
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private List<Drink> drinks = new ArrayList<>();
    private List<Drink> favdrinks = new ArrayList<>();
    private final static String HOST = "192.168.0.38:8080";
    private MyListAdapter adapter;
    private MyListAdapter adapter2;
    private ListView listView;
    private final static String TAG = "drinks";
    private TextView err;
    private DrinksDbHelper drinkdb;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    drinks = getDrinks();
                    System.out.println(drinks.size());
                    if(drinks.size() == 0){
                        error(R.string.no_connection);
                    }else {
                        noError();
                        adapter.setDrinkList(drinks);
                        listView.setAdapter(adapter);
                    }

                    return true;

                case R.id.navigation_dashboard:
                    if(favdrinks.size() == 0) {
                        error(R.string.no_favourites);
                    }
                    else {
                        noError();
                        adapter2.setDrinkList(favdrinks);
                        listView.setAdapter(adapter2);
                    }
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
        drinkdb = new DrinksDbHelper(this);


        adapter2 = new MyListAdapter(this, R.layout.my_custom_list, favdrinks);
        err = findViewById(R.id.error_msg);
        listView = (ListView) findViewById(R.id.bubu);
        err = (TextView) this.findViewById(R.id.error_msg);
        noError();
        adapter = new MyListAdapter(this, R.layout.my_custom_list, drinks);
        drinks = getDrinks();


    }

    public List<Drink> getDrinks() {
        drinksFromJson(this);
        return drinks;
    }

    private void drinksFromJson(final Context context) {
        final RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = "http://" + HOST +"/get/drinks";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            drinks.clear();
                            System.out.println(response.getJSONArray("drinks"));
                            JSONArray  jsonArray = response.getJSONArray("drinks");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                Drink drink = new Drink();
                                drink.setDescription(json.getString("description"));
                                drink.setId(json.getLong("id"));
                                drink.setName(json.getString("name"));
                                drink.setGlass(json.getString("glass"));
                                drink.setImage(json.getString("image"));
                                drink.setRecipe(json.getString("recipe"));
                                JSONArray ingredientsArray = json.getJSONArray("ingredients");
                                List<Ingredient> ingredients = new ArrayList<>();
                                System.out.println(ingredientsArray.length());
                                for (int j = 0; j < ingredientsArray.length(); j++) {

                                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(j);
                                    Ingredient ingredient = new Ingredient();
                                    ingredient.setId(jsonIngredient.getLong("id"));
                                    ingredient.setDrinkId(json.getLong("id"));
                                    ingredient.setName(jsonIngredient.getString("name"));
                                    ingredient.setQuantity(jsonIngredient.getString("quantity"));
                                    ingredient.setUnits(jsonIngredient.getString("units"));
                                    ingredients.add(ingredient);
                                }
                                drink.setIngredients(ingredients);
                                addToDrinks(drink);
                            }
                            adapter.setDrinkList(drinks);
                            listView.setAdapter(adapter);
                            tempAddTofavs(drinks.get(0));
                            if(drinks.size() == 0) {
                                error(R.string.no_connection);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mQueue.cancelAll(TAG);
                error(R.string.no_connection);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Nasze tajne haslo");
                return headers;
            }
        };
        request.setTag(TAG);
        request.setShouldRetryServerErrors(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10, 1, 2));
        mQueue.add(request);
    }

    private void addToDrinks(Drink d){
        drinks.add(d);
    }

    private void error(int e) {
        err.setText(e);
        listView.setVisibility(View.INVISIBLE);
        err.setVisibility(View.VISIBLE);
    }

    private void noError() {
        listView.setVisibility(View.VISIBLE);
        err.setVisibility(View.INVISIBLE);
    }

    private void tempAddTofavs(Drink drink) {
        favdrinks.clear();
        DrinkEntity d = new DrinkEntity(drink);
        drinkdb.addToFavourites(drinkdb.getWritableDatabase(),d);
        favdrinks.add(new Drink(drinkdb.getAllFavourites(drinkdb.getWritableDatabase()).get(0)));
    }


}

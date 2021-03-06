package com.drinkingTeam.drinkingProject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
import com.drinkingTeam.drinkingProject.R;
import com.drinkingTeam.drinkingProject.activities.listAdapters.MyListAdapter;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.drinkingTeam.drinkingProject.tables.IngredientsReaderContract;
import com.drinkingTeam.drinkingProject.tables.UserDbHelper;
import com.drinkingTeam.drinkingProject.types.Drink;
import com.drinkingTeam.drinkingProject.types.Ingredient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drinkingTeam.Singleton.GET_DRINKS;
import static com.drinkingTeam.Singleton.GET_DRINKS_REQUEST_TAG;
import static com.drinkingTeam.Singleton.HOST;
import static com.drinkingTeam.Singleton.UPDATE_FAVOURITES;
import static com.drinkingTeam.Singleton.UPDATE_FAVOURITES_REQUEST_TAG;
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
    private static UserDbHelper userDb;
    private Context cxt;
    private static RequestQueue mQueue;
    private boolean back;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    drinks = getDrinks();
                    if(drinks.size() == 0){
                        error(cxt,R.string.no_connection);
                    }else {
                    adapter.setDrinkList(drinks);
                    listView.setAdapter(adapter);
                    }
                    return true;

                case R.id.navigation_dashboard:
                    favdrinks = drinkDb.getAllFavourites(drinkDb.getReadableDatabase());
                    if(favdrinks.size() == 0) {
                        error(cxt, R.string.no_favourites);
                    }
                    adapter2.setDrinkList(favdrinks);
                    listView.setAdapter(adapter2);
                    return true;
            }
            return false;
        }
    };

    /**
     * sets activity_main layout
     * finds navigation(bottom)
     * creates an action bar button (logout)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        cxt = this;
        mQueue = Volley.newRequestQueue(cxt);
        drinkDb = new DrinksDbHelper(this);
        favdrinks = drinkDb.getAllFavourites(drinkDb.getReadableDatabase());
        userDb = new UserDbHelper(this);
        adapter2 = new MyListAdapter(this, R.layout.my_custom_list, favdrinks,favdrinks,true);
        listView = (ListView) findViewById(R.id.bubu);
        adapter = new MyListAdapter(this, R.layout.my_custom_list, drinks,favdrinks,false);
        drinks = getDrinks();
        back = getIntent().getBooleanExtra("backScreen",false);
        if(back) navView.setSelectedItemId(R.id.navigation_dashboard);
    }

    /**
     * inflates menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *  handle button activities
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * gets all the drinks
     * @return list of drinks
     */
    public List<Drink> getDrinks() {
        drinksFromJson(this);
        return drinks;
    }

    /**
     * loads drinks from json
     * @param context
     */
    private void drinksFromJson(final Context context) {
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
                                drink.setId(json.getLong("id"));
                                drink.setName(json.getString(COLUMN_NAME_NAME));
                                drink.setGlass(json.getString(COLUMN_NAME_GLASS));
                                drink.setImage(json.getString(COLUMN_NAME_IMAGE));
                                drink.setRecipe(json.getString(COLUMN_NAME_RECIPE));
                                JSONArray ingredientsArray = json.getJSONArray(IngredientsReaderContract.IngredientsTable.TABLE_NAME);
                                List<Ingredient> ingredients = new ArrayList<>();
                                for (int j = 0; j < ingredientsArray.length(); j++) {
                                    JSONObject jsonIngredient = ingredientsArray.getJSONObject(j);
                                    Ingredient ingredient = new Ingredient();
                                    ingredient.setId(jsonIngredient.getLong("id"));
                                    ingredient.setName(jsonIngredient.getString(IngredientsReaderContract.IngredientsTable.COLUMN_NAME_NAME));
                                    ingredient.setQuantity(jsonIngredient.getString(COLUMN_NAME_QUANTITY));
                                    ingredient.setUnits(jsonIngredient.getString(COLUMN_NAME_UNITS));
                                    ingredients.add(ingredient);
                                }
                                drink.setIngredients(ingredients);
                                addToDrinks(drink);
                            }
                            adapter.setDrinkList(drinks);

                            if(!back) listView.setAdapter(adapter);
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
        request.setRetryPolicy(new DefaultRetryPolicy(100, 1, 2));
        mQueue.add(request);
    }

    /**
     * adds loeded drinks to list to be displayed
     * @param d
     */
    private void addToDrinks(Drink d){
        drinks.add(d);
    }

    /**
     * handles logout
     * removes from phone's database
     */
    private void logout() {
        userDb.removeUser(userDb.getWritableDatabase());
        Intent loginDisplay = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginDisplay);
    }

    public static void favourites_update(final List<Drink> favdrinks) {
        String url = HOST + UPDATE_FAVOURITES;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray stopShouting = response.getJSONArray("favs");
                            System.out.println(stopShouting);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mQueue.cancelAll(UPDATE_FAVOURITES_REQUEST_TAG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mQueue.cancelAll(UPDATE_FAVOURITES_REQUEST_TAG);
            }
        }) {
            @Override
            public byte[] getBody() {
                List<Long> favIds = new ArrayList<>();
                for (Drink d: favdrinks) favIds.add(d.getId());
                String your_string_json = "{ \"favourites\": " + favIds + "," +
                        "\"username\": \"" + userDb.getUser(userDb.getReadableDatabase()).get(0).getUsername() + "\"}";
                return your_string_json.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", VERY_SECRET_PASSWORD);
                return headers;
            }
        };
        request.setTag(UPDATE_FAVOURITES_REQUEST_TAG);
        request.setShouldRetryServerErrors(false);
        request.setRetryPolicy(new DefaultRetryPolicy(1000, 1, 2));
        mQueue.add(request);
    }
}

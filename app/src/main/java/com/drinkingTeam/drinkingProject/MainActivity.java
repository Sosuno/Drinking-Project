package com.drinkingTeam.drinkingProject;

import android.content.Context;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ConstraintLayout home_layout;
    private List<Drink> drinks = new ArrayList<>();
    private final static String HOST = "192.168.0.38:8080";
    ListView listView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    //home_layout = findViewById(R.id.home_layout);
                    home_layout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //BottomNavigationView navView = findViewById(R.id.bottom);
        mTextMessage = findViewById(R.id.message);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listView = (ListView) findViewById(R.id.bubu);


        drinks = getDrinks(this);
    }

    public List<Drink> getDrinks(Context context) {
        drinksFromJson(context);
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
                            MyListAdapter adapter = new MyListAdapter(context, R.layout.my_custom_list, drinks);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Nasze tajne haslo");
                return headers;
            }
        };
        mQueue.add(request);
    }

    private void addToDrinks(Drink d){
        drinks.add(d);
    }
}

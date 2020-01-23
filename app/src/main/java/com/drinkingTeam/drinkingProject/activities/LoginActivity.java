package com.drinkingTeam.drinkingProject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drinkingTeam.drinkingProject.types.Drink;
import com.drinkingTeam.drinkingProject.types.Ingredient;
import com.drinkingTeam.drinkingProject.R;
import com.drinkingTeam.drinkingProject.entities.UserEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.drinkingTeam.drinkingProject.tables.IngredientsReaderContract;
import com.drinkingTeam.drinkingProject.tables.UserDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drinkingTeam.Singleton.HOST;
import static com.drinkingTeam.Singleton.LOGIN;
import static com.drinkingTeam.Singleton.LOGIN_REQUEST_TAG;
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
import static com.drinkingTeam.drinkingProject.tables.UserReaderContract.UserTable.COLUMN_NAME_EMAIL;


public class LoginActivity extends AppCompatActivity {

    private UserDbHelper userDbHelper;
    private DrinksDbHelper drinksDbHelper;
    private boolean loginTouched = false;
    private boolean passwordTouched = false;
    private EditText enterUsernameEditText;
    private EditText enterPasswordEditView;
    private Context cxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cxt = this;
        userDbHelper = new UserDbHelper(cxt);
        drinksDbHelper = new DrinksDbHelper(cxt);
        final List<UserEntity> user = userDbHelper.getUser(userDbHelper.getReadableDatabase());

        if(user.size() > 0) {
           Intent drinksDisplay = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(drinksDisplay);
        }else {
            drinksDbHelper.newUser(drinksDbHelper.getWritableDatabase());
        }
        setContentView(R.layout.login);
        setUpTextListeners();

        Button login = findViewById(R.id.buttonLogin);
        Button register = findViewById(R.id.buttonRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = enterUsernameEditText.getText().toString();
                String password = enterPasswordEditView.getText().toString();
                contactServer(username,password);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registration);
            }
        });
    }

    private void contactServer(final String login, final String password){
        if(login.equals("") || password.equals("")) {
            error(cxt,R.string.login_err_clean);
            return;
        }

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = HOST + LOGIN;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(TABLE_NAME);
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
                                drinksDbHelper.addToFavourites(drinksDbHelper.getWritableDatabase(),drink);
                            }
                            String email = response.getString(COLUMN_NAME_EMAIL);
                            userDbHelper.addUser(userDbHelper.getWritableDatabase(),new UserEntity(null,login,password,email));
                            mQueue.cancelAll(LOGIN_REQUEST_TAG);
                            Intent drinksDisplay = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(drinksDisplay);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();


                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    error(cxt,jsonError);
                }
                mQueue.cancelAll(LOGIN_REQUEST_TAG);
            }
        })
        {
            @Override
            public byte[] getBody() {
                String your_string_json = "{ \"username\": \"" + login + "\", " +
                        "\"password\": \"" + password + "\"}";
                return your_string_json.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", VERY_SECRET_PASSWORD);
                return headers;
            }
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };

        request.setTag(LOGIN_REQUEST_TAG);
        request.setShouldRetryServerErrors(false);
        request.setRetryPolicy(new DefaultRetryPolicy(1000, 1, 2));
        mQueue.add(request);
    }

    private void setUpTextListeners() {
        enterUsernameEditText = findViewById(R.id.usernameLoginEditText);
        final TextView enterUsernameTextView = findViewById(R.id.usernameLoginTextView);
        enterUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    enterUsernameTextView.setHint("");
                    loginTouched = true;
                }
                else if(!loginTouched || enterUsernameEditText.getText().toString().equals("")) enterUsernameTextView.setHint(R.string.enter_username);

            }
        });

        enterPasswordEditView = findViewById(R.id.passwordLoginEditText);
        final TextView enterPasswordTextView = findViewById(R.id.passwordLoginLoginTextView);
        enterPasswordEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    enterPasswordTextView.setHint("");
                    passwordTouched = true;
                }
                else if(!passwordTouched || enterPasswordEditView.getText().toString().equals("")) enterPasswordTextView.setHint(R.string.enter_password);

            }
        });
    }
}

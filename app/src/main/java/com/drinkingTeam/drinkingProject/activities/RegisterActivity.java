package com.drinkingTeam.drinkingProject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
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
import com.drinkingTeam.drinkingProject.R;
import com.drinkingTeam.drinkingProject.entities.UserEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.drinkingTeam.drinkingProject.tables.UserDbHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.drinkingTeam.Singleton.HOST;
import static com.drinkingTeam.Singleton.REGISTER;
import static com.drinkingTeam.Singleton.REGISTER_REQUEST_TAG;
import static com.drinkingTeam.Singleton.VERY_SECRET_PASSWORD;
import static com.drinkingTeam.Singleton.error;

public class RegisterActivity extends AppCompatActivity {

    private UserDbHelper userDbHelper;
    private DrinksDbHelper drinksDb;
    private EditText enterUsernameEditText;
    private EditText enterEmailEditText;
    private EditText enterPasswordEditText;
    private EditText reEnterPasswordEditText;
    private Context cxt;

    /**
     *  Sets listeners for register buttons
     *  sets login & register layouts
     *  finds all elements in layout
     *  checks all inputs
     *  calls register function
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cxt = this;
        userDbHelper = new UserDbHelper(this);
        setContentView(R.layout.register);
        enterPasswordEditText = findViewById(R.id.passwordEditText);
        reEnterPasswordEditText = findViewById(R.id.password2EditText);
        enterEmailEditText = findViewById(R.id.emailEditText);
        enterUsernameEditText = findViewById(R.id.usernameEditText);
        Button register = findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = enterUsernameEditText.getText().toString();
                String password = enterPasswordEditText.getText().toString();
                String email = enterEmailEditText.getText().toString();
                String repeatedPassword = reEnterPasswordEditText.getText().toString();
                if(username.equals("") || password.equals("") || email.equals("") || repeatedPassword.equals("")) {
                    error(cxt, R.string.empty_fields);
                    return;
                }
                if(!password.equals(repeatedPassword)) {
                    error(cxt,R.string.password_no_match);
                    enterPasswordEditText.setText("");
                    reEnterPasswordEditText.setText("");
                    return;
                }
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                if(!pattern.matcher(email).matches()){
                    error(cxt,R.string.email_not_valid);
                    return;
                }
                register(username,password,email);
            }
        });

    }

    /**
     * adds user to database & automatically logs in
     * @param username
     * @param password
     * @param email
     */
    private void register(final String username,final String password,final String email) {
        final RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = HOST + REGISTER;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userDbHelper.addUser(userDbHelper.getWritableDatabase(),new UserEntity(null,username,password,email));
                        drinksDb = new DrinksDbHelper(cxt);
                        drinksDb.newUser(drinksDb.getWritableDatabase());
                        mQueue.cancelAll(REGISTER_REQUEST_TAG);
                        Intent drinksDisplay = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(drinksDisplay);
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
                mQueue.cancelAll(REGISTER_REQUEST_TAG);
            }
        })
        {
            @Override
            public byte[] getBody() {
                String your_string_json = "{ \"username\": \"" + username + "\", " +
                        "\"password\": \"" + password + "\"," +
                        "\"email\": \"" + email + "\"}";
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

        request.setTag(REGISTER_REQUEST_TAG);
        request.setShouldRetryServerErrors(false);
        request.setRetryPolicy(new DefaultRetryPolicy(100, 1, 2));
        mQueue.add(request);
    }
}

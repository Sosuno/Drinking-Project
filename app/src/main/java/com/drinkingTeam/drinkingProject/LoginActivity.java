package com.drinkingTeam.drinkingProject;

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
import com.drinkingTeam.drinkingProject.entities.UserEntity;
import com.drinkingTeam.drinkingProject.tables.UserDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drinkingTeam.Singleton.HOST;
import static com.drinkingTeam.Singleton.LOGIN;
import static com.drinkingTeam.Singleton.LOGIN_REQUEST_TAG;
import static com.drinkingTeam.Singleton.VERY_SECRET_PASSWORD;
import static com.drinkingTeam.Singleton.error;
import static com.drinkingTeam.drinkingProject.tables.UserReaderContract.UserTable.COLUMN_NAME_EMAIL;


public class LoginActivity extends AppCompatActivity {

    private UserDbHelper userDbHelper;
    private boolean loginTouched = false;
    private boolean passwordTouched = false;
    private EditText enterUsernameEditText;
    private EditText enterPasswordEditView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userDbHelper = new UserDbHelper(this);
        final List<UserEntity> user = userDbHelper.getUser(userDbHelper.getReadableDatabase());

        if(user.size() > 0) {
            Intent drinksDisplay = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(drinksDisplay);
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
                setContentView(R.layout.register);
            }
        });
    }

    private void contactServer(final String login, final String password){
        if(login.equals("") || password.equals("")) {
            error(getApplicationContext(),R.string.login_err_clean);
            return;
        }

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = HOST + LOGIN;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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
                    error(getApplicationContext(),jsonError);
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
        request.setRetryPolicy(new DefaultRetryPolicy(100, 1, 2));
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

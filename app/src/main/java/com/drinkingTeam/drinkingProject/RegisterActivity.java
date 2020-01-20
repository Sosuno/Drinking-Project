package com.drinkingTeam.drinkingProject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.drinkingTeam.drinkingProject.entities.UserEntity;
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
    private boolean loginTouched = false;
    private boolean passwordTouched = false;
    private boolean repeatPasswordTouched = false;
    private boolean emailTouched = false;
    private EditText enterUsernameEditText;
    private EditText enterEmailEditText;
    private EditText enterPasswordEditText;
    private EditText reEnterPasswordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userDbHelper = new UserDbHelper(this);
        setContentView(R.layout.register);
        setUpTextListeners();
        Button register = findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = enterUsernameEditText.getText().toString();
                String password = enterPasswordEditText.getText().toString();
                String email = enterEmailEditText.getText().toString();
                String repeatedPassword = reEnterPasswordEditText.getText().toString();
                if(username.equals("") || password.equals("") || email.equals("") || repeatedPassword.equals("")) {
                    error(getApplicationContext(), R.string.empty_fields);
                    return;
                }
                if(!password.equals(repeatedPassword)) {
                    error(getApplicationContext(),R.string.password_no_match);
                    enterPasswordEditText.setText("");
                    reEnterPasswordEditText.setText("");
                    return;
                }
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                if(!pattern.matcher(email).matches()){
                    error(getApplicationContext(),R.string.email_not_valid);
                    return;
                }
                register(username,password,email);
            }
        });

    }

    private void register(final String username,final String password,final String email) {
        final RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = HOST + REGISTER;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userDbHelper.addUser(userDbHelper.getWritableDatabase(),new UserEntity(null,username,password,email));
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
                    error(getApplicationContext(),jsonError);
                }
                mQueue.cancelAll(REGISTER_REQUEST_TAG);
            }
        })
        {
            @Override
            public byte[] getBody() {
                String your_string_json = "{ \"username\": \"" + username + "\", " +
                        "\"password\": \"" + password + "\"" +
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

    private void setUpTextListeners() {
        enterUsernameEditText = findViewById(R.id.usernameEditText);
        final TextView enterUsernameTextView = findViewById(R.id.usernameTextView);
        enterUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    enterUsernameTextView.setHint("");
                    loginTouched = true;
                }
                else if(!loginTouched || enterUsernameEditText.getText().toString().equals("")) enterUsernameTextView.setHint(R.string.enter_username);

            }
        });

        enterEmailEditText = findViewById(R.id.emailEditText);
        final TextView enterEmailTextView = findViewById(R.id.emailTextView);
        enterEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    enterEmailTextView.setHint("");
                    emailTouched = true;
                }
                else if(!emailTouched || enterEmailEditText.getText().toString().equals("")) enterEmailTextView.setHint(R.string.enter_password);

            }
        });

        enterPasswordEditText = findViewById(R.id.passwordEditText);
        final TextView enterPasswordTextView = findViewById(R.id.passwordTextView);
        enterPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    enterPasswordTextView.setHint("");
                    passwordTouched = true;
                }
                else if(!passwordTouched || enterPasswordEditText.getText().toString().equals("")) enterPasswordTextView.setHint(R.string.enter_password);

            }
        });

        reEnterPasswordEditText = findViewById(R.id.password2EditText);
        final TextView reEnterPasswordTextView = findViewById(R.id.password2TextView);
        reEnterPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    reEnterPasswordTextView.setHint("");
                    repeatPasswordTouched = true;
                }
                else if(!repeatPasswordTouched || reEnterPasswordEditText.getText().toString().equals("")) reEnterPasswordTextView.setHint(R.string.enter_password_2);

            }
        });
    }
}

package com.drinkingTeam;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.drinkingTeam.drinkingProject.R;

public final class Singleton {

    private static final Singleton INSTANCE = new Singleton();
    public static final String HOST = "http://192.168.0.38:8080";
    public static final String GET_DRINKS = "/get/drinks";
    public static final String LOGIN = "/user/token";
    public static final String REGISTER = "/user/register";
    public static final String LOGIN_REQUEST_TAG = "no more entry";
    public static final String GET_DRINKS_REQUEST_TAG = "too drunk";
    public static final String REGISTER_REQUEST_TAG = "wanna get drunk?";
    public static final String VERY_SECRET_PASSWORD = "Nasze tajne haslo";

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }

    public static void error(Context cxt, int e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setMessage(e);
        builder.setPositiveButton(R.string.confirm_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void error(Context cxt,String e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setMessage(e);
        builder.setPositiveButton(R.string.confirm_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

package com.drinkingTeam;


public final class Singleton {

    private static final Singleton INSTANCE = new Singleton();
    public static final String HOST = "192.168.0.38:8080";
    public static final String GET_DRINKS = "/get/drinks";
    public static final String LOGIN = "/user/token";
    public static final String LOGIN_REQUEST_TAG = "no more entry";
    public static final String GET_DRINKS_REQUEST_TAG = "too drunk";

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }
}

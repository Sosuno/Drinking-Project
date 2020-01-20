package com.drinkingTeam.drinkingProject.tables;

import android.provider.BaseColumns;

public class UserReaderContract {
    private UserReaderContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}

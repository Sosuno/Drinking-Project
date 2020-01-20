package com.drinkingTeam.drinkingProject.tables;

import android.provider.BaseColumns;

public class UserReaderContract {
    private UserReaderContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_NAME = "username";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_RECIPE = "recipe";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_GLASS = "glass";
    }
}

package com.drinkingTeam.drinkingProject.tables;

import android.provider.BaseColumns;

import java.sql.Blob;

public final class DrinksReaderContract {
    private DrinksReaderContract() {}

    public static class DrinksTable implements BaseColumns {
        public static final String TABLE_NAME = "drinks";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_RECIPE = "recipe";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}


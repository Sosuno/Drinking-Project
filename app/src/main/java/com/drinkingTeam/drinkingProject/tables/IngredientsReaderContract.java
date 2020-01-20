package com.drinkingTeam.drinkingProject.tables;

import android.provider.BaseColumns;

public class IngredientsReaderContract {
    private IngredientsReaderContract() {}

    public static class IngredientsTable implements BaseColumns {
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_UNITS = "units";
        public static final String COLUMN_NAME_DRINK_ID = "drink_Id";

    }
}
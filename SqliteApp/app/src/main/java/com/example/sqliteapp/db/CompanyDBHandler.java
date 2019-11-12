package com.example.sqliteapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompanyDBHandler extends SQLiteOpenHelper {

    private static final String DATABAS_NAME = "companies.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COMPANIES = "companies";
    public static final String COLUMN_ID = "compId";
    public static final String COLUMN_NAME = "compName";
    public static final String COLUMN_WEB = "compWebPage";
    public static final String COLUMN_NUMBER = "compNumber";
    public static final String COLUMN_EMAIL = "compEmail";
    public static final String COLUMN_PRODUCTS = "compProducts";
    public static final String COLUMN_SERVICES = "compServices";
    public static final String COLUMN_CLASS = "compClassification";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_COMPANIES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_WEB + " TEXT, " +
            COLUMN_NUMBER + " NUMERIC," +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PRODUCTS + " TEXT, " +
            COLUMN_SERVICES + " TEXT, " +
            COLUMN_CLASS + " TEXT " + ")";

    public CompanyDBHandler(Context context) {
        super(context, DATABAS_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
        db.execSQL(TABLE_CREATE);
    }
}

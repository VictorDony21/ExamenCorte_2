package com.example.examencorte_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sistema.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "productos";
    public static final String COLUMN_ID = "codigo";
    public static final String COLUMN_PRODUCTO = "nombre_producto";
    public static final String COLUMN_MARCA = "marca";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_PERECEDERO = "perecedero";

    // Sentencia SQL para crear la tabla
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_PRODUCTO + " TEXT," +
                    COLUMN_MARCA + " TEXT," +
                    COLUMN_PRECIO + " REAL," +
                    COLUMN_PERECEDERO + " INTEGER)";

    // Constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
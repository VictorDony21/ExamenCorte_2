package com.example.examencorte_2;

import android.content.ContentValues;
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

    public void agregarProducto(int codigo, String nombre, String marca, double precio, boolean perecedero) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, codigo);
        values.put(COLUMN_PRODUCTO, nombre);
        values.put(COLUMN_MARCA, marca);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_PERECEDERO, perecedero);
        long resultado = db.insert(TABLE_NAME, null, values);
        if (resultado == -1) {
            // Error al insertar
        }
        db.close();
    }

    public void actualizarProductoEnBD(Productos producto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTO, producto.getNombre());
        values.put(COLUMN_MARCA, producto.getMarca());
        values.put(COLUMN_PRECIO, producto.getPrecio());
        values.put(COLUMN_PERECEDERO, producto.getPerecedero() ? 1 : 0);

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(producto.getCodigo())};

        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void borrarProductoDeBD(int codigo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(codigo)};

        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }

}
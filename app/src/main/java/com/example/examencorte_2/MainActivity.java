package com.example.examencorte_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager dbHelper;
    EditText etCodigo, etNombre, etMarca, etPrecio;
    RadioGroup radioGroup;
    RadioButton rbPerecedero, rbNoPerecedero;
    Button btnGuardar, btnLimpiar, btnNuevo, btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseManager(this);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombreProducto);
        etMarca = findViewById(R.id.etMarca);
        etPrecio = findViewById(R.id.etPrecio);

        radioGroup = findViewById(R.id.radioGroup);
        rbPerecedero = findViewById(R.id.rbPerecedero);
        rbNoPerecedero = findViewById(R.id.rbNoPerecedero);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnEditar = findViewById(R.id.btnEditar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarProducto();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaEditar();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });


    }

    private void guardarProducto() {
        String codigoStr = etCodigo.getText().toString().trim();
        String nombreProducto = etNombre.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        boolean esPerecedero = radioGroup.getCheckedRadioButtonId() == R.id.rbPerecedero;

        if (!codigoStr.isEmpty() && !nombreProducto.isEmpty()) {
            try {
                int codigo = Integer.parseInt(codigoStr);

                double precio = 0.0;
                if (!precioStr.isEmpty()) {
                    precio = Double.parseDouble(precioStr);
                }

                // Crear un nuevo objeto Producto con los datos ingresados
                Productos producto = new Productos();
                producto.setCodigo(codigo);
                producto.setNombre(nombreProducto);
                producto.setMarca(marca);
                producto.setPrecio(precio);
                producto.setPerecedero(esPerecedero);

                // Insertar o actualizar el producto en la base de datos
                guardarProductoEnBD(producto);
                dbHelper.agregarProducto(codigo, nombreProducto, marca, precio, esPerecedero);

                Toast.makeText(this, "Producto guardado.", Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al ingresar el código o el precio.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El código y el nombre del producto son requeridos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarProductoEnBD(Productos producto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_PRODUCTO, producto.getNombre());
        values.put(DatabaseManager.COLUMN_MARCA, producto.getMarca());
        values.put(DatabaseManager.COLUMN_PRECIO, producto.getPrecio());
        values.put(DatabaseManager.COLUMN_PERECEDERO, producto.getPerecedero() ? 1 : 0);

        if (producto.getCodigo() > 0) {
            // Si el producto ya tiene un código mayor a 0, se trata de una edición
            db.update(DatabaseManager.TABLE_NAME, values,
                    DatabaseManager.COLUMN_ID + "=?",
                    new String[]{String.valueOf(producto.getCodigo())});
        } else {
            // Si el código del producto es 0 o menor, se trata de una inserción
            long nuevoCodigo = db.insert(DatabaseManager.TABLE_NAME, null, values);
            producto.setCodigo((int) nuevoCodigo);
        }

        db.close();
    }


    private void ventanaEditar() {

        Intent intent = new Intent(this, buscarActivity.class);
        startActivity(intent);

    }

    private void limpiarCampos() {

        etCodigo.setText("");
        etNombre.setText("");
        etMarca.setText("");
        etPrecio.setText("");
        radioGroup.clearCheck();

    }

}
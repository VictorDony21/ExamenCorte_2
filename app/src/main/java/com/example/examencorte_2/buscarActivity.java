package com.example.examencorte_2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class buscarActivity extends AppCompatActivity {

    private DatabaseManager dbHelper;
    EditText etCodigo, etNombre, etMarca, etPrecio;
    RadioGroup radioGroup;
    RadioButton rbPerecedero, rbNoPerecedero;
    Button btnBuscar, btnBorrar, btnActualizar, btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        dbHelper = new DatabaseManager(this);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombreProducto);
        etMarca = findViewById(R.id.etMarca);
        etPrecio = findViewById(R.id.etPrecio);

        radioGroup = findViewById(R.id.radioGroup);
        rbPerecedero = findViewById(R.id.rbPerecedero);
        rbNoPerecedero = findViewById(R.id.rbNoPerecedero);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCerrar = findViewById(R.id.btnCerrar);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProductoPorCodigo();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarProducto();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarVentana();
            }
        });
    }

    private void buscarProductoPorCodigo() {
        String codigo = etCodigo.getText().toString().trim();

        Log.d("BuscarActivity", "Código ingresado: " + codigo);


        if (!codigo.isEmpty()) {
            int codigo2 = Integer.parseInt(codigo);
            Log.d("BuscarActivity", "Código convertido a entero: " + codigo2);


            // Realizar consulta a la base de datos para obtener el producto por su código
            Productos producto = obtenerProductoPorCodigo(codigo2);

            if (producto != null) {
                // Mostrar la información del producto encontrado
                etNombre.setText(producto.getNombre());
                etMarca.setText(producto.getMarca());
                etPrecio.setText(String.valueOf(producto.getPrecio()));
                radioGroup.check(producto.getPerecedero() ? R.id.rbPerecedero : R.id.rbNoPerecedero);
            } else {
                Toast.makeText(this, "No se encontró ningún producto con el código ingresado.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingresa un código para buscar el producto.", Toast.LENGTH_SHORT).show();
        }
    }

    private Productos obtenerProductoPorCodigo(int codigo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseManager.COLUMN_ID,
                DatabaseManager.COLUMN_PRODUCTO,
                DatabaseManager.COLUMN_MARCA,
                DatabaseManager.COLUMN_PRECIO,
                DatabaseManager.COLUMN_PERECEDERO
        };

        String selection = DatabaseManager.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(codigo)};

        Cursor cursor = db.query(
                DatabaseManager.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Productos producto = null;

        if (cursor.moveToFirst()) {
            // Imprimir información en el logcat para verificar los valores obtenidos
            Log.d("BuscarActivity", "Producto encontrado con el código: " + codigo);
            // ...

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID));
            String nombreProducto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRODUCTO));
            String marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_MARCA));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRECIO));
            int esPerecederoInt = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PERECEDERO));
            boolean esPerecedero = esPerecederoInt == 1;

            producto = new Productos();
            producto.setCodigo(id);
            producto.setNombre(nombreProducto);
            producto.setMarca(marca);
            producto.setPrecio(precio);
            producto.setPerecedero(esPerecedero);
        } else {
            // Producto no encontrado, imprimir un mensaje en el logcat
            Log.d("BuscarActivity", "No se encontró ningún producto con el código: " + codigo);
        }

        cursor.close();
        db.close();
        return producto;
    }


    private void actualizarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        String nombreProducto = etNombre.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        boolean esPerecedero = radioGroup.getCheckedRadioButtonId() == R.id.rbPerecedero;

        if (!codigo.isEmpty() && !nombreProducto.isEmpty()) {
            int codigo2 = Integer.parseInt(codigo);
            double precio2 = Double.parseDouble(precio);

            Productos producto = new Productos();
            producto.setCodigo(codigo2);
            producto.setNombre(nombreProducto);
            producto.setMarca(marca);
            producto.setPrecio(precio2);
            producto.setPerecedero(esPerecedero);

            dbHelper.actualizarProductoEnBD(producto);
            limpiarCampos();
        } else {
            Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void borrarProducto() {
        String codigo = etCodigo.getText().toString().trim();

        if (!codigo.isEmpty()) {
            int codigo2 = Integer.parseInt(codigo);
            dbHelper.borrarProductoDeBD(codigo2);
            limpiarCampos();
        } else {
            Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void cerrarVentana() {
        finish();
    }

    private void limpiarCampos() {
        etCodigo.setText("");
        etNombre.setText("");
        etMarca.setText("");
        etPrecio.setText("");
        radioGroup.clearCheck();
    }
}
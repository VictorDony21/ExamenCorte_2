package com.example.examencorte_2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    // Declaración de vistas
    EditText etCodigo, etNombre, etMarca, etPrecio;
    RadioGroup radioGroup;
    RadioButton rbPerecedero, rbNoPerecedero;
    Button btnGuardar, btnLimpiar, btnNuevo, btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
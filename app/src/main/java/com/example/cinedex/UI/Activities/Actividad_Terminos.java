package com.example.cinedex.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinedex.R;

public class Actividad_Terminos extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_terminos);

        TextView tvTerminos = findViewById(R.id.tvTerminos);
        Button btnAceptar = findViewById(R.id.btnAceptarTerminos);

        // Carga tus T&C largos aquí (texto o HTML)
        tvTerminos.setText(getString(R.string.terminos_placeholder));

        btnAceptar.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("CineDexPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("TERMINOS_ACEPTADOS", true).apply();
            // Volver al flow principal
            finish();
        });
    }
}


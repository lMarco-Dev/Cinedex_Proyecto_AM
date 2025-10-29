package com.example.cinedex.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinedex.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.logoSplash);
        TextView titulo = findViewById(R.id.tvAppName);

        // Cargar animaciones
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Mostrar el logo y texto con fade-in
        logo.startAnimation(fadeIn);
        titulo.startAnimation(fadeIn);

        // Esperar 3 segundos antes de pasar al login con transición elegante
        new Handler().postDelayed(() -> {
            logo.startAnimation(fadeOut);
            titulo.startAnimation(fadeOut);

            // Luego de la animación, pasamos al login
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, Actividad_Login.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }, 800); // espera que termine el fadeOut
        }, 2500); // duración total del splash antes del fade out
    }
}

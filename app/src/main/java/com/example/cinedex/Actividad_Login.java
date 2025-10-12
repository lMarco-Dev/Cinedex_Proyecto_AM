package com.example.cinedex;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Actividad_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_login);

        FrameLayout btnIniciarSesion = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        ImageView bgLogin = findViewById(R.id.bgLogin);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bgLogin.setRenderEffect(RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.CLAMP));
        } else {
            bgLogin.setAlpha(0.4f);
        }

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, Actividad_Registrarse.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnIniciarSesion.setOnClickListener(v-> {
            Intent intent = new Intent(this, Actividad_Principal.class);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();
        });
    }
}

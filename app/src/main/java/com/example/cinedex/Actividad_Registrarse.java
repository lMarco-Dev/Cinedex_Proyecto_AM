// Actividad_Registrarse.java
package com.example.cinedex;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Actividad_Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_registrarse);

        FrameLayout btnRegistrarse = findViewById(R.id.btnRegister);
        TextView btnRegresarLogin = findViewById(R.id.tvGoToLogin);
        ImageView bgRegister = findViewById(R.id.bgRegister);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bgRegister.setRenderEffect(RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.CLAMP));
        } else {
            bgRegister.setAlpha(0.4f);
        }

        btnRegistrarse.setOnClickListener(v-> {
            Intent intent = new Intent(this, Actividad_Login.class);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            startActivity(intent);
        });

        btnRegresarLogin.setOnClickListener(v-> {
            Intent intent = new Intent(this, Actividad_Login.class);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            startActivity(intent);
        });

    }
}

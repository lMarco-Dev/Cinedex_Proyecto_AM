package com.example.cinedex.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RenderEffect; // <- Nota: Esta importación solo funciona en API 31+
import android.graphics.Shader;      // <- Nota: Esta importación solo funciona en API 31+
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinedex.Data.Models.DTOs.UsuarioLoginDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Login extends AppCompatActivity {

    EditText etUsuario, etPassword;
    FrameLayout btnIniciarSesion;
    TextView txtIrARegistro;
    ImageView fondoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_login);

        // -- Conectar Vistas ---
        etUsuario = findViewById(R.id.campo_usuario);
        etPassword = findViewById(R.id.campo_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_ingresar);
        txtIrARegistro = findViewById(R.id.txtRegistrar);
        fondoLogin = findViewById(R.id.fondo_login);

        // -- Dirigir al registro --
        txtIrARegistro.setOnClickListener(v -> {
            Intent intent = new Intent(this, Actividad_Registrarse.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // -- Login --
        btnIniciarSesion.setOnClickListener(v-> {
            String username = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            //Hacemos la petición
            intentarLogin(username, password);
        });
    }

    //-- Metodo para comprar con el API REST ---
    private void intentarLogin(String username, String password) {
        //Creamos el DTO de Login
        UsuarioLoginDto loginDto = new UsuarioLoginDto(username, password);

        //Llamamos a la API
        CineDexApiService apiService = CineDexApiClient.getApiService();

        //Hacer la llamada
        apiService.loginUsuario(loginDto).enqueue(new Callback<UsuarioPublicoDto>() {
            @Override
            public void onResponse(Call<UsuarioPublicoDto> call, Response<UsuarioPublicoDto> response) {

                if(response.isSuccessful() && response.body() != null) {
                    //Si el login es exitoso
                    UsuarioPublicoDto usuarioLogueado = response.body();

                    // --- CORRECCIÓN 1: Renombrar la variable ---
                    //Guardar la sesión del usuario
                    SharedPreferences prefsSesion = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefsSesion.edit();

                    //Guardamos los datos clase del usuario
                    editor.putInt("ID_USUARIO", usuarioLogueado.getIdUsuario());
                    editor.putString("NOMBRE_USUARIO", usuarioLogueado.getNombreUsuario());
                    editor.putString("NOMBRES", usuarioLogueado.getNombres());
                    editor.putString("APELLIDOS", usuarioLogueado.getApellidos());
                    editor.putString("NOMBRE_RANGO", usuarioLogueado.getNombreRango());
                    editor.putBoolean("ESTA_LOGUEADO", true);
                    editor.apply();

                    //Enviamos al usuario a la actividad principal
                    Toast.makeText(Actividad_Login.this, "Bienvenido, " + usuarioLogueado.getNombreUsuario() + "!", Toast.LENGTH_SHORT).show();

                    // --- CORRECCIÓN 1 (Continuación): Usar un nombre diferente ---
                    SharedPreferences prefsTerminos = getSharedPreferences("CineDexPrefs", MODE_PRIVATE);
                    boolean acepto = prefsTerminos.getBoolean("TERMINOS_ACEPTADOS", false);

                    // --- CORRECCIÓN 2: Lógica de redirección arreglada ---
                    Intent intent;

                    if (!acepto) {
                        // Si no ha aceptado, lo mandamos a Términos
                        intent = new Intent(Actividad_Login.this, Actividad_Terminos.class);
                        startActivity(intent);
                        // No cerramos Login, para que pueda volver
                    } else {
                        // Si ya aceptó, lo mandamos a la Principal
                        intent = new Intent(Actividad_Login.this, Actividad_Principal.class);

                        // (Buena Práctica): Limpiamos la pila de actividades
                        // para que no pueda presionar "Atrás" y volver al Login
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish(); // Cerramos el Login
                    }

                    // --- CORRECCIÓN 2 (Continuación): Se eliminó el bloque de código duplicado que estaba aquí ---

                } else {
                    Log.e("[FALLO LOGIN]", "Código: " + response.code());
                    Toast.makeText(Actividad_Login.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioPublicoDto> call, Throwable t) {
                //Error de red
                Log.e("[LOGIN FALLO]", "Error de conexión: " + t.getMessage());
                Toast.makeText(Actividad_Login.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
// Archivo: UI/Activities/Actividad_Registrarse.java
package com.example.cinedex.UI.Activities;

import android.content.Context; // <-- ¡Asegúrate de importar Context!
import android.content.Intent; // <-- ¡Asegúrate de importar Intent!
import android.content.SharedPreferences; // <-- ¡Asegúrate de importar SharedPreferences!
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinedex.Data.Access.DAOUsuario;
import com.example.cinedex.Data.Models.Usuario;
import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioRegistroDto;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Registrarse extends AppCompatActivity {

    EditText etNombreUsuario, etNombres, etApellidos, etEmail, etPassword, etConfirmPassword;
    FrameLayout btnRegister;

    DAOUsuario dao;
    CineDexApiService apiService;

    TextView tvGoToLogin;

    /* ==================================================================
                        CONECTAMOS LAS CARAS Y EL BOTON
      ================================================================== */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_registrarse);

        dao = new DAOUsuario(this);
        apiService = CineDexApiClient.getApiService(); // <-- Inicializar aquí

        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, Actividad_Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // ------------ Cuando preciona el boton registrarse
        btnRegister.setOnClickListener(v -> {
            String username = etNombreUsuario.getText().toString().trim();
            String nombres = etNombres.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            // Validaciones
            if(username.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, llenar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)){
                etConfirmPassword.setError("No coinciden");
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Empaquetamos todos los datos
            UsuarioRegistroDto registroDto = new UsuarioRegistroDto(username, email, pass, nombres, apellidos);
            intentarRegistro(registroDto, pass); // <-- Pasamos el DTO y la contraseña
        });

    }

    /* ==================================================================
                        COMUNICACIÓN CON EL API
      ================================================================== */
    private void intentarRegistro(UsuarioRegistroDto registroDto, String contrasenaPlana) {

        /* -- Toma el paquete de registroDto */
        apiService.registrarUsuario(registroDto).enqueue(new Callback<UsuarioPublicoDto>() {

            /* ==================================================================
                                    CAMINO FELIZ
            ================================================================== */
            @Override
            public void onResponse(Call<UsuarioPublicoDto> call, Response<UsuarioPublicoDto> response) {

                if (response.isSuccessful() && response.body() != null) {

                    // ¡ÉXITO DE API!
                    UsuarioPublicoDto usuarioCreado = response.body();

                    // 1. Guardar en la BD local SQLite
                    new Thread(() -> {
                        // Llamamos a Insertar con los argumentos correctos
                        dao.Insertar(usuarioCreado, contrasenaPlana);
                    }).start();

                    // 2. GUARDAR LA SESIÓN EN SHAREDPREFERENCES
                    SharedPreferences prefs = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("ID_USUARIO", usuarioCreado.getIdUsuario());
                    editor.putString("NOMBRE_USUARIO", usuarioCreado.getNombreUsuario());
                    editor.putString("NOMBRES", usuarioCreado.getNombres());
                    editor.putString("APELLIDOS", usuarioCreado.getApellidos());
                    editor.putBoolean("ESTA_LOGUEADO", true);
                    editor.apply();

                    // 3. Enviar al usuario a la app
                    Toast.makeText(Actividad_Registrarse.this, "¡Registro exitoso! Iniciando sesión...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Actividad_Registrarse.this, Actividad_Principal.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else {
                    String errorMensaje = "Error desconocido al registrar.";
                    if (response.errorBody() != null) {
                        try {
                            errorMensaje = response.errorBody().string();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("API_REGISTRO_FALLO", "Código: " + response.code() + " | Mensaje: " + errorMensaje);
                    if (response.code() == 409) {
                        Toast.makeText(Actividad_Registrarse.this, "Usuario o correo ya existe.", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(Actividad_Registrarse.this, "Datos inválidos (Revise Logcat).", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Actividad_Registrarse.this, "Error en el servidor (Revise Logcat).", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioPublicoDto> call, Throwable t) {
                // (Tu código de manejo de fallos está bien)
                String falloMensaje = (t.getMessage() != null) ? t.getMessage() : "Error de conexión";
                Toast.makeText(Actividad_Registrarse.this, "No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
                Log.e("API_REGISTRO_FALLO", "Fallo de red: " + falloMensaje);
            }
        });
    }
}
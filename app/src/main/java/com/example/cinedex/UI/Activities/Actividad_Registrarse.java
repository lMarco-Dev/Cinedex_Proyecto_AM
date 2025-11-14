// Archivo: UI/Activities/Actividad_Registrarse.java
package com.example.cinedex.UI.Activities;

import android.content.Context; // <-- ¡Asegúrate de importar Context!
import android.content.Intent; // <-- ¡Asegúrate de importar Intent!
import android.content.SharedPreferences; // <-- ¡Asegúrate de importar SharedPreferences!
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    CineDexApiService apiService; // <-- Añadido para estar disponible en el método

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

        btnRegister.setOnClickListener(v -> {
            String username = etNombreUsuario.getText().toString().trim();
            String nombres = etNombres.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if(username.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, llenar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)){
                etConfirmPassword.setError("No coinciden");
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Asumiendo que tu DTO se llama 'UsuarioRegistroDto' y acepta estos campos
            UsuarioRegistroDto registroDto = new UsuarioRegistroDto(username, email, pass, nombres, apellidos);
            intentarRegistro(registroDto, pass); // <-- Pasamos el DTO y la contraseña
        });
    }

    // --- MÉTODO ACTUALIZADO ---
    private void intentarRegistro(UsuarioRegistroDto registroDto, String contrasenaPlana) {

        apiService.registrarUsuario(registroDto).enqueue(new Callback<UsuarioPublicoDto>() {

            @Override
            public void onResponse(Call<UsuarioPublicoDto> call, Response<UsuarioPublicoDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    // ¡ÉXITO DE API!
                    UsuarioPublicoDto usuarioCreado = response.body();

                    // --- ¡¡ESTA ES LA CORRECCIÓN!! ---
                    // 1. Guardar en la BD local (con el DTO y la contraseña)
                    new Thread(() -> {
                        // Llamamos a Insertar con los argumentos correctos
                        dao.Insertar(usuarioCreado, contrasenaPlana);
                    }).start();

                    // 2. ¡¡GUARDAR LA SESIÓN EN SHAREDPREFERENCES!!
                    // Esto es lo que arreglará el error 404 de las reseñas
                    SharedPreferences prefs = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("ID_USUARIO", usuarioCreado.getIdUsuario()); // <-- Guardamos el ID real (ej. 1, 2, 3...)
                    editor.putString("NOMBRE_USUARIO", usuarioCreado.getNombreUsuario());
                    editor.putString("NOMBRES", usuarioCreado.getNombres());
                    editor.putString("APELLIDOS", usuarioCreado.getApellidos());
                    editor.putBoolean("ESTA_LOGUEADO", true);
                    editor.apply(); // ¡Guardar cambios!

                    // 3. Enviar al usuario a la app
                    Toast.makeText(Actividad_Registrarse.this, "¡Registro exitoso! Iniciando sesión...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Actividad_Registrarse.this, Actividad_Principal.class);
                    // Limpiamos la pila de actividades para que no pueda "volver" al registro
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // Cerramos la actividad de registro

                }
                else {
                    // (Tu código de manejo de errores está bien)
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
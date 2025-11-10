package com.example.cinedex.UI.Activities;

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

    DAOUsuario dao; // ✅ Para guardar usuario local

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_registrarse);

        dao = new DAOUsuario(this); // ✅ Inicializar DAO

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

            UsuarioRegistroDto registroDto = new UsuarioRegistroDto(username, email, pass, nombres, apellidos);
            intentarRegistro(registroDto);
        });
    }

    // --- MÉTODO ACTUALIZADO ---
    private void intentarRegistro(UsuarioRegistroDto registroDto) {
        CineDexApiService apiService = CineDexApiClient.getApiService();

        apiService.registrarUsuario(registroDto).enqueue(new Callback<UsuarioPublicoDto>() {

            @Override
            public void onResponse(Call<UsuarioPublicoDto> call, Response<UsuarioPublicoDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    UsuarioPublicoDto usuarioCreado = response.body();

                    // ✅ Guardar usuario localmente
                    Usuario usuarioLocal = new Usuario();
                    usuarioLocal.setNombreUsuario(usuarioCreado.getNombreUsuario());
                    usuarioLocal.setEmail(registroDto.getEmail());
                    usuarioLocal.setNombres(usuarioCreado.getNombres());
                    usuarioLocal.setApellidos(usuarioCreado.getApellidos());
                    usuarioLocal.setIdRangoActual(1);

                    new Thread(() -> {
                        dao.Insertar(usuarioLocal);
                    }).start();

                    Toast.makeText(Actividad_Registrarse.this, "Registro exitoso 🎬\nBienvenido " + usuarioCreado.getNombreUsuario(), Toast.LENGTH_LONG).show();
                    finish(); // ✅ Volver al Login
                }
                else {
                    String errorMensaje = "Error desconocido al registrar."; // Mensaje por defecto

                    if (response.errorBody() != null) {
                        try {
                            errorMensaje = response.errorBody().string();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            errorMensaje = "Error al leer la respuesta del servidor.";
                        }
                    }

                    // Imprime el error real en la consola de Logcat (¡MUY IMPORTANTE!)
                    Log.e("API_REGISTRO_FALLO", "Código: " + response.code() + " | Mensaje: " + errorMensaje);

                    // Muestra un Toast más útil al usuario
                    if (response.code() == 409) { // 409 = Conflict (Este SÍ es "Usuario ya existe")
                        Toast.makeText(Actividad_Registrarse.this, "Usuario o correo ya existe.", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) { // 400 = Bad Request (Datos inválidos o... ¡Rangos no encontrados!)
                        Toast.makeText(Actividad_Registrarse.this, "Datos inválidos (Revise Logcat).", Toast.LENGTH_LONG).show();
                    } else { // 500 = Internal Server Error u otro
                        Toast.makeText(Actividad_Registrarse.this, "Error en el servidor (Revise Logcat).", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioPublicoDto> call, Throwable t) {
                // Este error es de CONEXIÓN (ej. sin internet, URL mal escrita, API caída)
                String falloMensaje = (t.getMessage() != null) ? t.getMessage() : "Error de conexión";
                Toast.makeText(Actividad_Registrarse.this, "No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
                Log.e("API_REGISTRO_FALLO", "Fallo de red: " + falloMensaje);
            }
        });
    }
}
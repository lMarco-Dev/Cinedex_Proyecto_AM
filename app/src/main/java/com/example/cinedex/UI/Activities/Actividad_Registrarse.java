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

    private void intentarRegistro(UsuarioRegistroDto registroDto) {
        CineDexApiService apiService = CineDexApiClient.getApiService();

        apiService.registrarUsuario(registroDto).enqueue(new Callback<UsuarioPublicoDto>() {
            @Override
            public void onResponse(Call<UsuarioPublicoDto> call, Response<UsuarioPublicoDto> response) {

                if (response.isSuccessful()) {

                    UsuarioPublicoDto usuarioCreado = response.body();

                    // ✅ Guardar usuario localmente
                    Usuario usuarioLocal = new Usuario();
                    usuarioLocal.setNombreUsuario(usuarioCreado.getNombreUsuario());
                    usuarioLocal.setEmail(registroDto.getEmail());
                    usuarioLocal.setContraseña(registroDto.getContrasena());
                    usuarioLocal.setNombres(registroDto.getNombres());
                    usuarioLocal.setApellidos(registroDto.getApellidos());
                    usuarioLocal.setIdRangoActual(1); // Por defecto

                    dao.Insertar(usuarioLocal); // ✅ GUARDAMOS EN SQLITE

                    Toast.makeText(Actividad_Registrarse.this, "Registro exitoso 🎬\nBienvenido " + usuarioCreado.getNombreUsuario(), Toast.LENGTH_LONG).show();
                    finish(); // ✅ Volver al Login
                }
                else {
                    Toast.makeText(Actividad_Registrarse.this, "Usuario o correo ya existe.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioPublicoDto> call, Throwable t) {
                Toast.makeText(Actividad_Registrarse.this, "No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
                Log.e("API_FALLO", t.getMessage());
            }
        });
    }
}

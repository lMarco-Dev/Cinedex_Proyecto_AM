// Archivo: UI/Activities/Actividad_Usuario.java
package com.example.cinedex.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem; // ✅ IMPORTADO
import android.view.View;      // ✅ IMPORTADO
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout; // ✅ IMPORTADO
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull; // ✅ IMPORTADO
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// ✅ IMPORTADO (Toolbar)
import com.google.android.material.appbar.MaterialToolbar;

import com.example.cinedex.Data.Models.DTOs.ResenaPublicaDto;
import com.example.cinedex.UI.Adapters.ResenaAdapter;

import com.example.cinedex.Data.Access.DAOResena;
import com.example.cinedex.Data.Access.DAOUsuario;
import com.example.cinedex.Data.Models.Resena;
import com.example.cinedex.Data.Models.DTOs.UsuarioActualizarDto;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Usuario extends AppCompatActivity {

    // Vistas de UI
    ImageView ivFoto;
    TextView tvNombreCompleto, tvCorreo;
    EditText etNombres, etApellidos, etCorreoEdit, etCambiarPass, etConfirmPass;
    Button btnEditarGuardar, btnCambiarPass, btnSeleccionarFoto;
    RecyclerView rvResenas;
    ResenaAdapter resenaAdapter;

    // Vistas para Toggles (Menús colapsables)
    TextView tvToggleEditarDatos, tvToggleCambiarPassword;
    LinearLayout layoutEditarDatos, layoutCambiarPassword;

    // Lógica de datos
    DAOResena daoResena;
    DAOUsuario daoUsuario;
    int usuarioId = -1;
    String authToken = "";

    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_usuario);

        // --- 1. LÓGICA DE LA TOOLBAR (FLECHA DE REGRESO) ---
        MaterialToolbar toolbar = findViewById(R.id.toolbarUsuario);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // --- 2. ENCONTRAR VISTAS (findViewById) ---

        // Vistas de Perfil (Header)
        ivFoto = findViewById(R.id.ivFotoUsuario);
        tvNombreCompleto = findViewById(R.id.tvNombreCompleto);
        tvCorreo = findViewById(R.id.tvCorreoPerfil);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        // Vistas de "Editar Datos" (colapsable)
        tvToggleEditarDatos = findViewById(R.id.tvToggleEditarDatos);
        layoutEditarDatos = findViewById(R.id.layoutEditarDatos);
        etNombres = findViewById(R.id.etNombresPerfil);
        etApellidos = findViewById(R.id.etApellidosPerfil);
        etCorreoEdit = findViewById(R.id.etCorreoPerfil); // <-- Ahora existe
        btnEditarGuardar = findViewById(R.id.btnEditarGuardarPerfil); // <-- Ahora existe

        // Vistas de "Cambiar Contraseña" (colapsable)
        tvToggleCambiarPassword = findViewById(R.id.tvToggleCambiarPassword);
        layoutCambiarPassword = findViewById(R.id.layoutCambiarPassword);
        etCambiarPass = findViewById(R.id.etNuevaPassword); // <-- Ahora existe
        etConfirmPass = findViewById(R.id.etConfirmarPassword); // <-- Ahora existe
        btnCambiarPass = findViewById(R.id.btnCambiarPassword); // <-- Ahora existe

        // Vistas de Reseñas (RecyclerView)
        rvResenas = findViewById(R.id.lvResenasUsuario);

        // DAOs
        daoResena = new DAOResena(this);
        daoUsuario = new DAOUsuario(this);

        // --- 3. LÓGICA DE PANELES COLAPSABLES ---
        tvToggleEditarDatos.setOnClickListener(v -> {
            if (layoutEditarDatos.getVisibility() == View.VISIBLE) {
                layoutEditarDatos.setVisibility(View.GONE);
            } else {
                layoutEditarDatos.setVisibility(View.VISIBLE);
            }
        });

        tvToggleCambiarPassword.setOnClickListener(v -> {
            if (layoutCambiarPassword.getVisibility() == View.VISIBLE) {
                layoutCambiarPassword.setVisibility(View.GONE);
            } else {
                layoutCambiarPassword.setVisibility(View.VISIBLE);
            }
        });


        // --- 4. LÓGICA DE DATOS (Cargar SharedPreferences) ---
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        usuarioId = prefs.getInt("ID_USUARIO", -1);
        String nombres = prefs.getString("NOMBRES", "");
        String apellidos = prefs.getString("APELLIDOS", "");
        String nombreUsuario = prefs.getString("NOMBRE_USUARIO", "");
        authToken = prefs.getString("AUTH_TOKEN", "");

        // Setear datos en las vistas
        tvNombreCompleto.setText((nombres + " " + apellidos).trim());
        tvCorreo.setText(nombreUsuario);
        etNombres.setText(nombres);
        etApellidos.setText(apellidos);
        etCorreoEdit.setText(nombreUsuario);

        String fotoUriStr = prefs.getString("URI_FOTO_USUARIO", "");
        if (!TextUtils.isEmpty(fotoUriStr)) {
            ivFoto.setImageURI(Uri.parse(fotoUriStr));
        }

        // --- 5. LÓGICA DE BOTONES (Tus listeners) ---

        // Listener para Guardar Cambios (Editar Datos)
        btnEditarGuardar.setOnClickListener(v -> {
            String newNombres = etNombres.getText().toString().trim();
            String newApellidos = etApellidos.getText().toString().trim();
            if (newNombres.isEmpty() || newApellidos.isEmpty()) {
                Toast.makeText(this, "Nombres y apellidos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!authToken.isEmpty() && usuarioId != -1) {
                // ... (Tu lógica de API para actualizar usuario)
                CineDexApiService api = CineDexApiClient.getApiService();
                String bearer = "Bearer " + authToken;
                UsuarioActualizarDto dto = new UsuarioActualizarDto(newNombres, newApellidos);
                api.actualizarUsuario(usuarioId, dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Actividad_Usuario.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Actividad_Usuario.this, "Error API: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Actividad_Usuario.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // ... (Tu lógica local de DAO para actualizar usuario)
            }
        });

        // Listener para Cambiar Contraseña
        btnCambiarPass.setOnClickListener(v -> {
            // ... (Tu lógica para cambiar contraseña)
        });

        // Launcher y listener para Seleccionar Foto
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selected = result.getData().getData();
                        if (selected != null) {
                            ivFoto.setImageURI(selected);
                            SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                            ed.putString("URI_FOTO_USUARIO", selected.toString());
                            ed.apply();
                        }
                    }
                });

        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        // --- 6. CARGAR RESEÑAS ---
        actualizarListaResenas();

    } // Fin de onCreate


    // --- 7. MÉTODO PARA MANEJAR EL CLIC DE REGRESO (Toolbar) ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Cierra esta actividad y regresa a la anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // --- 8. MÉTODO PARA ACTUALIZAR RESEÑAS (Tu lógica) ---
    private void actualizarListaResenas() {
        CineDexApiService api = CineDexApiClient.getApiService();
        api.getResenasPorUsuario(usuarioId).enqueue(new Callback<List<ResenaPublicaDto>>() {
            @Override
            public void onResponse(Call<List<ResenaPublicaDto>> call, Response<List<ResenaPublicaDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResenaPublicaDto> lista = response.body();

                    resenaAdapter = new ResenaAdapter(lista);
                    rvResenas.setLayoutManager(new LinearLayoutManager(Actividad_Usuario.this));
                    rvResenas.setAdapter(resenaAdapter);
                    rvResenas.setNestedScrollingEnabled(false);
                } else {
                    Toast.makeText(Actividad_Usuario.this, "No se encontraron reseñas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResenaPublicaDto>> call, Throwable t) {
                Toast.makeText(Actividad_Usuario.this, "Error al cargar reseñas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
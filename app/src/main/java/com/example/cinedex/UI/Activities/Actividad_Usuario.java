// Archivo: UI/Activities/Actividad_Usuario.java
package com.example.cinedex.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button; // Importaciones limpias
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// --- IMPORTACIONES AÑADIDAS ---
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinedex.UI.Adapters.ResenaAdapter; // <-- ¡Importamos el adaptador correcto!

import com.example.cinedex.Data.Access.DAOResena;
import com.example.cinedex.Data.Access.DAOUsuario;
import com.example.cinedex.Data.Models.Reseña;
import com.example.cinedex.Data.Models.DTOs.UsuarioActualizarDto;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;

import java.util.List; // Importamos solo List

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Usuario extends AppCompatActivity {

    ImageView ivFoto;
    TextView tvNombreCompleto, tvCorreo;
    EditText etNombres, etApellidos, etCorreoEdit, etCambiarPass, etConfirmPass;
    Button btnEditarGuardar, btnCambiarPass, btnSeleccionarFoto;

    // --- CAMBIADO: De ListView a RecyclerView ---
    RecyclerView rvResenas; // <-- 1. Cambiado de ListView
    ResenaAdapter resenaAdapter; // <-- 2. Añadido el adaptador

    DAOResena daoResena;
    DAOUsuario daoUsuario;

    int usuarioId = -1;
    String authToken = "";

    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_perfil);

        // Vistas
        ivFoto = findViewById(R.id.ivFotoUsuario);
        tvNombreCompleto = findViewById(R.id.tvNombreCompleto);
        tvCorreo = findViewById(R.id.tvCorreoPerfil);

        etNombres = findViewById(R.id.etNombresPerfil);
        etApellidos = findViewById(R.id.etApellidosPerfil);
        etCorreoEdit = findViewById(R.id.etCorreoPerfil);

        btnEditarGuardar = findViewById(R.id.btnEditarGuardarPerfil);
        btnCambiarPass = findViewById(R.id.btnCambiarPassword);
        etCambiarPass = findViewById(R.id.etNuevaPassword);
        etConfirmPass = findViewById(R.id.etConfirmarPassword);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        // --- CORREGIDO: Buscamos el ID del RecyclerView ---
        // Esta era tu línea 75, ahora está corregida
        // Tu XML tiene el ID "lvResenasUsuario" (lo cual está bien)
        // pero lo guardamos en nuestra variable de tipo RecyclerView.
        rvResenas = findViewById(R.id.lvResenasUsuario); // <-- 3. ¡Ahora sí coincide!

        daoResena = new DAOResena(this);
        daoUsuario = new DAOUsuario(this);

        // --- El resto de tu lógica de SharedPreferences y botones está PERFECTA ---
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        usuarioId = prefs.getInt("ID_USUARIO", -1);
        String nombres = prefs.getString("NOMBRES", "");
        String apellidos = prefs.getString("APELLIDOS", "");
        String nombreUsuario = prefs.getString("NOMBRE_USUARIO", "");
        authToken = prefs.getString("AUTH_TOKEN", ""); // puede estar vacío

        tvNombreCompleto.setText((nombres + " " + apellidos).trim());
        tvCorreo.setText(nombreUsuario);
        etNombres.setText(nombres);
        etApellidos.setText(apellidos);
        etCorreoEdit.setText(nombreUsuario);

        String fotoUriStr = prefs.getString("URI_FOTO_USUARIO", "");
        if (!TextUtils.isEmpty(fotoUriStr)) {
            ivFoto.setImageURI(Uri.parse(fotoUriStr));
        }

        // --- Tu lógica de botones está bien, no la tocamos ---

        btnEditarGuardar.setOnClickListener(v -> {
            String newNombres = etNombres.getText().toString().trim();
            String newApellidos = etApellidos.getText().toString().trim();
            if (newNombres.isEmpty() || newApellidos.isEmpty()) {
                Toast.makeText(this, "Nombres y apellidos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!authToken.isEmpty() && usuarioId != -1) {
                CineDexApiService api = CineDexApiClient.getApiService();
                String bearer = "Bearer " + authToken;
                UsuarioActualizarDto dto = new UsuarioActualizarDto(newNombres, newApellidos);
                api.actualizarUsuario(bearer, usuarioId, dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                            ed.putString("NOMBRES", newNombres);
                            ed.putString("APELLIDOS", newApellidos);
                            ed.apply();
                            tvNombreCompleto.setText(newNombres + " " + newApellidos);
                            Toast.makeText(Actividad_Usuario.this, "Datos actualizados en el servidor", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Actividad_Usuario.this, "Error al actualizar en servidor (revise log)", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Actividad_Usuario.this, "Error de conexión al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                com.example.cinedex.Data.Models.Usuario u = new com.example.cinedex.Data.Models.Usuario();
                u.setNombreUsuario(etCorreoEdit.getText().toString().trim());
                u.setNombres(newNombres);
                u.setApellidos(newApellidos);
                u.setContraseña("");
                boolean ok = daoUsuario.Actualizar(u, usuarioId);
                if (ok) {
                    SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                    ed.putString("NOMBRES", newNombres);
                    ed.putString("APELLIDOS", newApellidos);
                    ed.apply();
                    tvNombreCompleto.setText(newNombres + " " + newApellidos);
                    Toast.makeText(this, "Datos actualizados localmente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo actualizar localmente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCambiarPass.setOnClickListener(v -> {
            String pass = etCambiarPass.getText().toString();
            String conf = etConfirmPass.getText().toString();
            if (pass.isEmpty() || conf.isEmpty()) {
                Toast.makeText(this, "Complete ambos campos de contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(conf)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            com.example.cinedex.Data.Models.Usuario u = new com.example.cinedex.Data.Models.Usuario();
            u.setNombreUsuario(etCorreoEdit.getText().toString().trim());
            u.setContraseña(pass);
            u.setNombres(etNombres.getText().toString().trim());
            u.setApellidos(etApellidos.getText().toString().trim());
            boolean ok = daoUsuario.Actualizar(u, usuarioId);
            if (ok) {
                Toast.makeText(this, "Contraseña actualizada localmente", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                ed.putBoolean("ESTA_LOGUEADO", true);
                ed.apply();
                etCambiarPass.setText("");
                etConfirmPass.setText("");
            } else {
                Toast.makeText(this, "No se pudo actualizar la contraseña localmente", Toast.LENGTH_SHORT).show();
            }
        });

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

        // --- Cargar reseñas del usuario y mostrarlas ---
        // 4. Esta función ahora está corregida
        actualizarListaResenas();

    } // Fin de onCreate


    // --- MÉTODO ACTUALIZADO ---
    // Esta función ahora usa el RecyclerView y el ResenaAdapter
    private void actualizarListaResenas() {

        // 1. Obtener la lista de reseñas del DAO (esto ya lo tenías)
        List<Reseña> lista = daoResena.ListarPorUsuario(usuarioId);

        // 2. Configurar el Adaptador que ya creamos
        resenaAdapter = new ResenaAdapter(lista);

        // 3. Configurar el RecyclerView
        rvResenas.setLayoutManager(new LinearLayoutManager(this));
        rvResenas.setAdapter(resenaAdapter);

        // 4. (IMPORTANTE) Deshabilitar el scroll anidado
        // Esto es vital porque el RecyclerView está DENTRO de un ScrollView.
        // Esto hace que el ScrollView principal maneje todo el scroll.
        rvResenas.setNestedScrollingEnabled(false);
    }
}
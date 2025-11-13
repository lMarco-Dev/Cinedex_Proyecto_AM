package com.example.cinedex.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinedex.Data.Access.DAOResena;
import com.example.cinedex.Data.Access.DAOUsuario;
import com.example.cinedex.Data.Models.Reseña;
import com.example.cinedex.Data.Models.DTOs.UsuarioActualizarDto;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 Actividad_Perfil:
 - Muestra nombre + apellidos (clic -> editar)
 - Muestra correo
 - Permite editar nombres/apellidos y enviar a la API (si hay token)
 - Permite cambiar contraseña (local via DAOUsuario)
 - Muestra foto de usuario (picker) y guarda la URI en SharedPreferences
 - Lista reseñas hechas por el usuario (DAOResena.ListarPorUsuario)
*/

public class Actividad_Usuario extends AppCompatActivity {

    ImageView ivFoto;
    TextView tvNombreCompleto, tvCorreo;
    EditText etNombres, etApellidos, etCorreoEdit, etCambiarPass, etConfirmPass;
    Button btnEditarGuardar, btnCambiarPass, btnSeleccionarFoto;
    ListView lvResenas;
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

        lvResenas = findViewById(R.id.lvResenasUsuario);

        daoResena = new DAOResena(this);
        daoUsuario = new DAOUsuario(this);

        // Obtener sesión/usuario de SharedPreferences (igual que en login)
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        usuarioId = prefs.getInt("ID_USUARIO", -1);
        String nombres = prefs.getString("NOMBRES", "");
        String apellidos = prefs.getString("APELLIDOS", "");
        String nombreUsuario = prefs.getString("NOMBRE_USUARIO", "");
        authToken = prefs.getString("AUTH_TOKEN", ""); // puede estar vacío

        // Mostrar
        tvNombreCompleto.setText((nombres + " " + apellidos).trim());
        tvCorreo.setText(nombreUsuario);

        etNombres.setText(nombres);
        etApellidos.setText(apellidos);
        etCorreoEdit.setText(nombreUsuario);

        // foto guardada (URI) en prefs
        String fotoUriStr = prefs.getString("URI_FOTO_USUARIO", "");
        if (!TextUtils.isEmpty(fotoUriStr)) {
            ivFoto.setImageURI(Uri.parse(fotoUriStr));
        }

        // editar <-> guardar
        btnEditarGuardar.setOnClickListener(v -> {
            String newNombres = etNombres.getText().toString().trim();
            String newApellidos = etApellidos.getText().toString().trim();
            if (newNombres.isEmpty() || newApellidos.isEmpty()) {
                Toast.makeText(this, "Nombres y apellidos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si hay token, intenta actualizar via API (PUT api/Usuarios/{id})
            if (!authToken.isEmpty() && usuarioId != -1) {
                CineDexApiService api = CineDexApiClient.getApiService();
                String bearer = "Bearer " + authToken;
                UsuarioActualizarDto dto = new UsuarioActualizarDto(newNombres, newApellidos);
                api.actualizarUsuario(bearer, usuarioId, dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // actualizar SharedPreferences
                            SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                            ed.putString("NOMBRES", newNombres);
                            ed.putString("APELLIDOS", newApellidos);
                            ed.apply();

                            tvNombreCompleto.setText(newNombres + " " + newApellidos);
                            Toast.makeText(Actividad_Perfil.this, "Datos actualizados en el servidor", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Actividad_Perfil.this, "Error al actualizar en servidor (revise log)", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Actividad_Perfil.this, "Error de conexión al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // No hay token => actualizamos localmente en la BD con DAOUsuario
                // Necesitamos construir un Usuario y usar daoUsuario.Actualizar
                com.example.cinedex.Data.Models.Usuario u = new com.example.cinedex.Data.Models.Usuario();
                u.setNombreUsuario(etCorreoEdit.getText().toString().trim());
                u.setNombres(newNombres);
                u.setApellidos(newApellidos);
                u.setContraseña(""); // no la tocamos aquí
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

        // cambiar contraseña (LOCAL): actualiza tabla Usuario via DAOUsuario.Actualizar
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

            // Obtener usuario local, usar DAOUsuario.Actualizar
            com.example.cinedex.Data.Models.Usuario u = new com.example.cinedex.Data.Models.Usuario();
            u.setNombreUsuario(etCorreoEdit.getText().toString().trim());
            u.setContraseña(pass);
            u.setNombres(etNombres.getText().toString().trim());
            u.setApellidos(etApellidos.getText().toString().trim());
            boolean ok = daoUsuario.Actualizar(u, usuarioId);
            if (ok) {
                Toast.makeText(this, "Contraseña actualizada localmente", Toast.LENGTH_SHORT).show();
                // actualizar SharedPreferences si guardas algo
                SharedPreferences.Editor ed = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit();
                ed.putBoolean("ESTA_LOGUEADO", true);
                ed.apply();
                etCambiarPass.setText("");
                etConfirmPass.setText("");
            } else {
                Toast.makeText(this, "No se pudo actualizar la contraseña localmente", Toast.LENGTH_SHORT).show();
            }
        });

        // seleccionar foto -> launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selected = result.getData().getData();
                        if (selected != null) {
                            ivFoto.setImageURI(selected);
                            // guardar URI en prefs
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

        // Cargar reseñas del usuario y mostrarlas
        actualizarListaResenas();

    }

    private void actualizarListaResenas() {
        List<Reseña> lista = daoResena.ListarPorUsuario(usuarioId);
        List<String> textos = new ArrayList<>();
        for (Reseña r : lista) {
            String t = String.format("%.1f ★ — %s", r.getPuntuacion(), r.getReseñaTexto());
            textos.add(t);
        }
        lvResenas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos));
    }
}


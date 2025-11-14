// Archivo: UI/Fragments/ProfileFragment.java
package com.example.cinedex.UI.Fragments;

import android.content.Context; // ✅ IMPORTADO
import android.content.Intent;
import android.content.SharedPreferences; // ✅ IMPORTADO
import android.net.Uri; // ✅ IMPORTADO
import android.os.Bundle;
import android.text.TextUtils; // ✅ IMPORTADO
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cinedex.R;
import com.example.cinedex.UI.Activities.Actividad_Usuario;
import com.example.cinedex.UI.Activities.Actividad_Terminos;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    // ✅ AÑADIDO: Mantenemos referencias a las vistas para actualizarlas
    private TextView tvNombre;
    private TextView tvCorreo;
    private ImageView ivFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Solo infla la vista aquí
        return inflater.inflate(R.layout.ly_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- 1. Referencias a la tarjeta de perfil ---
        CardView cardPerfil = view.findViewById(R.id.cardPerfil);
        tvNombre = view.findViewById(R.id.tvNombreCompleto); // Asignamos a la variable de clase
        tvCorreo = view.findViewById(R.id.tvCorreoPerfil); // Asignamos a la variable de clase
        ivFoto = view.findViewById(R.id.ivFotoUsuario);   // Asignamos a la variable de clase

        // --- 2. Referencias al menú de opciones ---
        TextView btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        TextView btnTerminos = view.findViewById(R.id.btnTerminos);
        MaterialButton btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // --- 3. Cargar los datos (CÓDIGO REAL) ---
        // ✅ Cargar la info real desde SharedPreferences
        cargarDatosUsuario();

        // --- 4. Asignar los nuevos listeners ---

        // Listener para la tarjeta completa
        cardPerfil.setOnClickListener(v -> {
            // Esto abre la pantalla de Actividad_Usuario
            Intent i = new Intent(getActivity(), Actividad_Usuario.class);
            startActivity(i);
        });

        // Listener para el botón de "Editar mi perfil"
        btnEditarPerfil.setOnClickListener(v -> {
            // También abre la pantalla de Actividad_Usuario
            Intent i = new Intent(getActivity(), Actividad_Usuario.class);
            startActivity(i);
        });

        // Listener para Términos y Condiciones
        btnTerminos.setOnClickListener(v -> {
            // Aquí pones tu lógica para mostrar los términos
            // Intent i = new Intent(getActivity(), Actividad_Terminos.class);
            // startActivity(i);
            Toast.makeText(getActivity(), "Abriendo términos...", Toast.LENGTH_SHORT).show();
        });

        // Listener para Cerrar Sesión
        btnCerrarSesion.setOnClickListener(v -> {
            // Aquí pones tu lógica para cerrar sesión (borrar token, ir a Login)
            Toast.makeText(getActivity(), "Cerrando sesión...", Toast.LENGTH_SHORT).show();
        });
    }

    // ✅ NUEVO: Método para cargar los datos del usuario
    private void cargarDatosUsuario() {
        // 1. Obtener SharedPreferences (Necesitamos 'getActivity()' para el contexto)
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);

        // 2. Extraer los datos
        String nombres = prefs.getString("NOMBRES", "Usuario");
        String apellidos = prefs.getString("APELLIDOS", "Invitado");
        String correo = prefs.getString("NOMBRE_USUARIO", "correo@ejemplo.com");
        String fotoUriStr = prefs.getString("URI_FOTO_USUARIO", "");

        // 3. Formatear y establecer los textos
        String nombreCompleto = (nombres + " " + apellidos).trim();
        tvNombre.setText(nombreCompleto);
        tvCorreo.setText(correo);

        // 4. Cargar la foto de perfil si existe
        if (!TextUtils.isEmpty(fotoUriStr)) {
            try {
                Uri fotoUri = Uri.parse(fotoUriStr);
                ivFoto.setImageURI(fotoUri);
            } catch (Exception e) {
                // Manejar error si la URI está mal formada
                ivFoto.setImageResource(R.drawable.ic_person); // Poner imagen por defecto
            }
        } else {
            ivFoto.setImageResource(R.drawable.ic_person); // Poner imagen por defecto
        }
    }

    // ✅ NUEVO: Sobrescribir 'onResume'
    @Override
    public void onResume() {
        super.onResume();
        // Recargar los datos cada vez que el fragmento se vuelve visible
        // Esto es CLAVE para que si el usuario edita su perfil en 'Actividad_Usuario'
        // y regresa, los datos se actualicen aquí automáticamente.
        cargarDatosUsuario();
    }
}
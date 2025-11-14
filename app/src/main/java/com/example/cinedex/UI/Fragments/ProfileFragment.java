// Archivo: UI/Fragments/ProfileFragment.java
package com.example.cinedex.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinedex.R;
// --- CORREGIDO 1: Importar la clase Java correcta ---
import com.example.cinedex.UI.Activities.Actividad_Usuario;
// --- CORREGIDO 2: Importar la clase Java correcta (con guion bajo) ---
import com.example.cinedex.UI.Activities.Actividad_Terminos;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ly_fragment_profile, container, false);

        Button btnAbrirPerfilCompleto = view.findViewById(R.id.btnAbrirPerfilCompleto);

        // --- CORREGIDO 3: Esta línea ya no dará error ---
        Button btnTerminos = view.findViewById(R.id.btnTerminos);

        // botón para abrir pantalla de perfil completo
        btnAbrirPerfilCompleto.setOnClickListener(v -> {
            // --- CORREGIDO 1: Llamar a la Actividad correcta ---
            Intent i = new Intent(getActivity(), Actividad_Usuario.class);
            startActivity(i);
        });

        // botón de términos (si luego lo agregas)
        if (btnTerminos != null) {
            btnTerminos.setOnClickListener(v -> {
                // --- CORREGIDO 2: Llamar a la Actividad correcta ---
                Intent i = new Intent(getActivity(), Actividad_Terminos.class);
                startActivity(i);
            });
        }

        return view;
    }
}
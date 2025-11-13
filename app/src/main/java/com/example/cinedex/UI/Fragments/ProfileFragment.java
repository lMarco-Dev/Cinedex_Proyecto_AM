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
import com.example.cinedex.UI.Activities.Actividad_Perfil;
import com.example.cinedex.UI.Activities.ActividadTerminos;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ly_fragment_profile, container, false);

        Button btnAbrirPerfilCompleto = view.findViewById(R.id.btnAbrirPerfilCompleto);
        Button btnTerminos = view.findViewById(R.id.btnTerminos);

        // botón para abrir pantalla de perfil completo
        btnAbrirPerfilCompleto.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), Actividad_Perfil.class);
            startActivity(i);
        });

        // botón de términos (si luego lo agregas)
        if (btnTerminos != null) {
            btnTerminos.setOnClickListener(v -> {
                Intent i = new Intent(getActivity(), ActividadTerminos.class);
                startActivity(i);
            });
        }

        return view;
    }
}


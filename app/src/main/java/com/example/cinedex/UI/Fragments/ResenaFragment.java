// Archivo: UI/Fragments/ResenaFragment.java
package com.example.cinedex.UI.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cinedex.Data.Models.Resena;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.ResenaAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//
// ESTE ES EL FRAGMENTO LIMPIO
// ¡YA NO TIENE LA CLASE ResenaAdapter DUPLICADA DENTRO!
//
public class ResenaFragment extends Fragment {

    private RecyclerView rvResenas;
    private ResenaAdapter adapter;
    private List<Resena> listaDeResenas;
    private CineDexApiService apiService;

    public ResenaFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaDeResenas = new ArrayList<>();
        apiService = CineDexApiClient.getApiService();
        // Inicializamos el adaptador con la lista vacía
        adapter = new ResenaAdapter(listaDeResenas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout simple del fragmento (el que creaste en el Paso 2)
        return inflater.inflate(R.layout.ly_fragment_resena, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Encontrar el RecyclerView en el layout del fragmento
        rvResenas = view.findViewById(R.id.rv_resenas_fragment); // ID del Paso 2
        rvResenas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResenas.setAdapter(adapter);

        // Cargar los datos
        cargarResenasDesdeApi();
    }

    private void cargarResenasDesdeApi(){
        apiService.getResenas().enqueue(new Callback<List<Resena>>() {
            @Override
            public void onResponse(Call<List<Resena>> call, Response<List<Resena>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar la lista en el adaptador
                    listaDeResenas.clear();
                    listaDeResenas.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos cambiaron
                } else {
                    Toast.makeText(getContext(), "Error al cargar reseñas", Toast.LENGTH_SHORT).show();
                    Log.e("ResenaFragment", "Error API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Resena>> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("ResenaFragment", "Fallo de red: " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarResenasDesdeApi();
    }
}
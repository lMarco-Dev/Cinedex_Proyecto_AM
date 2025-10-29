package com.example.cinedex.UI.Fragments;

// ... (imports existentes) ...
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.MainAdapter; // Nuevo Adaptador
import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Models.Section; // Nuevo Modelo
import com.example.cinedex.Data.Network.RetrofitClient;
import com.example.cinedex.Data.Network.TmdbApiService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private final String TMDB_API_KEY = "f908b6414babca36cf721d90d6b85e1f";

    private RecyclerView mainRecyclerView; // Renombrado a mainRecyclerView
    private MainAdapter mainAdapter; // El nuevo adaptador vertical

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ly_home_fragment, container, false);

        // 1. Inicializar RecyclerView Principal (Vertical)
        mainRecyclerView = view.findViewById(R.id.recycler_popular_movies); // Mismo ID

        // Configuración para el desplazamiento VERTICAL
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. Inicializar el MainAdapter
        mainAdapter = new MainAdapter(new ArrayList<>(), getContext());
        mainRecyclerView.setAdapter(mainAdapter);

        // 3. Iniciar la carga de datos
        fetchPopularMovies();

        return view;
    }

    private void fetchPopularMovies(){
        TmdbApiService apiService = RetrofitClient.getApiService();
        Call<MovieResponse> call = apiService.getPopularMovies(TMDB_API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> allMovies = response.body().getMovies();

                    // 🚨 SIMULACIÓN: Dividir la lista para crear dos secciones
                    List<Movie> section1Movies = allMovies.subList(0, Math.min(10, allMovies.size()));
                    List<Movie> section2Movies = allMovies.subList(Math.min(10, allMovies.size()), allMovies.size());

                    // Crear las secciones
                    List<Section> sections = new ArrayList<>();
                    sections.add(new Section("🔥 Animes estranhos... e irresistíveis", section1Movies));
                    sections.add(new Section("👽 Alienígenas y Fantasmas", section2Movies));

                    // Actualizar el MainAdapter (el vertical)
                    mainAdapter.setSections(sections);

                    Log.d("TMDB", "Secciones cargadas con un total de " + allMovies.size() + " películas.");
                } else {
                    Log.e("TMDB", "Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("TMDB", "Fallo de conexión: " + t.getMessage(), t);
            }
        });
    }
}
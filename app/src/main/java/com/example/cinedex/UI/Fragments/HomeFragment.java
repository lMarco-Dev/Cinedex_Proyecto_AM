package com.example.cinedex.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.MainAdapter;
import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Models.Section;
import com.example.cinedex.Data.Models.SectionTop10;
import com.example.cinedex.Data.Network.TmdbClient;
import com.example.cinedex.Data.Network.TmdbApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private final String TMDB_API_KEY = "f908b6414babca36cf721d90d6b85e1f";

    private RecyclerView mainRecyclerView;
    private MainAdapter mainAdapter;
    private TmdbApiService apiService;

    private int totalCalls = 5;
    private int callsCompleted = 0;

    // --- CAMBIO: Variables para guardar los resultados ---
    // Guardaremos cada resultado aquí para controlar el orden
    private Section sectionPopulares = null;
    private Section sectionCartelera = null;
    private SectionTop10 sectionTop10 = null;
    private Section sectionEstrenos = null;
    private Section sectionTendencias = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Asumiendo que tu layout se llama 'fragment_home.xml'
        View view = inflater.inflate(R.layout.ly_fragment_home, container, false);

        apiService = TmdbClient.getApiService();
        mainRecyclerView = view.findViewById(R.id.recycler_popular_movies);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el MainAdapter con una lista vacía de tipo 'Object'
        mainAdapter = new MainAdapter(new ArrayList<>(), getContext());
        mainRecyclerView.setAdapter(mainAdapter);

        // Inicia la carga
        fetchAllSections();
        return view;
    }

    private void fetchAllSections() {
        // Limpiamos todo para una nueva carga
        callsCompleted = 0;
        totalCalls = 5;
        sectionPopulares = null;
        sectionCartelera = null;
        sectionTop10 = null;
        sectionEstrenos = null;
        sectionTendencias = null;

        // 1. Cargar "Top 10"
        apiService.getTopRatedMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionTop10 = new SectionTop10(
                            "TOP 10 HOY",
                            "Lo más visto en Perú",
                            response.body().getMovies().subList(0, 10)
                    );
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 2. Cargar "Populares"
        apiService.getPopularMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionPopulares = new Section("Películas Populares", response.body().getMovies());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 3. Cargar "Próximos Estrenos"
        apiService.getUpcomingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionEstrenos = new Section("Próximos Estrenos", response.body().getMovies());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 4. Cargar "En Cartelera"
        apiService.getNowPlayingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionCartelera = new Section("En Cartelera", response.body().getMovies());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 5. Cargar "Tendencias de la Semana"
        apiService.getTrendingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionTendencias = new Section("Tendencias de la Semana", response.body().getMovies());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });
    }

    // --- CAMBIO: Este método ahora construye la lista al final ---
    private synchronized void checkIfAllCallsAreDone() {
        callsCompleted++;
        // Solo continuar si TODAS las llamadas (5) han terminado
        if (callsCompleted == totalCalls) {

            Log.d("HomeFragment", "Todas las " + totalCalls + " secciones cargadas. Construyendo UI.");

            // 1. Crear la lista final en el orden deseado
            List<Object> finalList = new ArrayList<>();

            if (sectionPopulares != null) {
                finalList.add(sectionPopulares);
            }
            if (sectionCartelera != null) {
                finalList.add(sectionCartelera);
            }

            // 2. AÑADIR EL TOP 10 EN EL MEDIO
            if (sectionTop10 != null) {
                finalList.add(sectionTop10);
            }

            if (sectionEstrenos != null) {
                finalList.add(sectionEstrenos);
            }
            if (sectionTendencias != null) {
                finalList.add(sectionTendencias);
            }

            // 3. Actualizar el adaptador UNA SOLA VEZ
            // Esto elimina el "salto" del scroll
            mainAdapter.setSections(finalList);
        }
    }
}
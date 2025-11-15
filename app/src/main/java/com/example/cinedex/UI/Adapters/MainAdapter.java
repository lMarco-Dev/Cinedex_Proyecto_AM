package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinedex.Data.Models.Section;
import com.example.cinedex.Data.Models.SectionTop10; // Importa el nuevo modelo
import com.example.cinedex.R;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 1. Tipos de vista
    private static final int VIEW_TYPE_STANDARD = 0; // -> Filas normales
    private static final int VIEW_TYPE_TOP10 = 1; // -> Fila para top10

    private List<Object> sections; //-> Lista de datos donde se guardaran los Sections
    private Context context; // -> Para dar acceso herramientas y recursos

    public MainAdapter(List<Object> sections, Context context) {
        this.sections = sections;
        this.context = context;
    }

    /* ========================================================================
                            METODO INSPECTOR
       ======================================================================== */
    @Override
    public int getItemViewType(int position) {
        //Indica que tipo de fila construir de acuerdo a su posición
        if (sections.get(position) instanceof Section) {
            return VIEW_TYPE_STANDARD;
        } else if (sections.get(position) instanceof SectionTop10) {
            return VIEW_TYPE_TOP10;
        }
        return -1; // Error
    }

    /* ========================================================================
                            METODO PARA FABRICAR FILAS
       ======================================================================== */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout correcto basado en el viewType
        LayoutInflater inflater = LayoutInflater.from(context);

        //Si es de tipo 0 -> fila normal
        if (viewType == VIEW_TYPE_STANDARD) {
            View view = inflater.inflate(R.layout.item_section, parent, false);
            return new SectionViewHolder(view);
        } else {
            // Si es de tipo 1 -> fila top 10
            View view = inflater.inflate(R.layout.item_section_top10, parent, false);
            return new SectionTop10ViewHolder(view);
        }
    }

    /* ========================================================================
                            METODO DECORADOR PARA LLENAR LOS DATOS
       ======================================================================== */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bindea los datos al ViewHolder correcto, Pregunta de que tipo es 0 o 1
        if (holder.getItemViewType() == VIEW_TYPE_STANDARD) { // -> SI es de tipo 0

            // Es una sección estándar
            SectionViewHolder vhStandard = (SectionViewHolder) holder;
            Section section = (Section) sections.get(position);
            vhStandard.sectionTitle.setText(section.getTitle());

            vhStandard.movieRecyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
            // USA EL MovieAdapter ESTÁNDAR
            MovieAdapter movieAdapter = new MovieAdapter(section.getMovieList(), context); //-> Constructor horizontal
            vhStandard.movieRecyclerView.setAdapter(movieAdapter); // -> Le da las peliculas y llena la lista

        } else if (holder.getItemViewType() == VIEW_TYPE_TOP10) {
            // Es una sección Top 10
            SectionTop10ViewHolder vhTop10 = (SectionTop10ViewHolder) holder;
            SectionTop10 sectionTop10 = (SectionTop10) sections.get(position);

            // Pone el titulo y subtitulo
            vhTop10.sectionTitle.setText(sectionTop10.getTitle());
            vhTop10.sectionSubtitle.setText(sectionTop10.getSubtitle());

            vhTop10.movieRecyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
            // USA EL MovieAdapterTop10 (NUEVO)
            MovieAdapterTop10 movieAdapterTop10 = new MovieAdapterTop10(sectionTop10.getMovies(), context);
            vhTop10.movieRecyclerView.setAdapter(movieAdapterTop10);
        }
    }

    /* ========================================================================
                                        CONTADOR
       ======================================================================== */
    @Override
    public int getItemCount() {
        return sections.size();
    }


    /* ========================================================================
                                    ACTUALIZADOR
       ======================================================================== */
    public void setSections(List<Object> newSections) {
        this.sections.clear();
        this.sections.addAll(newSections); // -> Añade la lista nueva y completa
        notifyDataSetChanged(); // -> Dibuja los datos
    }


    /* ========================================================================
                             MOLDE PARA LA FILA ESTANDAR
       ======================================================================== */
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView movieRecyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.section_title); // -> Busca el TextView del titulo y lo guarda
            movieRecyclerView = itemView.findViewById(R.id.section_recycler_view); // -> Busca el RecyclerView y lo guarda en la caja
        }
    }


    /* ========================================================================
                             MOLDE PARA LA FILA TOP10
       ======================================================================== */
    public static class SectionTop10ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        TextView sectionSubtitle;
        RecyclerView movieRecyclerView;

        public SectionTop10ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.section_title_top10);
            sectionSubtitle = itemView.findViewById(R.id.section_subtitle_top10);
            movieRecyclerView = itemView.findViewById(R.id.section_recycler_view_top10);
        }
    }
}
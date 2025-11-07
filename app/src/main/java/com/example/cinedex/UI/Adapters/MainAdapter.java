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
    private static final int VIEW_TYPE_STANDARD = 0;
    private static final int VIEW_TYPE_TOP10 = 1;

    // AHORA ES UNA LISTA DE 'Object'
    private List<Object> sections;
    private Context context;

    public MainAdapter(List<Object> sections, Context context) {
        this.sections = sections;
        this.context = context;
    }

    // 2. Determina qué tipo de vista es
    @Override
    public int getItemViewType(int position) {
        if (sections.get(position) instanceof Section) {
            return VIEW_TYPE_STANDARD;
        } else if (sections.get(position) instanceof SectionTop10) {
            return VIEW_TYPE_TOP10;
        }
        return -1; // Error
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 3. Infla el layout correcto basado en el viewType
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_STANDARD) {
            View view = inflater.inflate(R.layout.item_section, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_section_top10, parent, false);
            return new SectionTop10ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 4. Bindea los datos al ViewHolder correcto
        if (holder.getItemViewType() == VIEW_TYPE_STANDARD) {
            // Es una sección estándar
            SectionViewHolder vhStandard = (SectionViewHolder) holder;
            Section section = (Section) sections.get(position);

            vhStandard.sectionTitle.setText(section.getTitle());

            vhStandard.movieRecyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
            // USA EL MovieAdapter ESTÁNDAR
            MovieAdapter movieAdapter = new MovieAdapter(section.getMovieList(), context);
            vhStandard.movieRecyclerView.setAdapter(movieAdapter);

        } else if (holder.getItemViewType() == VIEW_TYPE_TOP10) {
            // Es una sección Top 10
            SectionTop10ViewHolder vhTop10 = (SectionTop10ViewHolder) holder;
            SectionTop10 sectionTop10 = (SectionTop10) sections.get(position);

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

    @Override
    public int getItemCount() {
        return sections.size();
    }

    // Método para actualizar la lista (ahora de Objects)
    public void setSections(List<Object> newSections) {
        this.sections.clear();
        this.sections.addAll(newSections);
        notifyDataSetChanged();
    }

    // 5. Múltiples ViewHolders

    // ViewHolder para la sección estándar
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView movieRecyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.section_title);
            movieRecyclerView = itemView.findViewById(R.id.section_recycler_view);
        }
    }

    // ViewHolder para la sección Top 10
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
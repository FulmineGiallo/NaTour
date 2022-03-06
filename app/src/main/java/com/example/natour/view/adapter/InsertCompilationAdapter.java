package com.example.natour.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Compilation;

import java.util.List;

public class InsertCompilationAdapter extends RecyclerView.Adapter<InsertCompilationAdapter.ViewHolder>
{
    private final List<Compilation> compilationList;
    private ControllerVisualizzaItinerario controllerVisualizzaItinerario;

    public InsertCompilationAdapter(List<Compilation> compilationList, FragmentManager manager,
                                    ControllerVisualizzaItinerario controllerVisualizzaItinerario, Context context){
        this.compilationList = compilationList;
        this.controllerVisualizzaItinerario = controllerVisualizzaItinerario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_inserisci_compilation, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(!compilationList.isEmpty()){
            holder.txt_titolo.setText(compilationList.get(position).getNome());
            holder.btn_aggiungi.setOnClickListener(view -> {
                controllerVisualizzaItinerario.addItinerarioToCompilation(compilationList.get(position));
            });
        }
        else {
            //todo:caso in cui non ci sono compilation
            Log.i("InsertCompAdapter", "non ci sono compilation");
        }
    }

    @Override
    public int getItemCount()
    {
        return compilationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_titolo;
        Button btn_aggiungi;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_titolo = itemView.findViewById(R.id.txt_nome_compilation_aggiungi);
            btn_aggiungi = itemView.findViewById(R.id.btn_aggiungi_itinerario);
        }
    }
}

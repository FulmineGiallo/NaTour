package com.example.natour.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.model.Compilation;

import java.util.List;

public class CompilationAdapter extends RecyclerView.Adapter<CompilationAdapter.ViewHolder>
{

    List<Compilation> compilationList;
    ControllerProfile controllerProfile;

    public CompilationAdapter(ControllerProfile controllerProfile, List<Compilation> compilationList){
        this.compilationList = compilationList;
        this.controllerProfile = controllerProfile;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_compilation, parent, false);
        return new CompilationAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(!compilationList.isEmpty()){
            holder.txt_titolo_compilation.setText(compilationList.get(position).getNome());
            holder.txt_descrizione_compilation.setText(compilationList.get(position).getDescrizione());
            controllerProfile.setItinerariCompilationAdapter(holder.recyclerView, compilationList.get(position));
        }
    }

    @Override
    public int getItemCount()
    {
        return compilationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        RecyclerView recyclerView;
        TextView txt_titolo_compilation;
        TextView txt_descrizione_compilation;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rec_view_itinerario_compilation);
            txt_titolo_compilation = itemView.findViewById(R.id.txt_nomecompilation);
            txt_descrizione_compilation = itemView.findViewById(R.id.txt_descrizionecompilation);
        }
    }
}

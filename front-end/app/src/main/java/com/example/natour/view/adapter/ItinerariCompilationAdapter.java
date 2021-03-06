package com.example.natour.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.natour.R;
import com.example.natour.controller.ControllerInterface;
import com.example.natour.model.Itinerario;

import java.util.List;

public class ItinerariCompilationAdapter extends RecyclerView.Adapter<ItinerariCompilationAdapter.ViewHolder>
{

    private List<Itinerario> itinerarioList;
    private Fragment fragment;
    private RecyclerView recyclerView;
    private ControllerInterface controllerAdapter;


    public ItinerariCompilationAdapter(List<Itinerario> itinerarioList, Fragment fragment, RecyclerView recyclerView, ControllerInterface controllerAdapter)
    {
        this.itinerarioList = itinerarioList;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.controllerAdapter = controllerAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_itinerario_compilation, parent, false);
        return new ItinerariCompilationAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(!itinerarioList.isEmpty()){
            Glide
                    .with(recyclerView)
                    .load(itinerarioList.get(holder.getAdapterPosition()).getImmagini().get(0).getURL())
                    .error(R.drawable.placeholder)
                    .into(holder.img_itinerario);
        }
        holder.img_itinerario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i("position", String.valueOf(holder.getAdapterPosition()));
                /* Mettere nel put extra l'itinerario e fare l'intent a visualizzaItinerarioActivity */
                controllerAdapter.visualizzaItinerario(itinerarioList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return itinerarioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_itinerario;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img_itinerario = itemView.findViewById(R.id.img_itinerario_compilation);
        }
    }
}

package com.example.natour.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Recensione;

import java.util.List;

public class RecensioniAdapter extends RecyclerView.Adapter<RecensioniAdapter.ViewHolder>
{
    private List<Recensione> recensioneList;
    private FragmentManager manager;
    private ControllerVisualizzaItinerario controllerVisualizzaItinerario;
    private Context context;

    public RecensioniAdapter(List<Recensione> recensioneList, FragmentManager manager,
                             ControllerVisualizzaItinerario controller, Context context){
        this.recensioneList = recensioneList;
        this.manager = manager;
        this.controllerVisualizzaItinerario = controller;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.i("ADPTER RECENSIONI", "CREATA");

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_recensione, parent, false);

        return new ViewHolder(layoutView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Log.i("ADPTER RECENSIONI", String.valueOf(holder.getAdapterPosition()));
        if(!recensioneList.isEmpty())
        {
            holder.rate_valutazione.setRating(recensioneList.get(holder.getAdapterPosition()).getValutazione());
            holder.txt_utente.setText(recensioneList.get(holder.getAdapterPosition()).getUtente());
            holder.txt_testo.setText(recensioneList.get(holder.getAdapterPosition()).getTesto());
        }
    }

    @Override
    public int getItemCount()
    {
        return recensioneList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_utente;
        TextView txt_testo;
        RatingBar rate_valutazione;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_testo = itemView.findViewById(R.id.txt_testo_recensione);
            txt_utente = itemView.findViewById(R.id.txt_utente_recensione);
            rate_valutazione = itemView.findViewById(R.id.rate_valutazione);
        }


    }
}

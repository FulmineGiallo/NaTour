package com.example.natour.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Segnalazione;

import java.util.List;

public class SegnalazioniAdapter extends RecyclerView.Adapter<SegnalazioniAdapter.ViewHolder>
{
    private List<Segnalazione> segnalazioneList;
    private FragmentManager manager;
    private ControllerVisualizzaItinerario controllerVisualizzaItinerario;
    private Context context;

    public SegnalazioniAdapter(List<Segnalazione> segnalazioneList, FragmentManager manager,
                               ControllerVisualizzaItinerario controllerVisualizzaItinerario, Context context){
        this.segnalazioneList = segnalazioneList;
        this.manager = manager;
        this.controllerVisualizzaItinerario = controllerVisualizzaItinerario;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_segnalazione, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(!segnalazioneList.isEmpty()){
            holder.txt_utente.setText(segnalazioneList.get(position).getUtente());
            holder.txt_testo.setText(segnalazioneList.get(position).getDescrizione());
        }
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_utente;
        TextView txt_testo;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_testo = itemView.findViewById(R.id.txt_testo_segnalazione);
            txt_utente = itemView.findViewById(R.id.txt_utente_segnalazione);
        }
    }
}

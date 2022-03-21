package com.example.natour.view.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.natour.R;
import com.example.natour.controller.ControllerInterface;
import com.example.natour.model.Itinerario;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>
{
    private Fragment fragment;
    private List<Itinerario> itinerari;
    private FragmentManager manager;
    private ControllerInterface controllerAdapter;

    //TODO: trovare un modo per aggiungere dinamicamente le immagini e aggiornarle



    public MasonryAdapter(Fragment fragment, ArrayList<Itinerario> itinerari, FragmentManager fragmentManager, ControllerInterface controller)
    {
        this.fragment = fragment;
        this.itinerari = itinerari;
        this.manager = fragmentManager;
        this.controllerAdapter = controller;
    }

    @Override
    public MasonryAdapter.MasonryView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        MasonryView masonryView = new MasonryView(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(@NonNull MasonryAdapter.MasonryView holder, int position)
    {
        if(!itinerari.isEmpty()){
            Glide
                    .with(fragment)
                    .load(itinerari.get(holder.getAdapterPosition()).getImmagini().get(0).getURL())
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
            holder.textView.setText(itinerari.get(holder.getAdapterPosition()).getNome());
            switch (itinerari.get(holder.getAdapterPosition()).getDifficoltà()){
                case 1:
                    holder.difficolta.setColorFilter(fragment.requireContext().getResources().getColor(R.color.facile));
                    break;
                case 2:
                    holder.difficolta.setColorFilter(fragment.requireContext().getResources().getColor(R.color.dilettante));
                    break;
                case 3:
                    holder.difficolta.setColorFilter(fragment.requireContext().getResources().getColor(R.color.intermedio));
                    break;
                case 4:
                    holder.difficolta.setColorFilter(fragment.requireContext().getResources().getColor(R.color.difficile));
                    break;
                case 5:
                    holder.difficolta.setColorFilter(fragment.requireContext().getResources().getColor(R.color.esperto));
                    break;
            }
        }

        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i("position", String.valueOf(holder.getAdapterPosition()));
                /* Mettere nel put extra l'itinerario e fare l'intent a visualizzaItinerarioActivity */
                controllerAdapter.visualizzaTrigger(itinerari.get(holder.getAdapterPosition()).getNome());
                controllerAdapter.visualizzaItinerario(itinerari.get(holder.getAdapterPosition()));
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return itinerari.size();
    }

    class MasonryView extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        ImageView difficolta;

        public MasonryView(@NonNull View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            difficolta = (ImageView) itemView.findViewById(R.id.homepage_difficolta);
        }
    }
}


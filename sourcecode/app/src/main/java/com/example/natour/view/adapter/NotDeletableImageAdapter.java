package com.example.natour.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Immagine;

import java.util.List;

public class NotDeletableImageAdapter extends RecyclerView.Adapter<NotDeletableImageAdapter.ViewHolder>
{
    private List<Immagine> imageList;
    private FragmentManager manager;
    private ControllerVisualizzaItinerario controllerVisualizzaItinerario;
    private Context context;

    public NotDeletableImageAdapter(List<Immagine> uriList, FragmentManager fragmentManager, ControllerVisualizzaItinerario controllerVisualizzaItinerario, Context context)
    {
        imageList = uriList;
        this.manager = fragmentManager;
        this.controllerVisualizzaItinerario = controllerVisualizzaItinerario;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image, parent, false);
            return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(!imageList.isEmpty())
            Glide
                .with(context)
                .load(imageList.get(holder.getAdapterPosition()).getURL())
                .centerCrop()
                .placeholder(android.R.drawable.spinner_background)
                .into(holder.imageView);
    }



    @Override
    public int getItemCount()
    {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        ImageView deleteItinerario;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_itinerario_percorso);
            deleteItinerario = itemView.findViewById(R.id.deleteItinerario);
            deleteItinerario.setVisibility(View.INVISIBLE);
        }

        public ImageView getImageView()
        {
            return imageView;
        }


    }


}

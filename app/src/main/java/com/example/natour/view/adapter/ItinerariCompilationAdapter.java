package com.example.natour.view.adapter;

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
import com.example.natour.R;
import com.example.natour.model.Itinerario;

import java.util.List;

public class ItinerariCompilationAdapter extends RecyclerView.Adapter<ItinerariCompilationAdapter.ViewHolder>
{

    List<Itinerario> itinerarioList;
    Fragment fragment;

    public ItinerariCompilationAdapter(List<Itinerario> itinerarioList, Fragment fragment)
    {
        this.itinerarioList = itinerarioList;
        this.fragment = fragment;
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
                    .with(fragment)
                    .load(itinerarioList.get(position).getImmagini().get(0).getURL())
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(holder.img_itinerario);
        }
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

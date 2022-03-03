package com.example.natour.view.adapter;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
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
import com.example.natour.R;
import com.example.natour.controller.ControllerHomePage;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Tab.HomePageFragment;

import java.util.List;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>
{
    private Fragment fragment;
    private List<Itinerario> itinerari;
    private List<Immagine> immagineList;
    private FragmentManager manager;
    private ControllerHomePage controllerHomePage;

    //TODO: trovare un modo per aggiungere dinamicamente le immagini e aggiornarle

    int[] imgList = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
                     R.drawable.img7, R.drawable.img8};

    String[] nameList = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"};



    public MasonryAdapter(HomePageFragment fragment, List<Itinerario> itinerari, FragmentManager fragmentManager, ControllerHomePage controllerHomePage, List<Immagine> immagineList)
    {
        this.fragment = fragment;
        this.itinerari = itinerari;
        this.immagineList = immagineList;
        this.manager = fragmentManager;
        this.controllerHomePage = controllerHomePage;
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

        holder.imageView.setImageResource(imgList[position]);
        if(!immagineList.isEmpty())
            Glide
                .with(fragment)
                .load(immagineList.get(position).getURL())
                .centerCrop()
                .placeholder(android.R.drawable.spinner_background)
                .into(holder.imageView);
        if(!itinerari.isEmpty()){
            holder.textView.setText(itinerari.get(position).getNome());
            holder.difficolta.setAlpha(0.80f);
            switch (itinerari.get(position).getDifficoltà()){
                case 1:
                    holder.difficolta.getBackground().setColorFilter(fragment.requireContext().getResources().getColor(R.color.facile), PorterDuff.Mode.ADD);
                    break;
                case 2:
                    holder.difficolta.getBackground().setColorFilter(fragment.requireContext().getResources().getColor(R.color.dilettante), PorterDuff.Mode.ADD);
                    break;
                case 3:
                    holder.difficolta.getBackground().setColorFilter(fragment.requireContext().getResources().getColor(R.color.intermedio), PorterDuff.Mode.ADD);
                    break;
                case 4:
                    holder.difficolta.getBackground().setColorFilter(fragment.requireContext().getResources().getColor(R.color.difficile), PorterDuff.Mode.ADD);
                    break;
                case 5:
                    holder.difficolta.getBackground().setColorFilter(fragment.requireContext().getResources().getColor(R.color.esperto), PorterDuff.Mode.ADD);
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
                controllerHomePage.visualizzaItinerario(itinerari.get(holder.getAdapterPosition()));
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


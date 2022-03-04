package com.example.natour.view.adapter;

import android.text.NoCopySpan;
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
import com.example.natour.controller.ControllerProfile;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Tab.HomePageFragment;
import com.example.natour.view.Tab.ProfileFragment;

import java.util.List;


public class ProfileAdapter
{
    private Fragment fragment;
    private List<Itinerario> itinerari;
    private List<Immagine> immagineList;
    private FragmentManager manager;
    private ControllerProfile controllerProfile;
    private ControllerHomePage controllerHomePage;

    int[] imgList = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8};

    public ProfileAdapter(ProfileFragment fragment, List<Itinerario> itinerari, FragmentManager fragmentManager, ControllerProfile controllerProfile, List<Immagine> immagineList)
    {
        this.fragment = fragment;
        this.itinerari = itinerari;
        this.immagineList = immagineList;
        this.manager = fragmentManager;
        this.controllerProfile = controllerProfile;
    }

    public ProfileAdapter.ProfileView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        ProfileAdapter.ProfileView profileView = new ProfileAdapter.ProfileView(layoutView);
        return profileView;
    }

    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileView holder, int position)
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
            switch (itinerari.get(position).getDifficoltà()){
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
                controllerHomePage.visualizzaItinerario(itinerari.get(holder.getAdapterPosition()));
            }
        });
    }

    public int getItemCount()
    {
        return itinerari.size();
    }


    class ProfileView extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        ImageView difficolta;

        public ProfileView(@NonNull View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            difficolta = (ImageView) itemView.findViewById(R.id.homepage_difficolta);
        }
    }
}

package com.example.natour.view.adapter;

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
import com.example.natour.controller.ControllerProfile;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Profile.ProfileFragment;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileView>
{
    private Fragment fragment;
    private List<Itinerario> itinerari;
    private List<Immagine> immagineList;
    private FragmentManager manager;
    private ControllerProfile controllerProfile;

    int[] imgList = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11};

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
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_profile, parent, false);
        ProfileAdapter.ProfileView profileView = new ProfileAdapter.ProfileView(layoutView);
        return profileView;
    }

    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileView holder, int position)
    {
        if(!itinerari.isEmpty()){
            holder.textViewProfile.setText(itinerari.get(position).getNome());
            Glide
                    .with(fragment)
                    .load(itinerari.get(position).getImmagini().get(0).getURL())
                    .centerCrop()
                    .error(R.drawable.placeholder)
                    .placeholder(android.R.drawable.spinner_background)
                    .into(holder.imageViewProfile);
        }

        holder.imageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i("position", String.valueOf(holder.getAdapterPosition()));
                /* Mettere nel put extra l'itinerario e fare l'intent a visualizzaItinerarioActivity */
                controllerProfile.visualizzaItinerario(itinerari.get(holder.getAdapterPosition()));
            }
        });
    }

    public int getItemCount()
    {
        return itinerari.size();
    }


    class ProfileView extends RecyclerView.ViewHolder
    {
        ImageView imageViewProfile;
        TextView textViewProfile;

        public ProfileView(@NonNull View itemView)
        {
            super(itemView);
            imageViewProfile = (ImageView) itemView.findViewById(R.id.imageViewProfile);
            textViewProfile = (TextView) itemView.findViewById(R.id.textViewProfile);
        }
    }
}

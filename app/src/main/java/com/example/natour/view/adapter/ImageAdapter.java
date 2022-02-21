package com.example.natour.view.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.example.natour.model.Immagine;
import com.example.natour.view.dialog.ConfermaDialog;
import com.example.natour.view.dialog.ConfermaDialogInterfaccia;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    private List<Immagine> imageList;
    private FragmentManager manager;
    private ControllerItinerario controllerItinerario;
    public ImageAdapter(List<Immagine> uriList, FragmentManager fragmentManager, ControllerItinerario controllerItinerario)
    {
        imageList = uriList;
        this.manager = fragmentManager;
        this.controllerItinerario = controllerItinerario;
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

        holder.imageView.setImageURI(imageList.get(position).getUri());
        holder.deleteItinerario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ConfermaDialog conferma = new ConfermaDialog("Sei sicuro di voler cancellare questa foto?", new ConfermaDialogInterfaccia()
                {
                    @Override
                    public void actionConferma()
                    {
                        //Remove dalla lista
                        controllerItinerario.removeImageFromS3Bucket(imageList.get(holder.getAdapterPosition()).getKey());
                        imageList.remove(holder.getAdapterPosition());

                        //Aggiorna l'interfaccia
                        notifyItemRemoved(holder.getAdapterPosition());

                    }

                    @Override
                    public void actionAnnulla()
                    {

                    }
                });
                conferma.show(manager, null);
            }
        });
    }

    public void onDeleteImageView()
    {

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
        }

        public ImageView getImageView()
        {
            return imageView;
        }


    }


}

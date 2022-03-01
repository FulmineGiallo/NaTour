package com.example.natour.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerHomePage;
import com.example.natour.model.Itinerario;

import java.util.List;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>
{
    private Context context;
    private List<Itinerario> itinerari;
    private FragmentManager manager;
    private ControllerHomePage controllerHomePage;

    //TODO: trovare un modo per aggiungere dinamicamente le immagini e aggiornarle

    int[] imgList = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
                     R.drawable.img7, R.drawable.img8};

    String[] nameList = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"};



    public MasonryAdapter(List<Itinerario> itinerari, FragmentManager fragmentManager, ControllerHomePage controllerHomePage)
    {
        this.itinerari = itinerari;
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
        if(!itinerari.isEmpty())
            holder.textView.setText(itinerari.get(position).getNome());

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

        public MasonryView(@NonNull View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}


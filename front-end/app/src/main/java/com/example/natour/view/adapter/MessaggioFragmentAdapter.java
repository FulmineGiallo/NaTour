package com.example.natour.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.databinding.GridItemMessaggiUsersBinding;
import com.example.natour.model.Utente;
import com.example.natour.view.MessaggioActivity.ChatMessage;
import com.example.natour.view.MessaggioActivity.UserListener;

import java.util.List;

public class MessaggioFragmentAdapter extends RecyclerView.Adapter<MessaggioFragmentAdapter.ViewHolder>
{

    private final List<Utente> utenteList;
    private final UserListener userListener;
    private final List<ChatMessage> chatMessages;

    public MessaggioFragmentAdapter(List<Utente> utenteList, UserListener userListener, List<ChatMessage> chatMessages)
    {
        this.userListener = userListener;
        this.utenteList = utenteList;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        GridItemMessaggiUsersBinding binding = GridItemMessaggiUsersBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.setUserData(utenteList.get(holder.getAdapterPosition()));
        if(chatMessages != null)
            if(chatMessages.size() > holder.getAdapterPosition())
                holder.setData(chatMessages.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount()
    {
        return utenteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GridItemMessaggiUsersBinding binding;

        public ViewHolder(GridItemMessaggiUsersBinding gridItemMessaggiUsersBinding)
        {
            super(gridItemMessaggiUsersBinding.getRoot());
            binding = gridItemMessaggiUsersBinding;
        }

        void setUserData(Utente utente){
            String nomeCognome = utente.getNome() + " " + utente.getCognome();
            binding.txtNomeCognome.setText(nomeCognome);
            binding.getRoot().setOnClickListener(view -> userListener.onUserClicked(utente));
        }

        public void setData(ChatMessage chatMessage)
        {
            if(chatMessage != null){
                binding.txtRecentMessage.setText(chatMessage.message);
            }
        }
    }
}

package com.example.natour.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.databinding.GridItemMessaggiUsersBinding;
import com.example.natour.view.MessaggioActivity.ChatMessage;

import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder>
{

    private final List<ChatMessage> chatMessages;

    public RecentConversationAdapter(List<ChatMessage> chatMessages)
    {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new ConversationViewHolder(parent,
                GridItemMessaggiUsersBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position)
    {
        holder.setData(chatMessages.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount()
    {
        return chatMessages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{
        GridItemMessaggiUsersBinding binding;



        public ConversationViewHolder(@NonNull View itemView, GridItemMessaggiUsersBinding binding)
        {
            super(itemView);
            this.binding = binding;
        }

        public void setData(ChatMessage chatMessage){
            binding.txtNomeCognome.setText(chatMessage.conversationName);
            binding.txtRecentMessage.setText(chatMessage.message);
        }
    }
}

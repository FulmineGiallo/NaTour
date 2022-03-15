package com.example.natour.view.MessaggioActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.databinding.ActivityChatBinding;
import com.example.natour.model.Utente;
import com.example.natour.view.adapter.ChatAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity
{

    private Utente receiverUtente; //utente interlocutore
    private Utente senderUtente; //utente corrente
    private ActivityChatBinding binding;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        senderUtente = (Utente) getIntent().getSerializableExtra("utentecorrente");
        setListener();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if(error != null){
            return;
        }
        else if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Costanti.KEY_SENDER_ID);
                    chatMessage.reciverId = documentChange.getDocument().getString(Costanti.KEY_RECIEVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Costanti.KEY_MESSAGE);
                    chatMessage.timeStamp = documentChange.getDocument().getDate(Costanti.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
                Collections.sort(chatMessages, (obj1,obj2) -> obj1.timeStamp.compareTo(obj2.timeStamp));
                if(count == 0){
                    chatAdapter.notifyDataSetChanged();
                }
                else{
                    chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                    binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
                }
                binding.chatRecyclerView.setVisibility(View.VISIBLE);

            }
        }
    });

    private void listenMessages(){
        database.collection(Costanti.KEY_COLLECTION_CHAT)
                .whereEqualTo(Costanti.KEY_SENDER_ID, senderUtente.getToken())
                .whereEqualTo(Costanti.KEY_RECIEVER_ID, receiverUtente.getToken())
                .addSnapshotListener(eventListener);
        database.collection(Costanti.KEY_COLLECTION_CHAT)
                .whereEqualTo(Costanti.KEY_SENDER_ID, receiverUtente.getToken())
                .whereEqualTo(Costanti.KEY_RECIEVER_ID, senderUtente.getToken())
                .addSnapshotListener(eventListener);
    }

    public void loadReceiverDetails(){
        receiverUtente = (Utente) getIntent().getSerializableExtra("interlocutore");
        String nomeCognome = receiverUtente.getNome() + " " + receiverUtente.getCognome();
        binding.txtNomeutente.setText(nomeCognome);
    }

    private void setListener(){
        binding.btnIndietroMessaggio.setOnClickListener(view -> onBackPressed());
        binding.imgSendmessage.setOnClickListener(view -> sendMessage());
    }

    private void init(){
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, null, Costanti.KEY_SENDER);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage(){
        HashMap<String, Object> messaggio = new HashMap<>();
        messaggio.put(Costanti.KEY_SENDER_ID,senderUtente.getToken());
        messaggio.put(Costanti.KEY_RECIEVER_ID, receiverUtente.getToken());
        messaggio.put(Costanti.KEY_MESSAGE, binding.edtMessaggio.getText().toString());
        messaggio.put(Costanti.KEY_TIMESTAMP, new Date());
        database.collection(Costanti.KEY_COLLECTION_CHAT).add(messaggio);
        binding.edtMessaggio.setText(null);
    }

    private String getReadableTimeStamp(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy-hh:mm a", Locale.getDefault()).format(date);
    }

}
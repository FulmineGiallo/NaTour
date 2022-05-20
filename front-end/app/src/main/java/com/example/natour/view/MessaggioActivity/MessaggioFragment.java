package com.example.natour.view.MessaggioActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Tab.SharedViewModel;
import com.example.natour.view.adapter.MessaggioFragmentAdapter;
import com.example.natour.view.adapter.RecentConversationAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MessaggioFragment extends Fragment implements UserListener
{
    private TextView txtMessaggiVuoti;
    private LottieAnimationView lottieAnimationView;
    private final List<Utente> utenteList = new LinkedList<>();
    private RecyclerView recyclerView;
    private Utente utenteLoggato;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private FirebaseFirestore database;
    private RecentConversationAdapter recentConversationAdapter;


    public MessaggioFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_messaggio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        txtMessaggiVuoti = requireView().findViewById(R.id.txt_msgRicevuti);
        txtMessaggiVuoti.setVisibility(View.INVISIBLE);
        lottieAnimationView = requireView().findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.INVISIBLE);

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        setAdapter();


        /* Inizializzazione MESSAGGI */
        /* - Vedo se quell'utente ha messaggi altrimenti mostro la schermata di base */


        model.getUtente().observe(getViewLifecycleOwner(),
                utente ->
                {
                    utenteLoggato = utente;
                    getUtenti();
                    listenConversations();
                }
        );


    }

    public void init()
    {
        chatMessages = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(chatMessages);
        recyclerView = requireView().findViewById(R.id.rec_view_messaggi);
        recyclerView.setAdapter(recentConversationAdapter);
        database = FirebaseFirestore.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) ->
    {
        if (error != null)
            return;
        if (value != null){
            for (DocumentChange documentChange : value.getDocumentChanges())
            {
                if (documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    String senderId = documentChange.getDocument().getString(Costanti.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Costanti.KEY_RECIEVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.reciverId = receiverId;
                    if (utenteLoggato.getToken().equals(senderId))
                    {
                        chatMessage.conversationID = documentChange.getDocument().getString(Costanti.KEY_RECIEVER);
                    } else
                    {
                        chatMessage.conversationID = documentChange.getDocument().getString(Costanti.KEY_SENDER);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Costanti.KEY_LAST_MESSAGE);
                    chatMessage.timeStamp = documentChange.getDocument().getDate(Costanti.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED)
                {
                    for (int i = 0; i < chatMessages.size(); i++)
                    {
                        String senderId = documentChange.getDocument().getString(Costanti.KEY_SENDER);
                        String receiverId = documentChange.getDocument().getString((Costanti.KEY_RECIEVER));
                        if(chatMessages.get(i).senderId.equals(senderId) && chatMessages.get(i).reciverId.equals(receiverId)){
                            chatMessages.get(i).message = documentChange.getDocument().getString(Costanti.KEY_LAST_MESSAGE);
                            chatMessages.get(i).timeStamp = documentChange.getDocument().getDate(Costanti.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            //Collections.sort(chatMessages, (ob1,ob2) -> ob2.timeStamp.compareTo(ob1.timeStamp));
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            recyclerView.setVisibility(View.VISIBLE);
        }
    });

    private void listenConversations(){
        database.collection(Costanti.KEY_COLLECTION_PATH)
                .whereEqualTo(Costanti.KEY_SENDER_ID, utenteLoggato.getToken())
                .addSnapshotListener(eventListener);
        database.collection(Costanti.KEY_COLLECTION_PATH)
                .whereEqualTo(Costanti.KEY_RECIEVER_ID, utenteLoggato.getToken())
                .addSnapshotListener(eventListener);
    }

    private void setAdapter()
    {
        database = FirebaseFirestore.getInstance();
        recyclerView = requireView().findViewById(R.id.rec_view_messaggi);
        MessaggioFragmentAdapter messaggioFragmentAdapter = new MessaggioFragmentAdapter(utenteList, this, chatMessages);
        recyclerView.setAdapter(messaggioFragmentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void getUtenti()
    {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        utenteList.clear();
        chatMessages.clear();
        database.collection(Costanti.KEY_COLLECTION_PATH)
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                        {
                            Utente utente = new Utente();
                            if (Objects.equals(queryDocumentSnapshot.getString(Costanti.KEY_RECIEVER), utenteLoggato.getToken()))
                            {
                                utente.setToken(queryDocumentSnapshot.getString(Costanti.KEY_SENDER));
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.message = queryDocumentSnapshot.getString(Costanti.KEY_LAST_MESSAGE);
                                chatMessage.timeStamp = queryDocumentSnapshot.getDate(Costanti.KEY_TIMESTAMP);
                                chatMessage.senderId = queryDocumentSnapshot.getString(Costanti.KEY_SENDER);
                                chatMessage.reciverId = queryDocumentSnapshot.getString(Costanti.KEY_RECIEVER);
                                chatMessages.add(chatMessage);
                            }
                            else if (Objects.equals(queryDocumentSnapshot.getString(Costanti.KEY_SENDER), utenteLoggato.getToken()))
                            {
                                utente.setToken(queryDocumentSnapshot.getString(Costanti.KEY_RECIEVER));
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.message = queryDocumentSnapshot.getString(Costanti.KEY_LAST_MESSAGE);
                                chatMessage.timeStamp = queryDocumentSnapshot.getDate(Costanti.KEY_TIMESTAMP);
                                chatMessage.senderId = queryDocumentSnapshot.getString(Costanti.KEY_RECIEVER);
                                chatMessage.reciverId = queryDocumentSnapshot.getString(Costanti.KEY_SENDER);
                                chatMessages.add(chatMessage);
                            }
                            else
                                continue;
                            new UtenteDAO().getNomeCognomeUtente(utente.getToken(), requireContext()).subscribe(
                                    result ->
                                    {
                                        utente.setNome(result.getString("nome"));
                                        utente.setCognome(result.getString("cognome"));
                                        utenteList.add(utente);
                                        if (utenteList.size() > 0)
                                        {
                                            txtMessaggiVuoti.setVisibility(View.GONE);
                                            lottieAnimationView.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(utenteList.indexOf(utente));
                                        } else
                                        {
                                            txtMessaggiVuoti.setVisibility(View.VISIBLE);
                                            lottieAnimationView.setVisibility(View.VISIBLE);
                                        }
                                    },
                                    error ->
                                    {
                                    }
                            );
                        }


                    }
                });


    }

    @Override
    public void onUserClicked(Utente utente)
    {
        Intent intent = new Intent(requireActivity(), ChatActivity.class);
        intent.putExtra("interlocutore", utente);
        intent.putExtra("utentecorrente", utenteLoggato);
        startActivity(intent);
    }
}
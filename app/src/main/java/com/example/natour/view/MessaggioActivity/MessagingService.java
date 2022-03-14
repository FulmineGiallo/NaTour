package com.example.natour.view.MessaggioActivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MessagingService extends FirebaseMessagingService
{

    @Override
    public void onMessageSent(@NonNull String msgId)
    {
        super.onMessageSent(msgId);
        Log.i("FCM", "token: " + msgId);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message)
    {
        super.onMessageReceived(message);
        Log.i("FCM", "message: " + Objects.requireNonNull(message.getNotification()).getBody());
    }

    @Override
    public void onNewToken(@NonNull String token)
    {
        super.onNewToken(token);
    }


}

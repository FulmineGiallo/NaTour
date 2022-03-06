package com.example.natour.view.InserimentoItinerarioActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.example.natour.R;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Tab.TabActivity;

public class ItinerarioInseritoTransition extends AppCompatActivity
{

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerario_inserito_transition);

        Itinerario itinerario = (Itinerario) getIntent().getSerializableExtra("itinerario");

        new Handler().postDelayed(() ->
        {
            Intent intent = new Intent(this,TabActivity.class);
            intent.putExtra("itinerario",itinerario);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        },5000);
    }
}
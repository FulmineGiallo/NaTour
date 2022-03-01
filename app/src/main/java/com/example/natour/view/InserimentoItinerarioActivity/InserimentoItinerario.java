package com.example.natour.view.InserimentoItinerarioActivity;//package com.example.natour.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;

import org.osmdroid.views.MapView;

public class InserimentoItinerario extends AppCompatActivity
{
    private ControllerItinerario controllerItinerario;
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String token = getIntent().getStringExtra("token");
        setContentView(R.layout.activity_inserimento_itinerario);
        controllerItinerario = new ControllerItinerario(getSupportFragmentManager(), this, token);
        controllerItinerario.inizializzaInterfaccia();





    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 100)
            controllerItinerario.fileInserito(resultCode, resultCode, intent);

        if(requestCode == 20)
            controllerItinerario.insertGPX(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        controllerItinerario.removeOnBackPressedImage();
    }
}
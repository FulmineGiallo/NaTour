package com.example.natour.view.VisualizzaItinerario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.BuildConfig;
import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Tab.TabActivity;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class VisualizzaItinerarioActivity extends AppCompatActivity
{
    private TextView nomeUtente;
    private TextView nomeItinerario;
    private ImageView pallinoDifficolta;
    private TextView distanza;
    private TextView durata;
    private TextView descrizione;
    private MapView mappa;
    private ImageButton btn_indietro;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_itinerario2);

        Itinerario itinerario;
        itinerario = (Itinerario) getIntent().getSerializableExtra("itinerarioselezionato");
        nomeUtente = findViewById(R.id.txt_nomecognome);
        nomeItinerario = findViewById(R.id.txt_nomeitinerario);
        nomeItinerario.setText(itinerario.getNome());
        pallinoDifficolta = findViewById(R.id.img_difficolta);
        descrizione = findViewById(R.id.txt_descrizione);
        durata = findViewById(R.id.txt_durata);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mappa = findViewById(R.id.img_mappaitinerario);
        IMapController mapController = mappa.getController();
        mapController.setZoom(13.5);


        mappa.invalidate();

        btn_indietro = findViewById(R.id.btn_indietro);
        btn_indietro.setOnClickListener(view -> back());

        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(this, itinerario);
        controllerVisualizzaItinerario.getWaypointsFromItinerario();

        UtenteDAO utenteDAO = new UtenteDAO();
        PublishSubject<JSONObject> risposta = utenteDAO.getNomeCognomeUtente(itinerario.getFk_utente(), this);
        risposta.subscribe(
                data ->
                {
                    nomeUtente.setText(data.getString("nome") + " " + data.getString("cognome"));
                },
                error ->
                {

                }
        );

        switch (itinerario.getDifficoltà())
        {
            case 1:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.facile));
                break;
            case 2:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.dilettante));
                break;
            case 3:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.intermedio));
                break;
            case 4:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.difficile));
                break;
            case 5:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.esperto));
                break;
        }

        durata.setText("Durata" + " " + itinerario.getDurata() + " " + "min");
        descrizione.setText(itinerario.getDescrizione());

    }

    private void back()
    {
        startActivity(new Intent(this, TabActivity.class));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        back();
    }
}
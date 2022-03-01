package com.example.natour.view.VisualizzaItinerario;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.UtenteDAO;

import org.json.JSONObject;
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
        mappa = findViewById(R.id.img_mappaitinerario);
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

}
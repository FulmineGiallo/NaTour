package com.example.natour.view.VisualizzaItinerario;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.BuildConfig;
import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Tab.TabActivity;
import com.example.natour.view.dialog.RecensioneBottomSheet;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class VisualizzaItinerarioActivity extends AppCompatActivity implements RecensioneBottomSheet.CallbackRecensione
{
    private TextView nomeUtente;
    private TextView nomeItinerario;
    private ImageView pallinoDifficolta;
    private TextView distanza;
    private TextView durata;
    private TextView descrizione;
    private MapView mappa;
    private ImageButton btn_indietro;
    private ImageButton btn_addRecensione;
    private Itinerario itinerario;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_itinerario2);



        btn_addRecensione = findViewById(R.id.btn_addrecensione);
        itinerario = (Itinerario) getIntent().getSerializableExtra("itinerarioselezionato");
        nomeUtente = findViewById(R.id.txt_nomecognome);
        nomeItinerario = findViewById(R.id.txt_nomeitinerario);
        nomeItinerario.setText(itinerario.getNome());
        pallinoDifficolta = findViewById(R.id.img_difficolta);
        descrizione = findViewById(R.id.txt_descrizione);
        durata = findViewById(R.id.txt_durata);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mappa = findViewById(R.id.img_mappaitinerario);
        mapController = mappa.getController();

        mapController.setZoom(11.3);

        mappa.invalidate();

        btn_indietro = findViewById(R.id.btn_indietro);
        btn_indietro.setOnClickListener(view -> back());

        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(this, itinerario);
        controllerVisualizzaItinerario.getWaypointsFromItinerario();

        RecyclerView recyclerView = findViewById(R.id.rec_view_show_images);
        controllerVisualizzaItinerario.setAdapter(recyclerView);

        /* Set MAPPA */



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

        TextView text = findViewById(R.id.txt_difficolta);
        switch (itinerario.getDifficoltà())
        {
            case 1:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.facile));
                text.setText(R.string.string_facile);
                break;
            case 2:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.dilettante));
                text.setText(R.string.string_dilettante);
                break;
            case 3:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.intermedio));
                text.setText(R.string.string_intermedio);
                break;
            case 4:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.difficile));
                text.setText(R.string.string_difficile);
                break;
            case 5:
                pallinoDifficolta.setColorFilter(this.getResources().getColor(R.color.esperto));
                text.setText(R.string.string_esperto);
                break;
        }

        durata.setText("Durata" + " " + itinerario.getDurata() + " " + "min");
        descrizione.setText(itinerario.getDescrizione());

        /* OTTENGO LA LISTA DI IMMAGINI DI QUELL' ITINERARIO SE CI SONO */
        controllerVisualizzaItinerario.getImageItinerario();

        btn_addRecensione.setOnClickListener(v -> {
            new RecensioneBottomSheet().show(getSupportFragmentManager(),null);
        });


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

    public void setMappa()
    {
        Marker inizio = new Marker(mappa);
        Marker fine = new Marker(mappa);

        inizio.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_location_on_24));
        fine.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_finepercorso));
        inizio.setPosition(itinerario.getWaypoints().get(0));
        fine.setPosition(itinerario.getWaypoints().get(itinerario.getWaypoints().size() - 1));
        inizio.setTitle("inizio");
        fine.setTitle("fine");

        mappa.getOverlays().add(inizio);
        mappa.getOverlays().add(fine);
        mapController.setCenter(inizio.getPosition());
        OSRMRoadManager roadManager = new OSRMRoadManager(this , "MyOwnUserAgent/1.0");
        roadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT);

        new Thread(()->
        {
            Road road;
            Polyline roadOverlay = null;

            mappa.getOverlays().remove(roadOverlay);
            road = roadManager.getRoad(itinerario.getWaypoints());
            roadOverlay = RoadManager.buildRoadOverlay(road);
            mappa.getOverlays().add(roadOverlay);
            //per aggiornare l'UI della mappa è necessario farlo nel main thread
            this.runOnUiThread(() -> mappa.invalidate());

            /* Calcolo media tra due punti */
            double latInizio, lonInizio, latFine, lonFine;
            latInizio = itinerario.getWaypoints().get(0).getLatitude();
            lonInizio = itinerario.getWaypoints().get(0).getLongitude();
            latFine   = itinerario.getWaypoints().get(itinerario.getWaypoints().size()-1).getLatitude();
            lonFine   = itinerario.getWaypoints().get(itinerario.getWaypoints().size()-1).getLongitude();

            GeoPoint puntoMedio = new GeoPoint((latInizio + latFine) / 2, (lonInizio + lonFine) / 2);
            BoundingBox boundingBox = BoundingBox.fromGeoPointsSafe(itinerario.getWaypoints());

            //viene messo come centro della mappa il punto medio tra i marker
            runOnUiThread(() ->
            {
                mapController.setCenter(puntoMedio);
                mappa.zoomToBoundingBox(boundingBox, true);

            });
        }).start();
    }

    public void setImage(Immagine img)
    {
        Log.i("IMG IN ACTIVITY", img.toString());
        Marker imgMarker = new Marker(mappa);
        imgMarker.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_photo_location));
        imgMarker.setPosition(new GeoPoint(img.getLatitude(),img.getLongitude()));
        imgMarker.setImage(drawableFromUrl(img.getURL()));
        mappa.getOverlays().add(imgMarker);
        runOnUiThread(()-> mappa.invalidate());
    }

    public Drawable drawableFromUrl(String url)
    {
        Bitmap x = null;

        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            x = BitmapFactory.decodeStream(input);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return new BitmapDrawable(Resources.getSystem(), x);
    }

    @Override
    public void callback(int rate, String recensione)
    {
        Log.i("CALLBACK RECENSIONE", "bisogna inviare informazioni al database");
    }
}
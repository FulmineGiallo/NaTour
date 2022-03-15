package com.example.natour.view.VisualizzaItinerario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.BuildConfig;
import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Segnalazione;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Tab.TabActivity;
import com.example.natour.view.dialog.RecensioneBottomSheet;
import com.example.natour.view.dialog.SegnalazioneBottomSheet;
import com.google.android.material.button.MaterialButton;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class VisualizzaItinerarioActivity extends AppCompatActivity implements RecensioneBottomSheet.CallbackRecensione, SegnalazioneBottomSheet.CallbackSegnalazione
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
    private ImageButton btn_addSegnalazione;
    private ImageButton btn_messaggia;
    private Itinerario itinerario;
    private String token;
    private IMapController mapController;
    private TextView mediaRecensioni;
    private FrameLayout fotoVuote;
    private ControllerVisualizzaItinerario controllerVisualizzaItinerario;
    private TextView txt_disabili;
    private Button btn_addCompilation;
    private TextView recensionivuote;
    private ImageView imageInfo;
    private MaterialButton addphoto;
    private ProgressBar progMapLoading;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_itinerario2);
        btn_addRecensione = findViewById(R.id.btn_addrecensione);
        btn_addSegnalazione = findViewById(R.id.btn_segnala);
        btn_addCompilation = findViewById(R.id.btn_salvaitinerario);
        itinerario = (Itinerario) getIntent().getSerializableExtra("itinerarioselezionato");
        token = (String) getIntent().getSerializableExtra("token");
        addphoto = findViewById(R.id.addphoto);
        nomeUtente = findViewById(R.id.txt_nomecognome);
        nomeItinerario = findViewById(R.id.txt_nomeitinerario);
        nomeItinerario.setText(itinerario.getNome());
        pallinoDifficolta = findViewById(R.id.img_difficolta);
        descrizione = findViewById(R.id.txt_descrizione);
        durata = findViewById(R.id.txt_durata);
        mediaRecensioni = findViewById(R.id.txt_mediarecensioni);
        fotoVuote = findViewById(R.id.framephotovuote);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mappa = findViewById(R.id.img_mappaitinerario);
        mapController = mappa.getController();
        recensionivuote = findViewById(R.id.txt_recensioninondisponibili);
        imageInfo = findViewById(R.id.imginfo);
        mapController.setZoom(11.3);
        progMapLoading = findViewById(R.id.prog_map_loading2);
        mappa.invalidate();
        btn_messaggia = findViewById(R.id.img_mandamex);
        btn_messaggia.setOnClickListener(view -> controllerVisualizzaItinerario.addDataToFirestore(itinerario.getFk_utente()));

        btn_indietro = findViewById(R.id.btn_indietro);
        btn_indietro.setOnClickListener(view -> back());

        controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(this, itinerario, token);
        controllerVisualizzaItinerario.getWaypointsFromItinerario();

        RecyclerView recyclerView = findViewById(R.id.rec_view_show_images);
        controllerVisualizzaItinerario.setAdapter(recyclerView);

        RecyclerView recyclerRecensioni = findViewById(R.id.frame_recensioni);
        controllerVisualizzaItinerario.setRecensioniAdapter(recyclerRecensioni, recensionivuote, imageInfo);

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

        if (itinerario.getDescrizione() != null)
        {
            if(itinerario.getDescrizione().isEmpty())
                descrizione.setText("Descrizione non presente");
            else
                descrizione.setText(itinerario.getDescrizione());
        }


        txt_disabili = findViewById(R.id.text_disabili);
        if(itinerario.isAccessibilitaDisabili())
            txt_disabili.setText("Si");
        else
            txt_disabili.setText("No");



        /* OTTENGO LA LISTA DI IMMAGINI DI QUELL' ITINERARIO SE CI SONO */
        controllerVisualizzaItinerario.getImageItinerario(fotoVuote);
        controllerVisualizzaItinerario.getRecensioneItinerario(mediaRecensioni, recensionivuote, imageInfo);


        btn_addRecensione.setOnClickListener(v -> {
            new RecensioneBottomSheet().show(getSupportFragmentManager(),null);
        });
        btn_addSegnalazione.setOnClickListener(v -> {
            controllerVisualizzaItinerario.showSegnalazioni();
        });
        btn_addCompilation.setOnClickListener(v->{
            controllerVisualizzaItinerario.showCompilation();
        });

        addphoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerVisualizzaItinerario.addPhotoItinerario();
            }
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
        mappa.setMultiTouchControls(true);
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
    public void callbackRecensione(float rate, String recensione)
    {
        Log.i("CALLBACK RECENSIONE", "bisogna inviare informazioni al database");
        controllerVisualizzaItinerario.insertRecensione( rate, recensione, mediaRecensioni);
        recensionivuote.setVisibility(View.INVISIBLE);
        imageInfo.setVisibility(View.INVISIBLE);
    }
    public void nascondiInfo()
    {
        fotoVuote.setVisibility(View.INVISIBLE);
    }
    @Override
    public void callbackSegnalazione(String segnalazione, String titolo)
    {
        Log.i("CALLBACK Segnalazione", "bisogna inviare informazioni al database");
        controllerVisualizzaItinerario.insertSegnalazione(segnalazione,titolo);
    }

    public void showBottomSheetSegnalazione(List<Segnalazione> criminalRecord){
        new SegnalazioneBottomSheet(criminalRecord, controllerVisualizzaItinerario).show(getSupportFragmentManager(), null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 3)
            controllerVisualizzaItinerario.fileInserito(resultCode, resultCode, intent);

    }
    public void startMapLoading()
    {
        progMapLoading.setVisibility(View.VISIBLE);
    }

    public void stopMapLoading()
    {
        progMapLoading.setVisibility(View.GONE);
    }

    public void addPhotoMarker(Immagine uriImage, float[] latLong)
    {
        Marker photoMarker = new Marker(mappa);
        photoMarker.setPosition(new GeoPoint(latLong[0], latLong[1]));
        uriImage.setMarker(photoMarker);
        photoMarker.setIcon(AppCompatResources.getDrawable(this,R.drawable.ic_photo_location));
        try
        {
            InputStream is = this.getContentResolver().openInputStream(uriImage.getUri());
            photoMarker.setImage(Drawable.createFromStream(is, uriImage.getUri().toString()));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        mappa.getOverlays().add(photoMarker);
    }

    public void showSegnalazioniBadge()
    {
        findViewById(R.id.badge_segnalazioni).setVisibility(View.VISIBLE);
    }
}
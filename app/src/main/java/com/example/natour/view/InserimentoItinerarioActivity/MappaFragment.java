package com.example.natour.view.InserimentoItinerarioActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MappaFragment extends Fragment implements MapEventsReceiver, LocationListener
{
    private MapView map;
    private OverlayViewModel model;
    private Marker inizioPercorso;
    private Marker finePercorso;
    private LocationManager locManager;
    private IMapController mapController;
    private EditText edtInizio;
    private EditText edtFine;
    private ImageButton deleteMarkerInizio;
    private ImageButton deleteMarkerFine;
    private ArrayList<GeoPoint> waypoints;
    private LinkedList<Immagine> puntiImmagine;
    private Road road;
    private RoadManager roadManager;
    private Polyline roadOverlay;
    private ControllerItinerario controllerItinerario;

    private boolean isInizioMarkerDeleted = false;
    private boolean isWaypointsInitialized = false;


    public MappaFragment()
    {

    }

    public MappaFragment(ControllerItinerario controllerItinerario, LinkedList<Immagine> puntiImmagine, ArrayList<GeoPoint> waypoints)
    {

        this.waypoints = waypoints;
        this.puntiImmagine = puntiImmagine;
        if(!this.waypoints.isEmpty()) isWaypointsInitialized = true;
        this.controllerItinerario = controllerItinerario;
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
        return inflater.inflate(R.layout.fragment_mappa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Configurazione della mappa e del servizio di locazione
        String MY_USER_AGENT = "MyOwnUserAgent/1.0";
        Configuration.getInstance().setUserAgentValue(MY_USER_AGENT);
        roadManager = new OSRMRoadManager(requireContext(), MY_USER_AGENT);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);


        locManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        //viene preso il riferimento della mappa e creato l'overlay per la mappa
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map = requireView().findViewById(R.id.mapFragment);


        //si predispone l'oggetto per avere i due marker
        finePercorso = new Marker(map);
        inizioPercorso = new Marker(map);
        map.setMultiTouchControls(true);

        //si setta lo stato UI iniziale per la mappa
        mapController = map.getController();
        mapController.setZoom(10.5);
        mapController.setCenter(currentPositionPhone());
        Log.i("CURRENT", String.valueOf(currentPositionPhone()));

        //aggiunge lista preesistente di immagini
        if(!this.puntiImmagine.isEmpty()){
            for(Immagine img : this.puntiImmagine) regeneratePhotoMarker(img);
        }

        //comando usato per aggiornare la mappa
        map.invalidate();

        //viene aggiutno l'overlay per permettere eventi come il tocco
        map.getOverlays().add(0, mapEventsOverlay);


        //inizializzazione del viewmodel per gestire i marker
        model = new ViewModelProvider(requireActivity()).get(OverlayViewModel.class);

        //Observers per i cambiamenti al viewmodel
        model.getInizio().observe(getViewLifecycleOwner(),
                puntoIniziale -> inizializzaPunto(puntoIniziale, inizioPercorso, R.drawable.ic_baseline_location_on_24, "Punto Iniziale", edtInizio, deleteMarkerInizio)
        );
        model.getFine().observe(getViewLifecycleOwner(),
                puntoFinale ->{
                    inizializzaPunto(puntoFinale, finePercorso, R.drawable.ic_finepercorso, "Punto Finale", edtFine, deleteMarkerFine);
                    if(isWaypointsInitialized) isWaypointsInitialized = false;
                }
        );


    }

    private void regeneratePhotoMarker(Immagine img)
    {
        Marker newMarker = new Marker(map);
        newMarker.setPosition(img.getMarker().getPosition());
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        newMarker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_photo_location));
        ContentResolver cr = requireActivity().getContentResolver();
        try
        {
            InputStream is = cr.openInputStream(img.getUri());
            newMarker.setImage(Drawable.createFromStream(is, img.getUri().toString()));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        addPhotoMarker(newMarker);
        img.setMarker(newMarker);
    }

    private void inizializzaPuntoImmagine(List<GeoPoint> lista)
    {
        for(GeoPoint photo : lista){
            Marker newPhoto = new Marker(map);
            newPhoto.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_insert_photo_24));
            newPhoto.setPosition(photo);
            newPhoto.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            if(!map.getOverlays().contains(newPhoto))
                map.getOverlays().add(newPhoto);
        }
        map.invalidate();
    }

    /*Metodo che gestisce l'impostazione dei marker nella mappa*/
    public void inizializzaPunto(GeoPoint punto, Marker marker, int p, String s, EditText edtPunto, ImageButton deleteButton)
    {
        //viene deciso il punto per cui il marker deve posizionarsi
        marker.setPosition(punto);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        //se il marker è stato già aggiunto allora è necessario modificarlo solamente
        if (!map.getOverlays().contains(marker))
            map.getOverlays().add(marker);

        //viene messo come centro il punto in cui preme l'utente
        mapController.setCenter(punto);
        map.invalidate();

        //viene messa l'icona e il titolo adatti al tipo di marker
        marker.setIcon(AppCompatResources.getDrawable(requireContext(), p));
        marker.setTitle(s);

        //viene modificato il testo che mostra l'indirizzo
        if (edtPunto != null)
        {
            edtPunto.setText(getAddress(punto));
            deleteButton.setVisibility(View.VISIBLE);
        }
        //viene aggiunto il punto come waypoint per fare il tracciato

        if(!isWaypointsInitialized)
            waypoints.add(punto);

        //nel caso sono presenti entrambi i marker viene tracciato il percorso
        if(map.getOverlays().contains(inizioPercorso) && map.getOverlays().contains(finePercorso))
        {
            //TODO: caricamento nel mentre cerca la strada;

            //la strada viene tracciata usando un servizio network
            Thread percorso = new Thread(() ->
            {
                road = roadManager.getRoad(waypoints);
                roadOverlay = RoadManager.buildRoadOverlay(road);
                if(!map.getOverlays().contains(roadOverlay))
                    map.getOverlays().add(roadOverlay);
                //per aggiornare l'UI della mappa è necessario farlo nel main thread
                requireActivity().runOnUiThread(() -> map.invalidate());
            });
            percorso.start();


            /* Calcolo media tra due punti */
            double latInizio, lonInizio, latFine, lonFine;
            latInizio = inizioPercorso.getPosition().getLatitude();
            lonInizio = inizioPercorso.getPosition().getLongitude();
            latFine   = finePercorso.getPosition().getLatitude();
            lonFine   = finePercorso.getPosition().getLongitude();

            GeoPoint puntoMedio = new GeoPoint((latInizio + latFine) / 2, (lonInizio + lonFine) / 2);

            //viene messo come centro della mappa il punto medio tra i marker
            mapController.setCenter(puntoMedio);
        }
    }

    /*questo metodo, ottenuto implementando MapEventsReceiver
    * permette di gestire l'evento del tocco sulla mappa
    * */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p)
    {
        //Toast.makeText(getContext(), "Tapped", Toast.LENGTH_SHORT).show();


        /* INSERISCO IL MARKER PER IL PUNTO INIZIALE */
        if (!map.getOverlays().contains(inizioPercorso))
        {
            model.setInizio(p);

        }
        /* INSERISCO IL MARKER PER IL PUNTO FINALE */
        else if (!map.getOverlays().contains(finePercorso))
        {
            model.setFine(p);
        }
        else
        {
            setNextRoute(p);
        }
        map.invalidate();
        return true;
    }

    private void setNextRoute(GeoPoint p)
    {
        map.getOverlays().remove(finePercorso);
        map.getOverlays().remove(roadOverlay);
        if(isInizioMarkerDeleted){
            waypoints.remove(finePercorso.getPosition());
            isInizioMarkerDeleted = false;
        }
        model.setFine(p);
    }


    public void setEditTextMappa(EditText edtInizioPercorso, EditText edtFinePercorso, ImageButton deleteMarkerInizio, ImageButton deleteMarkerFine)
    {
        this.edtInizio = edtInizioPercorso;
        this.edtFine = edtFinePercorso;
        this.deleteMarkerInizio = deleteMarkerInizio;
        this.deleteMarkerFine = deleteMarkerFine;

        deleteMarkerInizio.setOnClickListener(view -> eliminaMarker(deleteMarkerInizio, edtInizio, inizioPercorso, finePercorso)
        );
        deleteMarkerFine.setOnClickListener(view ->
            eliminaMarker(deleteMarkerFine, edtFine, finePercorso, inizioPercorso)
        );

        //listener per poter aggiungere un marker partendo da un indirizzo inserito manualmente
        TextInputLayout til_puntoIniziale = (TextInputLayout) edtInizioPercorso.getParent().getParent();
        til_puntoIniziale.setEndIconOnClickListener(view ->
        {
            if(!edtInizioPercorso.getText().toString().isEmpty()){
                map.getOverlays().remove(inizioPercorso);
                GeoPoint newMarker = getLocationFromAddress(edtInizioPercorso.getText().toString());
                if (newMarker != null)
                    model.setInizio(newMarker);
            }

        });
        TextInputLayout til_puntoFinale = (TextInputLayout) edtFinePercorso.getParent().getParent();
        til_puntoFinale.setEndIconOnClickListener(view ->
        {
            if(!edtFinePercorso.getText().toString().isEmpty()){
                map.getOverlays().remove(finePercorso);
                GeoPoint newMarker = getLocationFromAddress(edtFinePercorso.getText().toString());
                if (newMarker != null)
                    model.setFine(newMarker);
            }

        });

        edtInizioPercorso.setOnFocusChangeListener((view, b) -> til_puntoIniziale.setEndIconVisible(b));
        edtFinePercorso.setOnFocusChangeListener((view,b) -> til_puntoFinale.setEndIconVisible(b));

    }

    //metodo per l'eliminazione dei marker dall'apposito bottone
    private void eliminaMarker(@NonNull ImageButton deleteMarker, @NonNull EditText edtInizio, Marker marker, Marker altroMarker)
    {
        edtInizio.setText("");
        map.getOverlays().remove(marker);
        waypoints.clear();
        if((map.getOverlays().contains(marker) || map.getOverlays().contains(altroMarker))){
            if(altroMarker == finePercorso){
                isInizioMarkerDeleted = true;
            }
            waypoints.add(altroMarker.getPosition());
        }
        deleteMarker.setVisibility(View.INVISIBLE);
        map.getOverlays().remove(roadOverlay);
        map.invalidate();
    }

    @Override
    public boolean longPressHelper(GeoPoint p)
    {
        return false;
    }

    public GeoPoint currentPositionPhone()
    {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude;
            double latitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            return new GeoPoint(latitude, longitude);
        } else
            return new GeoPoint(40.839326626673405, 14.185227261143826);


    }



    public String getAddress(GeoPoint point)
    {
        return controllerItinerario.getAddress(point);
    }

    public GeoPoint getLocationFromAddress(String strAddress) {
        return controllerItinerario.getLocationFromAddress(strAddress);
    }


    @Override
    public void onLocationChanged(@NonNull Location location)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.i("MappaFragment", "something happened");
    }

    public void addPhotoMarker(Marker photoMarker)
    {

        Log.e("MAPPA FRAGMENT", String.valueOf(photoMarker.getPosition().getLatitude()));
        map.getOverlays().add(photoMarker);
        map.invalidate();

    }

    public void removePhotoMarker(Immagine img){
        if(img.getMarker() != null){
            map.getOverlays().remove(img.getMarker());
            map.invalidate();
        }
        puntiImmagine.remove(img);


    }

    public Marker createPhotoMarker(GeoPoint geoPoint, Immagine img)
    {
        Marker photoMarker = new Marker(map);
        photoMarker.setPosition(geoPoint);
        photoMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        photoMarker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_photo_location));
        photoMarker.setTitle("photo");
        ContentResolver cr = requireActivity().getContentResolver();
        try
        {
            InputStream is = cr.openInputStream(img.getUri());
            photoMarker.setImage(Drawable.createFromStream(is, img.getUri().toString()));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return photoMarker;
    }

    public void addPhotoPoint(Immagine img)
    {
        puntiImmagine.add(img);
        addPhotoMarker(img.getMarker());
    }

    public void setGPXPercorso(ArrayList<GeoPoint> track)
    {
        Thread percorso = new Thread(() ->
        {
            road = roadManager.getRoad(track);
            roadOverlay = RoadManager.buildRoadOverlay(road);
            if(!map.getOverlays().contains(roadOverlay))
                map.getOverlays().add(roadOverlay);
            //per aggiornare l'UI della mappa è necessario farlo nel main thread
            requireActivity().runOnUiThread(() -> map.invalidate());
        });
        percorso.start();
    }


}
package com.example.natour.view.InserimentoItinerarioActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private Button inserisciPercorso;
    private ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
    private Road road;
    private RoadManager roadManager;
    private Polyline roadOverlay;
    public MappaFragment()
    {

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
        map = (MapView) requireView().findViewById(R.id.mapFragment);


        //si predispone l'oggetto per avere i due marker
        finePercorso = new Marker(map);
        inizioPercorso = new Marker(map);
        map.setMultiTouchControls(true);

        //si setta lo stato UI iniziale per la mappa
        mapController = map.getController();
        mapController.setZoom(10.5);
        mapController.setCenter(currentPositionPhone());
        Log.i("CURRENT", String.valueOf(currentPositionPhone()));

        //comando usato per aggiornare la mappa
        map.invalidate();

        //viene aggiutno l'overlay per permettere eventi come il tocco
        map.getOverlays().add(0, mapEventsOverlay);


        //inizializzazione del viewmodel per gestire i marker
        model = new ViewModelProvider(requireActivity()).get(OverlayViewModel.class);

        //Observers per i cambiamenti al viewmodel
        model.getInizio().observe(getViewLifecycleOwner(),
                puntoIniziale ->
                    inizializzaPunto(puntoIniziale, inizioPercorso, R.drawable.ic_baseline_location_on_24, "Punto Iniziale", edtInizio, deleteMarkerInizio)
        );
        model.getFine().observe(getViewLifecycleOwner(),
                puntoFinale ->
                    inizializzaPunto(puntoFinale, finePercorso, R.drawable.ic_finepercorso, "Punto Finale", edtFine, deleteMarkerFine)
        );


    }

    /*Metodo che gestisce l'impostazione dei marker nella mappa*/
    private void inizializzaPunto(GeoPoint punto, Marker marker, int p, String s, EditText edtInizio, ImageButton deleteButton)
    {
        marker.setPosition(punto);

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        if (!map.getOverlays().contains(marker))
            map.getOverlays().add(marker);

        mapController.setCenter(punto);
        map.invalidate();

        marker.setIcon(AppCompatResources.getDrawable(requireContext(), p));
        marker.setTitle(s);

        if (edtInizio != null)
        {
            edtInizio.setText(getAddress(punto));
            deleteButton.setVisibility(View.VISIBLE);
        }
        waypoints.add(punto);

        if(map.getOverlays().contains(inizioPercorso) && map.getOverlays().contains(finePercorso))
        {
            //TODO: caricamento nel mentre cerca la strada;

            Thread percorso = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    road = roadManager.getRoad(waypoints);
                    roadOverlay = RoadManager.buildRoadOverlay(road);
                    map.getOverlays().add(roadOverlay);
                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            map.invalidate();
                        }
                    });
                }
            });
            percorso.start();

            /* Calcolo media tra due punti */
            double latInizio, lonInizio, latFine, lonFine;
            latInizio = inizioPercorso.getPosition().getLatitude();
            lonInizio = inizioPercorso.getPosition().getLongitude();
            latFine   = finePercorso.getPosition().getLatitude();
            lonFine   = finePercorso.getPosition().getLongitude();

            GeoPoint puntoMedio = new GeoPoint((latInizio + latFine) / 2, (lonInizio + lonFine) / 2);

            mapController.setCenter(puntoMedio);
        }
    }

    /*questo metodo, ottenuto implementando MapEventsReceiver
    * permette di gestire l'evento del tocco sulla mappa
    * */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p)
    {
        Toast.makeText(getContext(), "Tapped", Toast.LENGTH_SHORT).show();


        /* INSERISCO IL MARKER PER IL PUNTO INIZIALE */
        if (!map.getOverlays().contains(inizioPercorso))
        {
            model.setInizio(p);

        }
        else if (!map.getOverlays().contains(finePercorso))
        {
            model.setFine(p);
        }
        map.invalidate();
        Log.i("TAP", "");
        return true;
    }



    public void setEditTextMappa(EditText edtInizioPercorso, EditText edtFinePercorso, ImageButton deleteMarkerInizio, ImageButton deleteMarkerFine)
    {
        this.edtInizio = edtInizioPercorso;
        this.edtFine = edtFinePercorso;
        this.deleteMarkerInizio = deleteMarkerInizio;
        this.deleteMarkerFine = deleteMarkerFine;

        deleteMarkerInizio.setOnClickListener(view ->
            eliminaMarker(deleteMarkerInizio, edtInizio, inizioPercorso)
        );
        deleteMarkerFine.setOnClickListener(view ->
            eliminaMarker(deleteMarkerFine, edtFine, finePercorso)
        );

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

    private void eliminaMarker(@NonNull ImageButton deleteMarker, @NonNull EditText edtInizio, Marker inizioPercorso)
    {
        edtInizio.setText("");
        map.getOverlays().remove(inizioPercorso);
        deleteMarker.setVisibility(View.INVISIBLE);
        waypoints.remove(inizioPercorso.getPosition());
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

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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


    @Override
    public void onLocationChanged(@NonNull Location location)
    {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider)
    {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    public String getAddress(GeoPoint point)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        String address = null;
        String city = null;
        String postalCode = null;
        try
        {
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            postalCode = addresses.get(0).getPostalCode();
        } catch (IOException e)
        {
            e.printStackTrace();
        }




        return address + city + postalCode;


    }

    public GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        GeoPoint p1;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
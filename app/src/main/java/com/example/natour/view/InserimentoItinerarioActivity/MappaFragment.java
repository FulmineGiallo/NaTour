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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
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
        Configuration.getInstance().setUserAgentValue("MyOwnUserAgent/1.0");
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getContext(), this);

        map = (MapView) getView().findViewById(R.id.mapFragment);
        map.setMultiTouchControls(true);


        mapController = map.getController();
        mapController.setZoom(10.5);
        mapController.setCenter(currentPositionPhone());
        Log.i("CURRENT", String.valueOf(currentPositionPhone()));
        map.invalidate();


        map.getOverlays().add(0, mapEventsOverlay);

        model = new ViewModelProvider(requireActivity()).get(OverlayViewModel.class);


        try
        {
            model.getInizio().observe(getViewLifecycleOwner(),
                    puntoIniziale ->
                    {
                        inizioPercorso = new Marker(map);
                        inizioPercorso.setPosition(puntoIniziale);

                        inizioPercorso.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        map.getOverlays().add(inizioPercorso);

                        map.invalidate();

                        inizioPercorso.setIcon(getResources().getDrawable(R.drawable.ic_baseline_location_on_24));
                        inizioPercorso.setTitle("Punto Iniziale");

                        if(edtInizio != null)
                        {
                            edtInizio.setText(getAddress(puntoIniziale));
                            deleteMarkerInizio.setVisibility(View.VISIBLE);
                        }

                        Log.e("ESEGUITO PRIMA VOLTA I", "eeeee");


                    }
            );
            model.getFine().observe(getViewLifecycleOwner(),
                    puntoFinale ->
                    {

                        finePercorso = new Marker(map);
                        finePercorso.setPosition(puntoFinale);

                        finePercorso.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        map.getOverlays().add(finePercorso);

                        map.invalidate();

                        finePercorso.setIcon(getResources().getDrawable(R.drawable.ic_finepercorso));
                        finePercorso.setTitle("Punto Finale");

                        if(edtFine != null)
                        {
                            edtFine.setText(getAddress(puntoFinale));
                            deleteMarkerFine.setVisibility(View.VISIBLE);
                        }
                        Log.e("ESEGUITO PRIMA VOLTA F", "eeeee");
                    }
            );
        }
        catch (NullPointerException ex)
        {
            Log.e("EX", ex.toString());
        }

    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p)
    {
        Toast.makeText(getContext(), "Tapped", Toast.LENGTH_SHORT).show();


        /* INSERISCO IL MARKER PER IL PUNTO INIZIALE */
        if (inizioPercorso == null)
        {
            model.setInizio(p);

        }
        else if (finePercorso == null)
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

        deleteMarkerInizio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                edtInizio.setText("");
                map.getOverlays().remove(inizioPercorso);
                inizioPercorso = null;


                deleteMarkerInizio.setVisibility(View.INVISIBLE);
                map.invalidate();
            }
        });
        deleteMarkerFine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                edtFine.setText("");
                map.getOverlays().remove(finePercorso);
                finePercorso = null;

                deleteMarkerFine.setVisibility(View.INVISIBLE);
                map.invalidate();
            }
        });
    }

    @Override
    public boolean longPressHelper(GeoPoint p)
    {
        return false;
    }

    public GeoPoint currentPositionPhone()
    {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = 0;
            double latitude  = 0;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            return new GeoPoint(latitude, longitude);
        }
        else
            return new GeoPoint(40.839326626673405, 14.185227261143826);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    public String getAddress(GeoPoint point)
    {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try
        {
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String postalCode = addresses.get(0).getPostalCode();

        return address + city + postalCode;



    }

   /* @Override
    public void onStop()
    {
        super.onStop();

        Log.i("DENTRO SAVE", "ccc");
        if(inizioPercorso == null  || finePercorso == null)
        {
            getActivity().getViewModelStore().clear();
        }
        Log.i("Fuori SAVE", "ccc");
    }*/
}
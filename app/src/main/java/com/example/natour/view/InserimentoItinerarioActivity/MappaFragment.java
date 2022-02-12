package com.example.natour.view.InserimentoItinerarioActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MappaFragment extends Fragment implements MapEventsReceiver, LocationListener
{
    private MapView map;
    private OverlayViewModel model;
    private Marker inizioPercorso;
    private Marker finePercorso;
    private LocationManager locManager;
    private IMapController mapController;

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

        map = (MapView) getView().findViewById(R.id.mapFragment);
        map.setMultiTouchControls(true);


        mapController = map.getController();
        mapController.setZoom(10.5);
        mapController.setCenter(currentPositionPhone());
        Log.i("CURRENT", String.valueOf(currentPositionPhone()));
        map.invalidate();

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getContext(), this);
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
                    }
            );
        } catch (NullPointerException ex)
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
            inizioPercorso = new Marker(map);
            inizioPercorso.setPosition(p);

            inizioPercorso.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(inizioPercorso);

            map.invalidate();

            inizioPercorso.setIcon(getResources().getDrawable(R.drawable.ic_baseline_location_on_24));
            inizioPercorso.setTitle("Punto Iniziale");

            model.setInizio(p);

        } else if (finePercorso == null)
        {
            finePercorso = new Marker(map);
            finePercorso.setPosition(p);

            finePercorso.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(finePercorso);

            map.invalidate();

            finePercorso.setIcon(getResources().getDrawable(R.drawable.ic_finepercorso));
            finePercorso.setTitle("Punto Finale");

            model.setFine(p);
        }
        map.invalidate();
        return true;
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
}
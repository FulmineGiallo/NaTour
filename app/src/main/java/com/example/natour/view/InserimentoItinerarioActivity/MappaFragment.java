package com.example.natour.view.InserimentoItinerarioActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.natour.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MappaFragment extends Fragment implements MapEventsReceiver
{



    public MappaFragment()
    {

    }

    public static MappaFragment newInstance(String param1, String param2)
    {
        MappaFragment fragment = new MappaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        MapView map = (MapView) getActivity().findViewById(R.id.mapFragment);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(48.13, -1.63);
        IMapController mapController = map.getController();
        mapController.setZoom(10.5);
        mapController.setCenter(startPoint);


        Overlay touchOverlay = new Overlay(getActivity().getApplicationContext())
        {
            ItemizedIconOverlay<OverlayItem> item = null;

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView)
            {
                final Drawable marker = getActivity().getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_location_on_24);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());

                String longitudine = Double.toString((((double) loc.getLongitude())));
                String lati = Double.toString((((double) loc.getLatitude())));

                ArrayList<OverlayItem> listOverlay = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("","", loc);

                Log.i("CLICK MAPPA", longitudine + lati);
                mapItem.setMarker(marker);
                listOverlay.add(mapItem);

                if(item == null)
                {
                    item = new ItemizedIconOverlay<OverlayItem>(getActivity().getApplicationContext(), listOverlay, null);
                    mapView.getOverlays().remove(item);
                    mapView.invalidate();
                }
                else
                {
                    mapView.getOverlays().remove(item);
                    mapView.invalidate();
                    item = new ItemizedIconOverlay<OverlayItem>(getActivity().getApplicationContext(), listOverlay, null);
                    mapView.getOverlays().add(item);
                }

                return super.onSingleTapConfirmed(e, mapView);
            }
        };


        map.getOverlays().add(touchOverlay);

    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p)
    {
        Log.i("TAP", String.valueOf(p.getLatitude()) + String.valueOf(p.getLongitude()));
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p)
    {
        return false;
    }
}
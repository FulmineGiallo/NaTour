package com.example.natour.view;//package com.example.natour.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.example.natour.R;
import com.google.android.material.slider.Slider;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class InserimentoItinerario extends AppCompatActivity {

    Slider difficolta;
    MapView map = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_itinerario);

        difficolta = findViewById(R.id.slider_difficoltà);
        difficolta.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                switch ((int) value)
                {
                    case 1:
                        slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.facile)));
                        slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.facile)));
                        break;
                    case 2:
                        slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.dilettante)));
                        slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.dilettante)));
                        break;
                    case 3:
                        slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.intermedio)));
                        slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.intermedio)));
                        break;
                    case 4:
                        slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.difficile)));
                        slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.difficile)));
                        break;
                    case 5:
                        slider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.esperto)));
                        slider.setHaloTintList(ColorStateList.valueOf(getResources().getColor(R.color.esperto)));
                        break;
                }
            }
        });


        /* SEZIONE MAPPA */


        map = findViewById(R.id.mapview);
        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18);
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        GeoPoint point = new GeoPoint(40.8889946, 14.2455589);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);

        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);

        map.getController().setCenter(point);

        Overlay overlay = new Overlay(this)
        {
            ItemizedIconOverlay<OverlayItem> items = null;
            ArrayList<OverlayItem> markers;
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int)e.getX(), (int)e.getY());
                double longitude = loc.getLongitude();
                double latitude = loc.getLatitude();
                point.setLatitude(latitude);
                point.setLongitude(longitude);

                ArrayList<OverlayItem> markers = new ArrayList<>();
                OverlayItem item = new OverlayItem("", "", new GeoPoint(latitude, longitude));
                item.setMarker(ContextCompat.getDrawable(InserimentoItinerario.this, R.drawable.marker_default));
                markers.add(item);


                items = new ItemizedIconOverlay<>(InserimentoItinerario.this, markers, null);
                map.getOverlays().add(items);
                map.invalidate();


                return true;
            }

            public ArrayList<OverlayItem> getMarker()
            {
                return markers;
            }
        };

        map.getOverlays().add(overlay);

        /* --------------------------------------------------------------------------------------------*/
    }
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
package com.example.natour.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.natour.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private UiSettings setting;
    private EditText inizioPercorso;
    private EditText finePercorso;
    private ImageButton indietro;
    private Marker markerInizio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        inizioPercorso = findViewById(R.id.edt_inizioPercorso);
        finePercorso = findViewById(R.id.edt_finePercorso);
        indietro = findViewById(R.id.btn_indietro);
        /*indietro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });*/

        inizioPercorso.setFocusable(false);
        inizioPercorso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                inizioPercorso.setFocusable(true);
                inizioPercorso.setFocusableInTouchMode(true);
                inizioPercorso.setEnabled(true);
                inizioPercorso.requestFocus();
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(inizioPercorso, 0);
            }
        });
        finePercorso.setFocusable(false);
        finePercorso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                finePercorso.setFocusable(true);
                finePercorso.setFocusableInTouchMode(true);
                finePercorso.setEnabled(true);
                finePercorso.requestFocus();
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(finePercorso, 0);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        setting = mMap.getUiSettings();
        setting.setMapToolbarEnabled(false);
        setting.setZoomControlsEnabled(true);

        //TODO: mettere la current position dell'utente
        LatLng Napoli = new LatLng(40.88817802379429, 14.280845900795518);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Napoli));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {

                if(markerInizio != null)
                    markerInizio.remove();

                inizioPercorso.setText(getAddresMarker(getApplicationContext(), latLng));
                markerInizio = mMap.addMarker(new MarkerOptions()
                 .position(latLng));

            }
        });

        /*
        *Ottenere la posizione
        *Creare tracciato, con inzio(Marker) e fine
        * Le foto se hanno latlng possiamo mettere il marker sulla mappa
        **Far inserire marker sulla mappa, con allegato una foto con i metadati LatLng

        */
    }
    public String getAddresMarker(Context context, LatLng pos)
    {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressString = new String();
        try
        {
            List<Address> addressList = geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
            Address address = addressList.get(0);
            addressString = address.getAddressLine(0);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        return addressString;
    }


}
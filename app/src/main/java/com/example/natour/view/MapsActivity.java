package com.example.natour.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.example.natour.view.InserimentoItinerarioActivity.MappaFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Marker;

import org.osmdroid.config.Configuration;


public class MapsActivity extends AppCompatActivity
{

    private GoogleMap mMap;
    private UiSettings setting;
    private EditText inizioPercorso;
    private EditText finePercorso;
    private ImageButton indietro;
    private Marker markerInizio;
    private FrameLayout backContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        backContainer = findViewById(R.id.frameIndietro);
        inizioPercorso = findViewById(R.id.edt_inizioPercorso);
        finePercorso = findViewById(R.id.edt_finePercorso);
        indietro = findViewById(R.id.btn_indietro);
        backContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //animazione per la pulsazione del tasto back

                AnimationSet animationSet = new AnimationSet(true);
                Animation bigger = new ScaleAnimation(1f, 1.25f, // Start and end values for the X axis scaling
                        1f, 1.25f, // Start and end values for the Y axis scaling
                        Animation.ABSOLUTE, backContainer.getPivotX(), // Pivot point of X scaling
                        Animation.ABSOLUTE, backContainer.getPivotY());
                bigger.setDuration(500);
                Animation smaller = new ScaleAnimation(1f, 0, // Start and end values for the X axis scaling
                        1f, 0, // Start and end values for the Y axis scaling
                        Animation.ABSOLUTE, backContainer.getPivotX(), // Pivot point of X scaling
                        Animation.ABSOLUTE, backContainer.getPivotY());
                smaller.setDuration(200);
                smaller.setStartOffset(200);
                animationSet.addAnimation(bigger);
                animationSet.addAnimation(smaller);
                animationSet.setFillAfter(true);

                //fade out del tasto back

                Animation fadeOut = new AlphaAnimation(indietro.getAlpha(), 0f);
                fadeOut.setFillAfter(true);
                fadeOut.setDuration(500);

                backContainer.startAnimation(animationSet);
                //TODO: ritorno all'attività precedente
            }
        });

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

        Configuration.getInstance().setUserAgentValue("MyOwnUserAgent/1.0");
        FrameLayout map = this.findViewById(R.id.map);

        if(map != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.map, new MappaFragment());
            ft.commit();
        }

    }





}
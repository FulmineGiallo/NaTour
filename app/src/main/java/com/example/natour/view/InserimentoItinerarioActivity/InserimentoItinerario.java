package com.example.natour.view.InserimentoItinerarioActivity;//package com.example.natour.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.example.natour.view.MapsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.Slider;

public class InserimentoItinerario extends AppCompatActivity
{

    Slider difficolta;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    FrameLayout containerMappa;
    MaterialCardView boxMappa;
    ImageView buttonNascoto;

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

        boxMappa = findViewById(R.id.cardMap);
        containerMappa = findViewById(R.id.containerMappa);
        buttonNascoto = findViewById(R.id.btn_nascoto);
        if(findViewById(R.id.containerMappa) != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.containerMappa, new MappaFragment());
            ft.commit();
        }

        buttonNascoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
    }


}
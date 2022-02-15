package com.example.natour.view.InserimentoItinerarioActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.Slider;


public class InserimentoItinerarioFragment extends Fragment
{
    Slider difficolta;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    FrameLayout containerMappa;
    MaterialCardView boxMappa;
    ImageView buttonNascosto;
    InserimentoPercorsoFragment percorsoFragment;
    MappaFragment mappaFragment;

    public InserimentoItinerarioFragment()
    {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inserimento_itinerario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        difficolta = getView().findViewById(R.id.slider_difficoltà);
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

        boxMappa = getView().findViewById(R.id.cardMap);
        containerMappa = getView().findViewById(R.id.containerMappa);
        buttonNascosto = getView().findViewById(R.id.btn_nascoto);

        if(getView().findViewById(R.id.containerMappa) != null)
        {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mappaFragment = new MappaFragment();

            ft.add(R.id.containerMappa, mappaFragment);
            ft.commit();
        }


        buttonNascosto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(percorsoFragment == null)
                    percorsoFragment = new InserimentoPercorsoFragment(mappaFragment, InserimentoItinerarioFragment.this);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentMappa, percorsoFragment)
                        .addToBackStack(InserimentoItinerarioFragment.class.getSimpleName())
                        .commit();

            }
        });
    }



}
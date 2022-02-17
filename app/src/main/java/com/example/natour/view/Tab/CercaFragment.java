package com.example.natour.view.Tab;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.natour.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;


public class CercaFragment extends Fragment {

    private FloatingActionButton filtro;
    private NavigationView filtraView;
    private EditText posizione;
    private EditText durata;
    private Slider difficolta;
    private SwitchCompat disabili;
    private ImageView bottoneNascoto;



    public CercaFragment()
    {

    }

    public static CercaFragment newInstance(String param1, String param2)
    {
        CercaFragment fragment = new CercaFragment();
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

        return inflater.inflate(R.layout.fragment_cerca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        filtro = requireView().findViewById(R.id.btn_filter);
        filtraView = requireView().findViewById(R.id.viewFiltro);
        bottoneNascoto = requireView().findViewById(R.id.btn_nascotoFiltro);

        difficolta = requireView().findViewById(R.id.slider_difficoltà);
        difficolta.addOnChangeListener((slider, value, fromUser) ->
        {
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
        });

        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation salto =  new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                filtraView.getX(), Animation.RELATIVE_TO_PARENT, filtraView.getX(), Animation.RELATIVE_TO_PARENT,
                1f, Animation.RELATIVE_TO_PARENT, filtraView.getY() - 0.1f);
        salto.setDuration(500);
        salto.setRepeatMode(Animation.REVERSE);
        salto.setFillAfter(true);
        animationSet.addAnimation(salto);

        TranslateAnimation discesa = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                filtraView.getX(), Animation.RELATIVE_TO_PARENT, filtraView.getX(), Animation.RELATIVE_TO_PARENT,
                filtraView.getY() - 0.1f, Animation.RELATIVE_TO_PARENT, filtraView.getY());
        discesa.setStartOffset(500);
        discesa.setDuration(1000);
        discesa.setFillAfter(true);
        animationSet.addAnimation(discesa);



        filtro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                filtraView.setVisibility(View.VISIBLE);
                filtraView.startAnimation(animationSet);
                bottoneNascoto.setVisibility(View.VISIBLE);

            }
        });
        bottoneNascoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                filtraView.setVisibility(View.INVISIBLE);
                bottoneNascoto.setVisibility(View.INVISIBLE);
            }
        });

    }






}
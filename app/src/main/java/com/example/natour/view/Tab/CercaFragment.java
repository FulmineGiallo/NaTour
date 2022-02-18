package com.example.natour.view.Tab;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.natour.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;


public class CercaFragment extends Fragment {

    private static final String TAG = "CercaFragment";
    private FloatingActionButton filtro;
    private NavigationView filtraView;
    private EditText posizione;
    private EditText durata;
    private Slider difficolta;
    private SwitchCompat disabili;
    private ImageView bottoneNascoto;
    private SearchView searchView;


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

        searchView = requireView().findViewById(R.id.searchInput);
        TextView txt_cercaQui = requireView().findViewById(R.id.txt_cerca_qui);
        searchView.setOnQueryTextFocusChangeListener((view1, b) ->
        {
            Log.i(TAG,"focus change del search view");
            if(b)
                txt_cercaQui.setVisibility(View.INVISIBLE);
            else if(searchView.getQuery().length() == 0) txt_cercaQui.setVisibility(View.VISIBLE);


        });
        FrameLayout viewTop = requireView().findViewById(R.id.viewtop_cerca);
        viewTop.setOnClickListener((banner)->
                searchView.requestFocus());

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



        filtro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                filtraView.setVisibility(View.VISIBLE);
                filtraView.startAnimation(getPopUpAnimation());
                bottoneNascoto.setVisibility(View.VISIBLE);

            }
        });
        bottoneNascoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                filtraView.startAnimation(getCloseAnimation());
                filtraView.setVisibility(View.INVISIBLE);
                bottoneNascoto.setVisibility(View.INVISIBLE);
            }
        });

    }

    @NonNull
    private TranslateAnimation getCloseAnimation()
    {
        TranslateAnimation discesa = new TranslateAnimation(0,0,0,1000);
        discesa.setDuration(300);
        discesa.setInterpolator(new AccelerateInterpolator());
        return discesa;
    }

    @NonNull
    private AnimationSet getPopUpAnimation()
    {
        AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation salto =  new TranslateAnimation(0f,0f,1000,-100);
        salto.setDuration(300);
        salto.setFillAfter(true);
        salto.setInterpolator(new AccelerateInterpolator());
        animationSet.addAnimation(salto);

        TranslateAnimation discesa = new TranslateAnimation(0f,0f,-100,100);
        discesa.setStartOffset(300);
        discesa.setDuration(150);
        discesa.setFillAfter(true);
        animationSet.addAnimation(discesa);
        return animationSet;
    }


}
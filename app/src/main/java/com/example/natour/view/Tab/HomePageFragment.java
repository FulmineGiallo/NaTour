package com.example.natour.view.Tab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePageFragment extends Fragment
{
    RecyclerView mRecyclerView;
    FloatingActionButton fab;
    Intent intentInserimentoItinerario;
    TextView tokenLogin;
    Utente utente;

    public HomePageFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        fab = getView().findViewById(R.id.btn_inserimentoItinerario);
        intentInserimentoItinerario = new Intent(HomePageFragment.this.getActivity(), InserimentoItinerario.class);



        fab.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(intentInserimentoItinerario);
                return true;
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rec_view);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        MasonryAdapter adapter = new MasonryAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
    }
}
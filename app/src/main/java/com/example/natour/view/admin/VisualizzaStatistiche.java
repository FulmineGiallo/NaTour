package com.example.natour.view.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.natour.R;
import com.example.natour.controller.ControllerStatistiche;
import com.example.natour.databinding.FragmentVisualizzaStatisticheBinding;

public class VisualizzaStatistiche extends Fragment
{

    private FragmentVisualizzaStatisticheBinding binding;
    private ControllerStatistiche controllerStatistiche;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentVisualizzaStatisticheBinding.inflate(getLayoutInflater());

        controllerStatistiche = new ControllerStatistiche(this);

        controllerStatistiche.aggiornaStatisticheItinerario();

        binding.txtValItinerariTotale.setText("oh yes");


        return binding.getRoot();
    }
}
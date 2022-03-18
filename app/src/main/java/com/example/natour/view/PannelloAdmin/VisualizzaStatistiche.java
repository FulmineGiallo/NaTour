package com.example.natour.view.PannelloAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentVisualizzaStatisticheBinding.inflate(getLayoutInflater());
        controllerStatistiche = new ControllerStatistiche(this);
        controllerStatistiche.aggiornaStatisticheItinerario();
        binding.txtValItinerariTotale.setText("");

        /*final PeriodicWorkRequest periodicWorkRequest1 = new PeriodicWorkRequest.Builder(BackgroundStatistiche.class,1000, TimeUnit.MILLISECONDS)
                .setInitialDelay(1,TimeUnit.MILLISECONDS)
                .build();

        WorkManager workManager =  WorkManager.getInstance(requireContext());
        workManager.enqueue(periodicWorkRequest1);
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest1.getId())
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>()
                {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo)
                    {
                        if (workInfo != null) {
                            Log.d("periodicWorkRequest", "Status changed to : " + workInfo.getState());

                        }
                    }
                });*/


        return binding.getRoot();
    }

    private void aggiornaUI()
    {

    }

    public void updateItinerari(String num)
    {
        binding.txtValItinerariTotale.setText(num);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        controllerStatistiche.stopPolling();
    }

    public void updateRecensioni(String num)
    {
        binding.txtValRecensioniTotale.setText(num);
    }

    public void updateSegnalazioni(String num)
    {
        binding.txtValSegnalazioniTotale2.setText(num);
    }

    public void updateImmagini(String num)
    {
        binding.txtValFotoTotale.setText(num);
    }
}
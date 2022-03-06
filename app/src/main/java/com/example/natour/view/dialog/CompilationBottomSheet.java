package com.example.natour.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Compilation;
import com.example.natour.model.Segnalazione;
import com.example.natour.view.adapter.InsertCompilationAdapter;
import com.example.natour.view.adapter.SegnalazioniAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.LinkedList;
import java.util.List;

public class CompilationBottomSheet extends BottomSheetDialogFragment
{
    CallbackCompilation listener;
    ControllerVisualizzaItinerario controller;
    List<Compilation> compilationList;
    InsertCompilationAdapter compilationAdapter;

    public CompilationBottomSheet(List<Compilation> compilationList, ControllerVisualizzaItinerario controller){
        this.controller = controller;
        this.compilationList = compilationList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.compilation_bottom_sheet, container);
        v.findViewById(R.id.btn_addCompilation).setOnClickListener(view -> {
            new CreaCompilationDialog((titolo, descrizione) ->
            {
                //todo: metti compilation nel database
                Log.i("CompilationBottomSheet", "compilation da mettere nel database");
                Compilation newCompilation = new Compilation();
                newCompilation.setNome(titolo);
                newCompilation.setDescrizione(descrizione);
                compilationList.add(newCompilation);
                compilationAdapter.notifyItemInserted(compilationList.indexOf(newCompilation));
            }).show(getParentFragmentManager(),null);
        });
        compilationAdapter = new InsertCompilationAdapter(compilationList, getParentFragmentManager(), controller, requireContext());
        RecyclerView recyclerView = v.findViewById(R.id.rec_view_inserisci_compilation);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(compilationAdapter);
        return v;
    }

    public interface CallbackCompilation{
        void callbackCompilation(String segnalazione, String titolo);
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (CallbackCompilation) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    " must implement CallbackSegnalazione");
        }
    }
}

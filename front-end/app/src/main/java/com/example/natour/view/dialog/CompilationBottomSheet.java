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
import com.example.natour.model.dao.CompilationDAO;
import com.example.natour.view.adapter.InsertCompilationAdapter;
import com.example.natour.view.adapter.SegnalazioniAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class CompilationBottomSheet extends BottomSheetDialogFragment
{
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
            dismiss();
            new CreaCompilationDialog((titolo, descrizione) ->
            {
                if(!titolo.isEmpty()){
                    Compilation newCompilation = new Compilation();
                    newCompilation.setNome(titolo);
                    newCompilation.setDescrizione(descrizione);
                    CompilationDAO compilationDAO = new CompilationDAO();
                    compilationDAO.insertCompilation(titolo,descrizione, controller.getToken(), controller.getContext());
                    compilationList.clear();
                    compilationDAO.getCompilation(controller.getContext(), controller.getToken()).subscribe(
                            compilations ->{
                                for(int i = 0; i < compilations.length(); i++){
                                    JSONObject jsonObject = compilations.getJSONObject(i);
                                    Compilation compilation = new Compilation();
                                    compilation.setIdCompilation(Integer.parseInt(jsonObject.getString("id_compilation")));
                                    compilation.setNome(jsonObject.getString("nome"));
                                    compilation.setDescrizione(jsonObject.getString("descrizione"));
                                    compilation.setIdUtente(jsonObject.getString("id_utente"));
                                    compilationList.add(compilation);
                                }
                                compilationAdapter.notifyItemRangeChanged(0, compilationList.size()-1);
                            },
                            error->{}
                    );
                }
                else
                    new ErrorDialog("Inserisci il titolo della Compilation").show(getParentFragmentManager(),null);
            }).show(getParentFragmentManager(),null);
        });
        compilationAdapter = new InsertCompilationAdapter(compilationList, getParentFragmentManager(), controller, requireContext());
        RecyclerView recyclerView = v.findViewById(R.id.rec_view_inserisci_compilation);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(compilationAdapter);
        return v;
    }


}

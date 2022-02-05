package com.example.natour.view.Tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.natour.R;


public class CercaFragment extends Fragment {

    public CercaFragment()
    {
        // Required empty public constructor
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
}
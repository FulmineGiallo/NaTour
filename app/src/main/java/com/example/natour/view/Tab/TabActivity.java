package com.example.natour.view.Tab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.view.LoginActivity.SingletonNullException;
import com.example.natour.view.LoginActivity.UtenteSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TabActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Utente utente;
    private String TAG = "tab activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        try
        {
            model.setUtente(UtenteSingleton.getInstance().getUtente());
        }
        catch (SingletonNullException e)
        {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_tab);


        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomePageFragment()).commit();

        try
        {
            Log.i(TAG, UtenteSingleton.getInstance().getUtente().toString());
        } catch (SingletonNullException e)
        {
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment fragment = null;

            switch (item.getItemId())
            {
                case R.id.home:
                    fragment = new HomePageFragment();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.cerca:
                    fragment = new CercaFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


            return true;
        }
    };
}
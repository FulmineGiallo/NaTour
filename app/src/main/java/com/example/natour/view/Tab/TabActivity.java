package com.example.natour.view.Tab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.view.MessaggioActivity.MessaggioFragment;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        model.setUtente((Utente) getIntent().getSerializableExtra("utente"));
        //TODO: eliminare la classe singleton e ogni suo utilizzo
        setContentView(R.layout.activity_tab);


        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomePageFragment()).commit();

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
                case R.id.messaggio:
                    fragment = new MessaggioFragment();


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


            return true;
        }
    };
}
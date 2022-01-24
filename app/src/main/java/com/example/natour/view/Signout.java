package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;

public class Signout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        Amplify.Auth.signOut(
                () ->
                {
                    Log.i("AuthQuickstart", "Signed out successfully");
                    /*contexController.startActivity(intentLogin);*/

                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }


}
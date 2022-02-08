package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.R;
import com.example.natour.controller.ControllerProfile;
import com.example.natour.view.LoginActivity.Login;

public class Signout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        RxAmplify.Auth.signOut()
                .subscribe(
                        () ->
                        {
                            Log.i("AuthQuickstart", "Signed out successfully");
                            startActivity(new Intent(this, Login.class));
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        if (intent == null) {
            intent = new Intent();
        }
        super.startActivityForResult(intent, requestCode);
    }
}
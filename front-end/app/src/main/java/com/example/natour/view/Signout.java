package com.example.natour.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.rx.RxAmplify;
import com.example.natour.R;

public class Signout extends AppCompatActivity
{

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        RxAmplify.Auth.signOut()
                .subscribe(
                        () ->
                        {
                            Log.i("AuthQuickstart", "Signed out successfully");

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
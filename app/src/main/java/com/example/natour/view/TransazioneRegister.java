package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.natour.R;

public class TransazioneRegister extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transazione_register);
        ImageView logo = findViewById(R.id.img_logoReg);
        ImageView ok = findViewById(R.id.img_regconfermata);
        ImageView gufo = findViewById(R.id.img_gufo);
        TextView conferma = findViewById(R.id.txt_reg);
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.REVERSE);
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f, Animation.RELATIVE_TO_PARENT, ok.getX(), Animation.RELATIVE_TO_PARENT,
                ok.getY(), Animation.RELATIVE_TO_PARENT, ok.getY());
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);
        logo.startAnimation(anim);
        ok.startAnimation(animationSet);

        gufo.startAnimation(anim);
        conferma.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(TransazioneRegister.this,Login.class));
            }
        },3000);
    }
}
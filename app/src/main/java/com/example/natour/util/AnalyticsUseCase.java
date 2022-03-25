package com.example.natour.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUseCase
{
    public static void event(String name, String contentType, String nameEvent, Context context)
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
    }
}

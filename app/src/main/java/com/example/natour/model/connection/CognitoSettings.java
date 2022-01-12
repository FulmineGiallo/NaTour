package com.example.natour.model.connection;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings
{

    private String userPoolId = "eu-west-1_qydDAtdPy";
    private String clientId = "78jp80lubkon4tfh31m5t85b81";
    private String clientSecret = "g7qf4mr0h4j55f7lpbd91naeg60drsl5aump64gg9rt6cuc1k82";
    private Regions cognitoRegion = Regions.EU_WEST_1;
    private Context context;

    public CognitoSettings(Context context)
    {
        this.context = context;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public CognitoUserPool getUserPool()
    {
        return new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
    }

}

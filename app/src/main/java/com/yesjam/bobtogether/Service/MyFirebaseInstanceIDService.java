package com.yesjam.bobtogether.Service;


import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Etc;
import com.yesjam.bobtogether.Preferences.FCMRTokenPreferences;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "bobtogetherInID";
    private FCMRTokenPreferences fcmrTokenPreferences;

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        fcmrTokenPreferences = new FCMRTokenPreferences(MyFirebaseInstanceIDService.this);
        fcmrTokenPreferences.setFCMToken(refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }
}

package com.pwszit.singiel;
import android.app.IntentService;
import android.content.Intent;
//import android.support.v4.content.LocalBroadcastManager;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
//import com.google.firebase.iid.InstanceIdResult;

//import net.simplifiedcoding.simplifiedcodingchat.R;
//import net.simplifiedcoding.simplifiedcodingchat.helper.AppController;
//import net.simplifiedcoding.simplifiedcodingchat.helper.Constants;
//import net.simplifiedcoding.simplifiedcodingchat.helper.URLs;

import java.util.HashMap;
import java.util.Map;



public class GCMRegistrationIntentService extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String REGISTRATION_TOKEN_SENT = "RegistrationTokenSent";

    public GCMRegistrationIntentService() {
        super("");
    }
    private SharedPreferences userPref;


    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            //InstanceIdResult instanceID = InstanceIdResult.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);

            sendRegistrationTokenToServer(token);
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final int id = AppController.getInstance().getUserId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.STORAGE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Intent registrationComplete = new Intent(REGISTRATION_TOKEN_SENT);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){

            //dodanie tokena do naglowka


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

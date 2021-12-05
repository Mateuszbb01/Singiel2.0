package com.pwszit.singiel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences infoUser = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                boolean isLoggedIn = infoUser.getBoolean("isLoggedIn", false);

                if (isLoggedIn){
                    // jeśli usługa Google Play nie znajduje się w aplikacji urządzenia, nie będzie działać
                    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

                    if (ConnectionResult.SUCCESS != resultCode) {
                        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                            Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                            GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                        } else {
                            Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Intent itent = new Intent(MainActivity.this, GCMRegistrationIntentService.class);
                        startService(itent);
                    }
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                    //isFirstTime();

                }


                else {
                    isFirstTime();
                }
            }
        } ,1500);
    }

    private void isFirstTime() {

        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime",true);
        //default value true
        if (isFirstTime){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime",false);
            editor.apply();

            //Onboard activity
            startActivity(new Intent(MainActivity.this,OnBoardActivity.class));
            finish();
        }
        else{
            //start Auth Activity
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();
        }
    }
}
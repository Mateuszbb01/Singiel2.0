package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class PreferencesActivity3 extends AppCompatActivity {
    private TextView textView, textView2;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences3);
        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = preferences.getString("NAME", "");
        String date = preferences.getString("BIRTHDATE", "");
        textView.setText(name);
        textView2.setText(date);
    }
}
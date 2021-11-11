package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity4 extends AppCompatActivity {

    EditText txtCity;
    Button save;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences4);
        save  =  findViewById(R.id.btnConfirmCity);
        txtCity = findViewById(R.id.txtCity);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city = txtCity.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("CITY", city);
                editor.apply();

                Intent intent = new Intent(PreferencesActivity4.this, PreferencesActivity5.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity5 extends AppCompatActivity {


    EditText txtHobbies;
    Button save;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences5);
        save  =  findViewById(R.id.btnConfirmInterested);
        txtHobbies = findViewById(R.id.txtHobbies);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hobbies = txtHobbies.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("HOBBIES", hobbies);
                editor.apply();

                Intent intent = new Intent(PreferencesActivity5.this, PreferencesActivity6.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PreferencesActivity3 extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button save;
    SharedPreferences sharedPreferences;
    String engGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences3);
        save  =  findViewById(R.id.btnConfirmGender);
        radioGroup = findViewById(R.id.radioGroup);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String gender = radioButton.getText().toString();

                if (gender.equals("KOBIETĄ")) {
                    engGender = "female";
                }
                else
                {
                    engGender = "male";
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("GENDER", engGender);
                editor.apply();

                Intent intent = new Intent(PreferencesActivity3.this, PreferencesActivity4.class);
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
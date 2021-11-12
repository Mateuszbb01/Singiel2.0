package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class PreferencesActivity4 extends AppCompatActivity {

    EditText txtCity;
    TextInputLayout txtLayoutCity;
    Button save;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences4);
        save  =  findViewById(R.id.btnConfirmCity);
        txtCity = findViewById(R.id.txtCity);
        txtLayoutCity = findViewById(R.id.txtLayoutCity);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        save.setOnClickListener(v -> {

            if (validateCity()) {
                save();
            }
        });


        txtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtCity.getText().toString().isEmpty()) {
                    txtLayoutCity.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private Boolean validateCity() {
        String val = txtCity.getText().toString();
        String noWhiteSpaces = "(?=\\s+$)";
        if (val.isEmpty()){
            txtLayoutCity.setError("Miasto musi zostać podane");
            return false;
        }
        else if(val.length()<=3) {
            txtLayoutCity.setError("Nazwa miasta jest za krótka");
            return false;
        }
        else{
            txtLayoutCity.setError(null);
            return true;
        }

    }
    private void save(){

        String city = txtCity.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CITY", city);
        editor.apply();

        Intent intent = new Intent(PreferencesActivity4.this, PreferencesActivity5.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
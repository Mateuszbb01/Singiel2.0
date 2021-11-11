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

public class PreferencesActivity5 extends AppCompatActivity {


    EditText txtHobbies;
    TextInputLayout txtLayoutHobbies;
    Button save;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences5);
        save  =  findViewById(R.id.btnConfirmInterested);
        txtHobbies = findViewById(R.id.txtHobbies);
        txtLayoutHobbies = findViewById(R.id.txtLayoutHobbies);

        save.setOnClickListener(v -> {

            if (validateHobbies()) {
                save();
            }
        });
        txtHobbies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtHobbies.getText().toString().isEmpty()) {
                    txtLayoutHobbies.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private Boolean validateHobbies() {
        String val = txtHobbies.getText().toString();
        String noWhiteSpaces = "(?=\\s+$)";
        if (val.isEmpty()){
            txtLayoutHobbies.setError("Musisz posiadać opis");
            return false;
        }
        else if(val.length()<=10){
            txtLayoutHobbies.setError("Opis wymaga przynajmniej 10 znaków");
            return false;
        }
        else{
            txtLayoutHobbies.setError(null);
            return true;
        }

    }
    private void save(){
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String hobbies = txtHobbies.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HOBBIES", hobbies);
        editor.apply();

        Intent intent = new Intent(PreferencesActivity5.this, PreferencesActivity6.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
package com.pwszit.singiel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PreferencesActivity extends AppCompatActivity {

    EditText txtName;
    TextInputLayout txtLayoutName;
    Button save;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        save = findViewById(R.id.btnConfirmName);
        txtName = findViewById(R.id.txtName);
        txtLayoutName = findViewById(R.id.txtLayoutName);

        save.setOnClickListener(v -> {

            if (validateName()) {
                save();
            }
        });


    }

    private Boolean validateName() {
        String val = txtName.getText().toString();
        if (val.isEmpty()){
            txtLayoutName.setError("Imię musi zostać podane!");
            return false;
        }
        else{
            txtName.setError(null);
            return true;
        }

    }

    private void save() {
            sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
            String name = txtName.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("NAME", name);
            editor.apply();

            Intent intent = new Intent(PreferencesActivity.this, PreferencesActivity2.class);
            startActivity(intent);
            finish();
        }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}




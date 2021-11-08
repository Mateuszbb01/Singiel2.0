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
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class PreferencesActivity2 extends AppCompatActivity {


    TextInputEditText txtBirthDate;
    Button save;
    //TextView txtDate;
    DatePicker datePicker;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences2);
        save = findViewById(R.id.btnConfirmBirthDate);
       // txtDate = findViewById(R.id.txtBirthDate);
        datePicker = findViewById(R.id.txtBirthDate);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day=datePicker.getDayOfMonth();
                int month=datePicker.getMonth();
                int year=datePicker.getYear();


                String birthDate = makeDateString(day, month, year);


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("BIRTHDATE", birthDate);
                editor.apply();

                Intent intent = new Intent(PreferencesActivity2.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private String makeDateString(int day, int month, int year) {
        return year + "-" + month + "-" + day;
    }
}
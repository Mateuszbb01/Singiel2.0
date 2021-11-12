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

import java.util.Calendar;
import java.util.GregorianCalendar;


public class PreferencesActivity2 extends AppCompatActivity {


    TextInputEditText txtBirthDate;
    Button save;
    TextView adult;
    DatePicker datePicker;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences2);
        save = findViewById(R.id.btnConfirmBirthDate);
        adult = findViewById(R.id.Adult);
        datePicker = findViewById(R.id.txtBirthDate);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);


        save.setOnClickListener(v -> {

            if (validateDate()) {
                save();
            }
        });



    }
    private Boolean validateDate(){
        int day=datePicker.getDayOfMonth();
        int month=datePicker.getMonth();
        int year=datePicker.getYear();
        Calendar userAge = new GregorianCalendar(year,month,day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            adult.setText("Musisz być pełnoletni, aby używać tej aplikacji");
            return false;

        }
        else{
            adult.setText("");
            return true;

        }
    }
    private void save(){
        int day=datePicker.getDayOfMonth();
        int month=datePicker.getMonth();
        int year=datePicker.getYear();


        String birthDate = makeDateString(day, month, year);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BIRTHDATE", birthDate);
        editor.apply();

        Intent intent = new Intent(PreferencesActivity2.this, PreferencesActivity3.class);
        startActivity(intent);
        finish();
    }
    private String makeDateString(int day, int month, int year) {
        return year + "-" + month + "-" + day;
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
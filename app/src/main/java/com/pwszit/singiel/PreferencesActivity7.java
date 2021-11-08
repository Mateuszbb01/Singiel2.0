package com.pwszit.singiel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PreferencesActivity7 extends AppCompatActivity {
    private Button btnConfirm;
    private Bitmap bitmap = null;
    private SharedPreferences preferences, userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        btnConfirm = findViewById(R.id.btnAccept);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);



        btnConfirm.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                         //walidacja
           // if (validate()){
              saveUserInfo();
           // }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode==RESULT_OK){

            preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
            String uriPhoto = preferences.getString("URI_PHOTO", "");
            Uri imgUri = Uri.parse(uriPhoto);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private void saveUserInfo() {
        dialog.setMessage("Zapisywanie");
        dialog.show();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = preferences.getString("NAME", "");
        String date = preferences.getString("BIRTHDATE", "");
        String gender = preferences.getString("GENDER", "");
        String city = preferences.getString("CITY", "");
        String hobbies = preferences.getString("HOBBIES", "");


        StringRequest request = new StringRequest(Request.Method.POST, Constant.SAVE_NAME, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){

                    startActivity(new Intent(PreferencesActivity7.this, HomeActivity.class));
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){
            //dodanie tokena do naglowka


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            //dodanie parametrow

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("bornDate",date);
                map.put("gender",gender);
                map.put("city",city);
                map.put("interests",hobbies);
                map.put("photo",bitmapToString(bitmap));
                return map;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(PreferencesActivity7.this);
        queue.add(request);
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }
}
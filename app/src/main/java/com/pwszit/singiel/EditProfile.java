package com.pwszit.singiel;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private TextView txtBirthDate;
    private TextView txtName;
    private TextView txtNewDescription;
    private TextView txtNewCity;
    SharedPreferences preferences, userPref, sharedPreferences;
    private Bitmap bitmap = null;
    private Button btnSaveProfile;
    private ProgressDialog dialog;
    private ImageView imageView;
    private static final int GALLERY_ADD_PROFILE = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        txtName = findViewById(R.id.txtName);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtNewDescription = findViewById(R.id.txtNewDescription);
        txtNewCity = findViewById(R.id.txtNewCity);



        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = preferences.getString("NAME", "");
        String date = preferences.getString("BIRTHDATE", "");
        String hobbies = preferences.getString("HOBBIES", "");
        String city = preferences.getString("CITY", "");

//        String uriPhoto = preferences.getString("URI_PHOTO", "");
//        Uri imageUri = Uri.parse(uriPhoto);
//        imageView.setImageURI(imageUri);

        txtName.setText(name);
        txtBirthDate.setText(date);
        txtNewDescription.setText(hobbies);
        txtNewCity.setText(city);



        //sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        init();
    }



    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        imageView = findViewById(R.id.addPhoto);
        TextView btnEditPhoto = findViewById(R.id.btnEditPhoto);


        btnEditPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });

        btnSaveProfile.setOnClickListener(v -> {
//            Intent i = new Intent(Intent.ACTION_PICK);
//            i.setType("image/*");

            updateProfile();
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imageView.setImageURI(imgUri);

            try {
                if(Build.VERSION.SDK_INT < 29) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imgUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile() {
        dialog.setMessage("Aktualizacja profilu");
        dialog.show();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

//wyciąganie wartosci z shared do zapisania
        String name = txtName.getText().toString();
        String date = txtBirthDate.getText().toString();
        String city = txtNewCity.getText().toString();
        String gender = preferences.getString("GENDER", "");
        String hobbies = txtNewDescription.getText().toString();



//Zapisywanie w shared NOWYCH wartosci
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", name);
        editor.putString("BIRTHDATE", date);
        editor.putString("CITY", city);
        editor.putString("HOBBIES", hobbies);

        // editor.putString("URI_PHOTO", imgUri.toString());

        editor.apply();



        StringRequest request = new StringRequest(Request.Method.POST, Constant.PROFILE_UPDATE, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){

                    startActivity(new Intent(EditProfile.this, EditProfile.class));
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
                map.put("city",city);
                map.put("gender",gender);
                map.put("interests",hobbies);
                map.put("photo",bitmapToString(bitmap));
                return map;
            }
        };

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
        queue.add(request);
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }



}



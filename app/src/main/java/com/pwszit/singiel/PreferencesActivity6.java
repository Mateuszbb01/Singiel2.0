package com.pwszit.singiel;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PreferencesActivity6 extends AppCompatActivity {


    Button save;
    SharedPreferences sharedPreferences;
    private ImageView imageView;
    private TextView btnSelectPhoto, photoWarning;
    private SharedPreferences preferences, userPref;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences6);
        init();

    }
    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        save = findViewById(R.id.btnConfirmPhoto);
        imageView = findViewById(R.id.addPhoto);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        photoWarning = findViewById(R.id.photoWarning);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);


        btnSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });

        save.setOnClickListener(v->{
            if(photoValidate()){
                savePreferences();
            }
        });


    }
    private Boolean photoValidate() {
        String photoString = bitmapToString(bitmap);
        String noWhiteSpaces = "(?=\\s+$)";
        if (photoString.isEmpty()){
            photoWarning.setText("Zdjęcie jest wymagane");
            return false;
        }
        else{
            photoWarning.setError(null);
            return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imageView.setImageURI(imgUri);

            try {
                if(Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imgUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void savePreferences(){

        dialog.setMessage("Zapisywanie");
        dialog.show();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = preferences.getString("NAME", "");
        String date = preferences.getString("BIRTHDATE", "");
        String gender = preferences.getString("GENDER", "");
        String city = preferences.getString("CITY", "");
        String hobbies = preferences.getString("HOBBIES", "");

        StringRequest request = new StringRequest(Request.Method.POST,Constant.REGISTER_PREFERENCES, response->{

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    // SharedPreferences.Editor editor = userPref.edit();
                    //editor.putString("photo",object.getString("photo"));
                    // editor.apply();
                    startActivity(new Intent(PreferencesActivity6.this,HomeActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        },error ->{
            error.printStackTrace();
            dialog.dismiss();
        } ){

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

        RequestQueue queue = Volley.newRequestQueue(PreferencesActivity6.this);
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
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
package com.pwszit.singiel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class PreferencesActivity6 extends AppCompatActivity {


    Button save;
    SharedPreferences sharedPreferences;
    private ImageView imageView;
    private TextView btnSelectPhoto;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences6);
        init();

    }
    private void init() {
        save = findViewById(R.id.btnConfirmPhoto);
        imageView = findViewById(R.id.addPhoto);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);


        btnSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imageView.setImageURI(imgUri);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("URI_PHOTO", imgUri.toString());
            editor.apply();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(PreferencesActivity6.this, PreferencesActivity7.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
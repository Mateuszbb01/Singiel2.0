package com.pwszit.singiel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class specificchat extends AppCompatActivity {


    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);


        mgetmessage = findViewById(R.id.getmessage);
        msendmessagecardview = findViewById(R.id.carviewofsendmessage);
        msendmessagebutton = findViewById(R.id.imageviewsendmessage);
        mtoolbarofspecificchat = findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser = findViewById(R.id.Nameofspecificuser);


    }
}
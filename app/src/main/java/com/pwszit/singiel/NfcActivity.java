package com.pwszit.singiel;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.auron.library.vcard.VCard;
import it.auron.library.vcard.VCardParser;

public class NfcActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;


    private EditText et_phone;
    private EditText et_email;
    private EditText et_insta;
    private EditText et_facebook;
    private EditText et_tiktok;

    private String name;
    private String company;
    private String address;
    private String phone;
    private String email;
    private String insta;
    private String facebook;
    private String tiktok;
    private String idUser;
    private ImageView imageView;
    SharedPreferences  userPref, userIdPref;
    private ProgressDialog dialog;


    SharedPreferences preferences2,sharedPreferences;

    private StringBuilder stringBuilder = new StringBuilder();
    private AlertDialog writeDataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_insta = findViewById(R.id.et_insta);
        et_facebook = findViewById(R.id.et_facebook);
        et_tiktok = findViewById(R.id.et_tiktok);
        //Button btn = findViewById(R.id.btn);
        Button btnSave = findViewById(R.id.btnSaveIt);
        imageView = findViewById(R.id.qrcod3e);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dane kontaktowe");

        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("vCard", MODE_PRIVATE);
        String phoneV = sharedPreferences.getString("phoneV", "");
        String mailV = sharedPreferences.getString("mailV", "");
        String instaV = sharedPreferences.getString("instaV", "");
        String facebookV = sharedPreferences.getString("facebookV", "");
        String tiktokV = sharedPreferences.getString("tiktokV", "");
        userIdPref = getSharedPreferences("user", MODE_PRIVATE);
        int userId = userIdPref.getInt("id", -1);
        String user_id_string = String.valueOf(userId);


        et_phone.setText(phoneV);
        et_email.setText(mailV);
        et_insta.setText(instaV);
        et_facebook.setText(facebookV);
        et_tiktok.setText(tiktokV);



        stringBuilder.setLength(0);
        stringBuilder.append("BEGIN:VCARD\n")
                .append("VERSION:3.0\n")
               // .append("FN:").append(nameV).append("\n")
                .append("TEL;TYPE=CELL:").append(phoneV).append("\n")
                .append("EMAIL;TYPE=HOME,INTERNET:").append(mailV).append("\n")
               // .append("ADR;TYPE=HOME:;;").append(addressV).append(";;;;\n")
                //.append("ORG:").append(companyV).append("\n")
                .append("URL:https://www.instagram.com/").append(instaV).append("\n")
//                .append("URL:").append(facebookV).append("\n")
//                .append("URL:").append(tiktokV).append("\n")
                .append("END:VCARD");
        VCard vCard = VCardParser.parse(stringBuilder.toString());
        //vCard.setAddress(addressV);
        vCard.addTelephone(phoneV);
        vCard.addEmail(mailV);
        String vCardcontent = vCard.buildString();

        imageView.setImageBitmap(QRCode.from(vCardcontent).withSize(250, 250).bitmap());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.share);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //name = et_name.getText().toString().trim();
                //company = et_company.getText().toString().trim();
                //address = et_address.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                email = et_email.getText().toString().trim();
                insta = et_insta.getText().toString().trim();
                facebook = et_facebook.getText().toString().trim();
                tiktok = et_tiktok.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email = "brak";
                }
                if (TextUtils.isEmpty(insta)) {
                    insta = "brak";
                }
                if (TextUtils.isEmpty(facebook)) {
                    facebook = "brak";
                }
                if (TextUtils.isEmpty(tiktok)) {
                    tiktok = "brak";
                }


                if (TextUtils.isEmpty(phone)) {
                    dataLack();
                    return;
                }else{
                    dialog.setMessage("Aktualizacja danych kontaktowych");
                    dialog.show();

                    stringBuilder.append("BEGIN:VCARD\n")
                            .append("VERSION:3.0\n")
                            // .append("FN:").append(nameV).append("\n")
                            .append("TEL;TYPE=CELL:").append(phone).append("\n")
                            .append("EMAIL;TYPE=HOME,INTERNET:").append(email).append("\n")
                            // .append("ADR;TYPE=HOME:;;").append(addressV).append(";;;;\n")
                            //.append("ORG:").append(companyV).append("\n")
                            .append("URL:https://www.instagram.com/").append(insta).append("\n")
//                            .append("item1.URL:").append(insta).append("\n")
//                            .append("item1.X-ABLabel:Instagram").append("\n")
//                            .append("URL:").append(facebook).append("\n")
//                            .append("URL:").append(tiktok).append("\n")
                            .append("END:VCARD");


                    VCard vCard = VCardParser.parse(stringBuilder.toString());
                    //vCard.setAddress(address);
                    vCard.addTelephone(phone);
                    vCard.addEmail(email);
                    String vCardcontent = vCard.buildString();

                    imageView.setImageBitmap(QRCode.from(vCardcontent).withSize(250, 250).bitmap());

                    sharedPreferences = getSharedPreferences("vCard", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();



                    editor.putString("useridV", user_id_string);
                    editor.putString("phoneV", phone);
                    editor.putString("mailV", email);
                    editor.putString("instaV", insta);
                    editor.putString("facebookV", facebook);
                    editor.putString("tiktokV", tiktok);

                    editor.apply();

                    StringRequest request = new StringRequest(Request.Method.POST, Constant.VCARD, response -> {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                dialog.dismiss();
                                Toast.makeText(NfcActivity.this, "Zauktalizowano dane kontaktowe", Toast.LENGTH_SHORT).show();
                                return;


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
                            map.put("phone_no",phone);
                            map.put("mail",email);
                            map.put("address",insta);
                            map.put("company",facebook);
                            map.put("website",tiktok);

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


                    RequestQueue queue = Volley.newRequestQueue(NfcActivity.this);
                    queue.add(request);

                }


            }
        });


        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


    }
    public void send_via_nfc(){
        if (mNfcAdapter == null) {
           nonSupport();
        }else {

            description();


        }
    }
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            if (TextUtils.isEmpty(stringBuilder.toString())) {
                Toast.makeText(this, "Wpisz swoje dane osobowe i kliknij Dalej zanim przyłożysz tag NFC", Toast.LENGTH_SHORT).show();
                return;
            }
            NdefMessage ndefMessage = new NdefMessage(NdefRecord.createMime("text/vcard", stringBuilder.toString().getBytes()));
            writeTag(ndefMessage, tag);
            if (writeDataDialog != null) {
                writeDataDialog.dismiss();
            }
        }
    }



    private void nonSupport() {
        AlertDialog nonSupportDialog = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Niestety twoje urządzenie nie wspiera NFC")
                .setCancelable(false)
                .setPositiveButton("zamknij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();

        nonSupportDialog.show();
    }


    private void description() {
        AlertDialog descriptionDialog = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage(R.string.description)
                .setCancelable(false)
                .setPositiveButton("Akceptuję", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
                            hintOpenNfc();
                        }
                        else{

                            phone = et_phone.getText().toString().trim();
                            email = et_email.getText().toString().trim();
                            insta = et_insta.getText().toString().trim();
                            facebook = et_facebook.getText().toString().trim();
                            tiktok = et_tiktok.getText().toString().trim();

                            if (TextUtils.isEmpty(phone)) {
                                dataLack();
                                return;
                            }

                            if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
                                hintOpenNfc();
                                return;
                            }

                            stringBuilder.setLength(0);
                            stringBuilder.append("BEGIN:VCARD\n")
                                    .append("VERSION:3.0\n")
                                    // .append("FN:").append(nameV).append("\n")
                                    .append("TEL;TYPE=CELL:").append(phone).append("\n")
                                    //.append("EMAIL;TYPE=HOME,INTERNET:").append(email).append("\n")
                                    // .append("ADR;TYPE=HOME:;;").append(addressV).append(";;;;\n")
                                    //.append("ORG:").append(companyV).append("\n")
                                    .append("URL:IG|").append(insta).append("\n")
                                    .append("URL:FB|").append(facebook).append("\n")
                                    .append("URL:TikTok|").append(tiktok).append("\n")
                                    .append("END:VCARD");


                            VCard vCard = VCardParser.parse(stringBuilder.toString());
                            //vCard.setAddress(address);
                            vCard.addTelephone(phone);
                            vCard.addEmail(email);
                            String vCardcontent = vCard.buildString();

                            writeData();


                            imageView.setImageBitmap(QRCode.from(vCardcontent).withSize(250, 250).bitmap());

                            sharedPreferences = getSharedPreferences("vCard", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("phoneV", phone);
                            editor.putString("mailV", email);
                            editor.putString("instaV", insta);
                            editor.putString("facebookV", facebook);
                            editor.putString("tiktokV", tiktok);
                            preferences2 = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

                            editor.apply();
                        }
                    }
                }).create();

        descriptionDialog.show();
    }


    private void hintOpenNfc() {
        AlertDialog hintOpenNfcDialog = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Aby używać tej aplikacji musisz włączyć NFC, czy chcesz przejść do ustawień?")
                .setCancelable(false)
                .setPositiveButton("Ustawienia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                        startActivity(setNfc);
                    }
                }).setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();

        hintOpenNfcDialog.show();
    }


    private void writeData() {
        AlertDialog.Builder writeDataDialog = new AlertDialog.Builder(NfcActivity.this);
        LayoutInflater factory = LayoutInflater.from(NfcActivity.this);
        final View view = factory.inflate(R.layout.sample, null);
        writeDataDialog.setTitle("Info");
        writeDataDialog.setMessage("Przyłóż tag NFC do tyłu urządzenia");
        writeDataDialog.setCancelable(false);
        writeDataDialog.setPositiveButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
                .create();

        writeDataDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Restore the default state
                if (mNfcAdapter != null)
                    mNfcAdapter.disableForegroundDispatch(NfcActivity.this);
            }
        });

        //Setting processing is better than all other NFC processing
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(NfcActivity.this, mPendingIntent, null, null);

        writeDataDialog.show();
    }


    public void writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Błąd: Dane NFC nie mogą zostać usunięte z tagu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "Błąd: niewystarczająca pojemnosc tagu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ndef.writeNdefMessage(message);
                Toast.makeText(NfcActivity.this, "Dane zapisane poprawnie.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dataLack() {
        AlertDialog dataLackDialog = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("\n" +
                        "Musisz podać numer telefonu")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        dataLackDialog.show();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Intent intent;
                    switch (menuItem.getItemId()){
                        case R.id.share:
                            startActivity(new Intent(getApplicationContext()
                                    ,NfcActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.czat:
                            startActivity(new Intent(getApplicationContext()
                                    ,ChatActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.home:
                            startActivity(new Intent(getApplicationContext()
                                    ,HomeActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.profile:
                            startActivity(new Intent(getApplicationContext()
                                    ,EditProfile.class));
                            overridePendingTransition(0, 0);


                    }

                    return false;
                }
            };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nfc, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_send_nfc:
                send_via_nfc();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.pwszit.singiel;

import android.app.PendingIntent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.glxn.qrgen.android.QRCode;

import it.auron.library.vcard.VCard;
import it.auron.library.vcard.VCardParser;

public class NfcActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    private EditText et_name;
    private EditText et_company;
    private EditText et_address;
    private EditText et_phone;
    private EditText et_email;
    private EditText et_website;

    private String name;
    private String company;
    private String address;
    private String phone;
    private String email;
    private String website;
    private ImageView imageView;

    SharedPreferences sharedPreferences;

    private StringBuilder stringBuilder = new StringBuilder();
    private AlertDialog writeDataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        et_name = findViewById(R.id.et_name);
        et_company = findViewById(R.id.et_company);
        et_address = findViewById(R.id.et_address);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_website = findViewById(R.id.et_website);
        Button btn = findViewById(R.id.btn);
        imageView = findViewById(R.id.qrcod3e);


        sharedPreferences = getSharedPreferences("vCard", MODE_PRIVATE);
        String nameV = sharedPreferences.getString("nameV", "");
        String phoneV = sharedPreferences.getString("phoneV", "");
        String emailV = sharedPreferences.getString("emailV", "");
        String addressV = sharedPreferences.getString("addressV", "");
        String companyV = sharedPreferences.getString("companyV", "");
        String websiteV = sharedPreferences.getString("websiteV", "");

        et_name.setText(nameV);
        et_company.setText(companyV);
        et_address.setText(addressV);
        et_phone.setText(phoneV);
        et_email.setText(emailV);
        et_website.setText(websiteV);



        stringBuilder.setLength(0);
        stringBuilder.append("BEGIN:VCARD\n")
                .append("VERSION:3.0\n")
                .append("FN:").append(nameV).append("\n")
                .append("TEL;TYPE=CELL:").append(phoneV).append("\n")
                .append("EMAIL;TYPE=HOME,INTERNET:").append(emailV).append("\n")
                .append("ADR;TYPE=HOME:;;").append(addressV).append(";;;;\n")
                .append("ORG:").append(companyV).append("\n")
                .append("URL:").append(websiteV).append("\n")
                .append("END:VCARD");

        VCard vCard = VCardParser.parse(stringBuilder.toString());
        vCard.setAddress(addressV);
        vCard.addTelephone(phoneV);
        vCard.addEmail(emailV);
        String vCardcontent = vCard.buildString();

        imageView.setImageBitmap(QRCode.from(vCardcontent).withSize(250, 250).bitmap());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.share);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                name = et_name.getText().toString().trim();
                company = et_company.getText().toString().trim();
                address = et_address.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                email = et_email.getText().toString().trim();
                website = et_website.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
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
                        .append("FN:").append(name).append("\n")
                        .append("TEL;TYPE=CELL:").append(phone).append("\n")
                        .append("EMAIL;TYPE=HOME,INTERNET:").append(email).append("\n")
                        .append("ADR;TYPE=HOME:;;").append(address).append(";;;;\n")
                        .append("ORG:").append(company).append("\n")
                        .append("URL:").append(website).append("\n")
                        .append("END:VCARD");

                writeData();

                VCard vCard = VCardParser.parse(stringBuilder.toString());
                vCard.setAddress(address);
                vCard.addTelephone(phone);
                vCard.addEmail(email);
                String vCardcontent = vCard.buildString();

                imageView.setImageBitmap(QRCode.from(vCardcontent).withSize(250, 250).bitmap());

                sharedPreferences = getSharedPreferences("vCard", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nameV", name);
                editor.putString("phoneV", phone);
                editor.putString("emailV", email);
                editor.putString("addressV", address);
                editor.putString("companyV", company);
                editor.putString("websiteV", website);
                editor.apply();
            }
        });

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        if (mNfcAdapter == null) {
//            nonSupport();
//        }

        description();
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
                        "Musisz podać przynajmniej imię i numer telefonu")
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

}
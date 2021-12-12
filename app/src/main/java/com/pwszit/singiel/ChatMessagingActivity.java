package com.pwszit.singiel;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import static com.pwszit.singiel.AppController.TAG;

public class ChatMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //Recyclerview objects
    Cipher ecipher;
    Cipher dcipher;
    // 8-byte Salt
    byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    //Progress dialog
    private ProgressDialog dialog;


    //Recyclerview objects
    private RecyclerView recyclerView;
    private LinearLayoutManager  layoutManager;
    private RecyclerView.Adapter adapter;
    private SharedPreferences preferences,sharedPreferences, userPref2, usertoid, userpref, userpref2;

    //ArrayList messages to store messages
    private ArrayList<Message> messages;

    //Button to send new message
    private Button buttonSend, sendVCard;

    //EditText to send new message
    private EditText editTextMessage;
    private TextView username;
    String name, id, namewritingto;
    String FETCH_MESSAGES;
    // Iteration count
    int iterationCount = 19;
    private int page = 1;
    private int notificationordsend = 0;

    private int totalItemCount;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int previousTotal;
    private boolean load = true;
    private boolean added = false;
    public String encrypt(String secretKey, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {
        //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        //Enc process
        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        String charSet="UTF-8";
        byte[] in = plainText.getBytes(charSet);
        byte[] out = ecipher.doFinal(in);
        String encStr=new BASE64Encoder().encode(out);
        return encStr;
    }
    /**
     * @param secretKey Key used to decrypt data
     * @param encryptedText encrypted text input to decrypt
     * @return Returns plain text after decryption
     */
    public String decrypt (String secretKey, String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException

    {
        //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        //Decryption process; same key will be used for decr
        dcipher=Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
        byte[] enc = new BASE64Decoder().decodeBuffer(encryptedText);
        byte[] utf8 =dcipher.doFinal(enc);
        String charSet="UTF-8";
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userpref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userpref2 = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        getAndSetIntentData();

        //Wyświetlanie okna dialogowego, gdy czat jest gotowy
        dialog = new ProgressDialog(this);
        dialog.setMessage("Otwarcie czatu");
        dialog.show();

        //Initializing recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing message arraylist
        messages = new ArrayList<>();

        //Wywołanie funkcji, aby pobrać istniejące wiadomości w wątku
        fetchMessages();

        //initializing button and edittext
        buttonSend = (Button) findViewById(R.id.buttonSend);
        sendVCard = (Button) findViewById(R.id.sendVCard);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        username = (TextView) findViewById(R.id.username);

        //Adding listener to button
        buttonSend.setOnClickListener(this);

        //Creating broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {

                    //Jeśli rejestracja w gcm zakończy się sukcesem

                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_TOKEN_SENT)) {

                    //Gdy token rejestracji zostanie wysłany do serwera wyświetlającego toast
                    Toast.makeText(getApplicationContext(), "Czat gotowy..", Toast.LENGTH_SHORT).show();

                    //Kiedy otrzymaliśmy powiadomienie, gdy aplikacja jest na pierwszym planie
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    //Getting message data
                    String title = intent.getStringExtra("title");
                    String body = intent.getStringExtra("body");
                    String user_from_id_notification = intent.getStringExtra("id");
                    //String message = "elloooososoos";

                    if(user_from_id_notification.equals(id)){
                        processMessage(title, body, user_from_id_notification);

                    }
//                    String id = intent.getStringExtra("id");

                    //processing the message to add it in current thread
                }
            }
        };

        // jeśli usługa Google Play nie znajduje się w aplikacji urządzenia, nie będzie działać
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        if (ConnectionResult.SUCCESS != resultCode) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
//                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
//
//            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
//            startService(itent);
//        }

        sendVCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("vCard", MODE_PRIVATE);
                String user_id_vcard = preferences.getString("useridV", "");

                //String user_id_string = String.valueOf(user_id_vcard);

                preferences = getSharedPreferences("user", MODE_PRIVATE);
                int user_logged_id = preferences.getInt("id", -1);
                String user_logged_string = String.valueOf(user_logged_id);
                if (TextUtils.isEmpty(user_id_vcard)) {
                    user_id_vcard = "brak";
                }
                if (user_id_vcard.equals(user_logged_string)) {


                    dialog.setMessage("Wysyłanie kontaktu");
                    dialog.show();


                    StringRequest request = new StringRequest(Request.Method.POST, Constant.SEND_VCARD, response -> {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }, error -> {
                        error.printStackTrace();
                        dialog.dismiss();

                    }) {
                        //dodanie tokena do naglowka

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String token = userpref2.getString("token", "");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("Authorization", "Bearer " + token);
                            return map;
                        }

                        //dodanie parametrow

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_to_id", id);

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


                    RequestQueue queue = Volley.newRequestQueue(ChatMessagingActivity.this);
                    queue.add(request);

                } else
                {
                    AlertDialog descriptionDialog = new AlertDialog.Builder(ChatMessagingActivity.this)
                            .setTitle("Info")
                            .setMessage("Musisz podać swoje dane kontaktowe")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getApplicationContext()
                                            , NfcActivity.class));
                                }
                            }).create();

                    descriptionDialog.show();
                }



            }
        });

    }
    void getAndSetIntentData() {
        if (getIntent().hasExtra("name")
             && getIntent().hasExtra("id"))
            //                && getIntent().hasExtra("cena_l") &&
//                getIntent().hasExtra("koszt") && getIntent().hasExtra("litry"))
        {
            //Zbieranie danych z Intent
            namewritingto = getIntent().getStringExtra("name");

            id = getIntent().getStringExtra("id");
            sharedPreferences = getSharedPreferences("store_pared", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("id", id);
            editor.apply();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }
    //Metoda pobierania wszystkich wiadomości z wątku
    private void fetchMessages() {
        String id2 = getIntent().getStringExtra("id");
        userPref2 = getSharedPreferences("user", MODE_PRIVATE);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = preferences.getInt("id", -1);

              //   ?page="+page
        StringRequest request = new StringRequest(Request.Method.POST, Constant.FETCH_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObj = object.optJSONObject("Message");
                            JSONArray array = jsonObj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject messageObject = array.getJSONObject(i);
                                JSONObject preferObject = messageObject.getJSONObject("preferences");
                                int userId = messageObject.getInt("user_from_id");
                                String message = null;
                                try {
                                    message = decrypt("ciezkiemaslo", messageObject.getString("message"));
                                } catch (Exception e) {

                                }
                                //String message = messageObject.getString("message");
                                //String name = "name";
                                String name = preferObject.getString("name");

                                String sentAt = messageObject.getString("sentat");
                                Message messagObject = new Message(userId, message, sentAt, name);
                                messages.add(0, messagObject);
                                username.setText(namewritingto);

                            }

                            adapter = new ThreadAdapter(ChatMessagingActivity.this, messages, userId);
                            recyclerView.setAdapter(adapter);
                            scrollToBottom();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            error.printStackTrace();

        }) {

            //dodanie tokena do naglowka

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref2.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            //dodanie parametrow
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("user_to_id", id);
                map.put("page", "1");
                map.put("count_notification", "0");
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
        RequestQueue queue = Volley.newRequestQueue(ChatMessagingActivity.this);
        queue.getCache().clear();

        queue.add(request);

      pagination();

    }

    private void pagination() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if(added) {
                    if (totalItemCount > previousTotal) {
                        previousTotal = totalItemCount;
                        added = false;
                    }
                }
                if(load){
                    if(totalItemCount>previousTotal){
                        previousTotal = totalItemCount;
                        page++;
                        load=false;
                    }
                }
                if (!load && (firstVisibleItem == 0)){
                    getNext();
                    load=true;

                    Log.v(TAG, "page number: "+page);
                }
            }
        });
    }


    private void getNext() {
        String strPage = String.valueOf(page);
        String strNotificationordsend = String.valueOf(notificationordsend);

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = preferences.getInt("id", -1);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.FETCH_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObj = object.optJSONObject("Message");
                            JSONArray array = jsonObj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject messageObject = array.getJSONObject(i);
                                JSONObject preferObject = messageObject.getJSONObject("preferences");
                                int userId = messageObject.getInt("user_from_id");
                                String message = null;
                                try {
                                    message = decrypt("ciezkiemaslo", messageObject.getString("message"));
                                } catch (Exception e) {

                                }
                                //String message = messageObject.getString("message");
                                //String name = "name";
                                String name = preferObject.getString("name");

                                String sentAt = messageObject.getString("sentat");
                                Message messagObject = new Message(userId, message, sentAt, name);
                                messages.add(0,messagObject);

                            }
                            adapter = new ThreadAdapter(ChatMessagingActivity.this, messages, userId);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                            scrollToLastInserted();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            error.printStackTrace();

        }) {

            //dodanie tokena do naglowka

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref2.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            //dodanie parametrow
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("user_to_id", id);
                map.put("page", strPage);
                map.put("count_notification", strNotificationordsend);
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
        RequestQueue queue = Volley.newRequestQueue(ChatMessagingActivity.this);
        queue.getCache().clear();

        queue.add(request);

    }


//    //Przetwarzanie wiadomości do dodania w wątku
//    private void processMessage(String name, String message, String id) {
//        Message m = new Message(Integer.parseInt(id), message, getTimeStamp(), name);
//        messages.add(m);
//        scrollToBottom();
//    }
    //Przetwarzanie wiadomości do dodania w wątku

    private void processMessage(String title, String body, String id) {
        Message m = new Message(Integer.parseInt(id), body, getTimeStamp(), title);
        messages.add(m);
        scrollToBottom();
        added=true;
        pagination();
        notificationordsend++;


    }


    //Ta metoda wyśle nową wiadomość do wątku
    private void sendMessage() {

        final String message = editTextMessage.getText().toString().trim();
        if (message.equalsIgnoreCase(""))
            return;

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = preferences.getInt("id", -1);
        String user_id_string = String.valueOf(userId);
        userpref = getSharedPreferences("vCard", MODE_PRIVATE);
        String name = "ja";
        String sentAt = getTimeStamp();



// zapis do bazy wiadomosci
        usertoid = getSharedPreferences("usertoid", MODE_PRIVATE);
        String userToId = usertoid.getString("user_to_id", "");

        Message m = new Message(userId, message, sentAt, name);
        messages.add(m);
        adapter.notifyDataSetChanged();
        scrollToBottom();
        added=true;
        pagination();
        notificationordsend++;

        editTextMessage.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SEND_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            //dodanie tokena do naglowka

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref2.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user_id_string);
                //params.put("message", message);
                try {
                    params.put("message", encrypt("ciezkiemaslo",message));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //params.put("name", AppController.getInstance().getUserName());
                params.put("user_to_id", id);
                return params;
            }
        };

        //Wyłączanie ponawiania próby, aby zapobiec duplikowaniu wiadomości
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue queue = Volley.newRequestQueue(ChatMessagingActivity.this);
        queue.add(stringRequest);

    }

    //metoda przewijania widoku recyclerview  na dół
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
    }
    private void scrollToLastInserted() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().scrollToPosition(9);
    }

    //Ta metoda zwróci aktualny znacznik czasu
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //Rejestracja odbiorników nadawczych
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_TOKEN_SENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
    }


    //Wyrejestrowywanie odbiorców
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    //Wysyłanie wiadomości po kliknięciu
    @Override
    public void onClick(View v) {
        if (v == buttonSend)
            sendMessage();
    }

    //Tworzenie menu opcji, aby dodać funkcję wylogowania
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

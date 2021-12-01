package com.pwszit.singiel;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private TextView txtBirthDate;
    private TextView txtName;
    private TextView txtNewDescription;
    private TextView txtNewCity;
    private TextView txtGender;
    SharedPreferences preferences2, userPref;
    private View view;
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private ArrayList<ItemModel> arrayList;
    private Bitmap bitmap = null;
    private Button btnSaveProfile;
    private ProgressDialog dialog;
    private ImageView imageView;
    private static final int GALLERY_ADD_PROFILE = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addList();
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edycja profilu");

        setSupportActionBar(toolbar);

        txtName = findViewById(R.id.txtName);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtNewDescription = findViewById(R.id.txtNewDescription);
        txtNewCity = findViewById(R.id.txtNewCity);
        txtGender = findViewById(R.id.txtNewCity);



//        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
//        String name = preferences.getString("NAME", "");
//        String date = preferences.getString("BIRTHDATE", "");
//        String hobbies = preferences.getString("HOBBIES", "");
//        String city = preferences.getString("CITY", "");
//
//      //  String uriPhoto = preferences.getString("URI_PHOTO", "");
//       // Uri imageUri = Uri.parse(uriPhoto);
//      //  imageView.setImageURI(imageUri);
//
//        txtName.setText(name);
//        txtBirthDate.setText(date);
//        txtNewDescription.setText(hobbies);
//        txtNewCity.setText(city);



        //sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edycja, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivity(new Intent(getApplicationContext()
                        ,EditPassword.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_delete:
                startActivity(new Intent(getApplicationContext()
                        ,DeleteAccount.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_logout:
                logoutUser();
            default:
                return super.onOptionsItemSelected(item);
        }
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
        preferences2 = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

//wyciąganie wartosci z shared do zapisania
        String name = txtName.getText().toString();
        String date = txtBirthDate.getText().toString();
        String city = txtNewCity.getText().toString();
     //   String gender = txtGender.getString("GENDER", "");
        String hobbies = txtNewDescription.getText().toString();



/*//Zapisywanie w shared NOWYCH wartosci
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", name);
        editor.putString("BIRTHDATE", date);
        editor.putString("CITY", city);
        editor.putString("HOBBIES", hobbies);

        // editor.putString("URI_PHOTO", imgUri.toString());

        editor.apply();*/



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
              //  map.put("gender",gender);
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

    //////////////////
    private void logoutUser() {
        dialog.setMessage("Wylogowywanie");
        dialog.show();
        SharedPreferences preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.LOGOUT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn",false);
                    editor.apply();


                    startActivity(new Intent(EditProfile.this, MainActivity.class));
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
    ///////////////










    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }


    private List<ItemModel> addList() {

        List<ItemModel> items = new ArrayList<>();

        userPref = getSharedPreferences("user", MODE_PRIVATE);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.USER_PROFILE, response -> {


            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONArray array = new JSONArray(object.getString("preferences2"));

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject usersObject = array.getJSONObject(i);
                        //JSONObject userObject = usersObject.getJSONObject("user");

                        String val1 = usersObject.getString("photo");
                        String val2 = usersObject.getString("name");
                        String val3 = usersObject.getString("bornDate");
                        String val4 = usersObject.getString("city");
                        String val7 = usersObject.getString("interests");

//                        LocalDate today = LocalDate.now();
//                        LocalDate birthday = LocalDate.parse(val3);
//                        int val6 =  Period.between(birthday, today).getYears();
//                        String yearsold=String.valueOf(val6);
                      //  items.add(new ItemModel(val1, val2, val3, val4, val5, val7));
                        // items.add(new ItemModel(val1, val2, val3,val4));
                        //  items.add(new ItemModel("1636651607.jpeg", "val2", "val3","val4"));
                        //items.add(new ItemModel(val1, val2, val3,val4));


                        /*    String bornDate = usersObject.getString("photo");
                             //  String uriPhoto = preferences.getString("URI_PHOTO", "");
       // Uri imageUri = Uri.parse(uriPhoto);
      //  imageView.setImageURI(imageUri);

        txtName.setText(name);
        txtBirthDate.setText(date);
        txtNewDescription.setText(hobbies);
        txtNewCity.setText(city);
                         */

                    //    imageView.setImageURI(val1);
                        txtName.setText(val2);
                        txtBirthDate.setText(val3);
                        txtNewCity.setText(val4);
                        txtNewDescription.setText(val7);

                        Picasso.get()
                                .load(Constant.URL+"storage/photo/"+ val1)
                                .fit()
                                .centerCrop()
                                .into(imageView);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            error.printStackTrace();

        }) {

            //dodanie tokena do naglowka


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
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
        return items;

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


}



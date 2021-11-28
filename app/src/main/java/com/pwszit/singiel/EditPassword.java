package com.pwszit.singiel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPassword extends AppCompatActivity {
    private Button btnSavePassword;
    EditText oldPassword, newPassword, newPasswordConfirmation;
    SharedPreferences  userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        newPasswordConfirmation = findViewById(R.id.newPasswordConfirmation);

        btnSavePassword = findViewById(R.id.btnSavePassword);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);


        btnSavePassword.setOnClickListener(v -> {
            if(validate()) {
                updatePassword();
            }
        });
    }

    private boolean validate () {
        if (oldPassword.getText().toString().length() < 6) {
            oldPassword.setError("Min. 6 znakow");
            return false;
        }
        if (newPassword.getText().toString().length() < 6) {
            newPassword.setError("Min. 6 znakow");
            return false;
        }
        if (!newPasswordConfirmation.getText().toString().equals(newPassword.getText().toString())) {
            newPasswordConfirmation.setError("Hasła musza być identyczne!");
            return false;
        }


        return true;
    }
    private void updatePassword() {
        dialog.setMessage("Aktualizacja hasła");
        dialog.show();

        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        //wyciąganie wartosci z shared do zapisania
        String oldPass = oldPassword.getText().toString();
        String newPass = newPassword.getText().toString();


        StringRequest request = new StringRequest(Request.Method.POST, Constant.UPDATE_PASSWORD, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){

                    startActivity(new Intent(EditPassword.this, EditProfile.class));
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
                map.put("current_password",oldPass);
                map.put("password",newPass);
                map.put("confirm_password",newPass);
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


        RequestQueue queue = Volley.newRequestQueue(EditPassword.this);
        queue.add(request);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditPassword.this,EditProfile.class));
    }

}

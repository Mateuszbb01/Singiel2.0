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

public class DeleteAccount extends AppCompatActivity {
    private Button btnDeleteAccount;
    EditText passwordHere, passwordConfirmation;
    SharedPreferences userPref;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);


        passwordHere = findViewById(R.id.passwordHere);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);


        btnDeleteAccount.setOnClickListener(v -> {
                if (validate()) {
                    deleteAccount();
                }
        });
    }
    private boolean validate () {
        if (passwordHere.getText().toString().length() < 6) {
            passwordHere.setError("Min. 6 znakow");
            return false;
        }
        if (!passwordConfirmation.getText().toString().equals(passwordHere.getText().toString())) {
            passwordConfirmation.setError("Hasła musza być identyczne!");
            return false;
        }


        return true;
    }
    private void deleteAccount() {
        dialog.setMessage("Dezaktywacja konta");
        dialog.show();


        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        String passssw = passwordHere.getText().toString();


        StringRequest request = new StringRequest(Request.Method.POST, Constant.DELETE_ACCOUNT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putBoolean("isLoggedIn",false);
                    editor.apply();
                    startActivity(new Intent(DeleteAccount.this, MainActivity.class));
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
                map.put("password",passssw);
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
                return 100;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        RequestQueue queue = Volley.newRequestQueue(DeleteAccount.this);
        queue.add(request);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DeleteAccount.this,EditProfile.class));
    }

}

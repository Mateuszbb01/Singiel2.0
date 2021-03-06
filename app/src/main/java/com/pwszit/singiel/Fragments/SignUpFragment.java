package com.pwszit.singiel.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pwszit.singiel.AuthActivity;
import com.pwszit.singiel.Constant;
import com.pwszit.singiel.HomeActivity;
import com.pwszit.singiel.PreferencesActivity;
import com.pwszit.singiel.PreferencesActivity7;
import com.pwszit.singiel.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail,layoutPassword,layoutConfirm;
    private TextInputEditText txtEmail,txtPassword,txtConfirm;
    private TextView txtSignIn;
    private Button btnSignUp;
    private ProgressDialog dialog;

    public SignUpFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign_up,container, false);
        init();
        return view;
    }

    private void init() {
        layoutEmail = view.findViewById(R.id.txtLayoutEmailSignUp);
        layoutPassword = view.findViewById(R.id.txtLayoutPasswordSignUp);
        layoutConfirm = view.findViewById(R.id.txtLayoutConfirmSignUp);
        txtEmail = view.findViewById(R.id.txtEmailSignUp);
        txtPassword = view.findViewById(R.id.txtPasswordSignUp);
        txtConfirm = view.findViewById(R.id.txtConfirmSignUp);
        txtSignIn = view.findViewById(R.id.txtSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtSignIn.setOnClickListener(v->{
            //change fragments
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()).commit();
        });

        btnSignUp.setOnClickListener(v->{

            if (validate()) {
                register();
            }
        });
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtEmail.getText().toString().isEmpty()) {
                    layoutEmail.setErrorEnabled(false);
                }

            }


            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtPassword.getText().toString().isEmpty()) {
                    layoutPassword.setErrorEnabled(false);
                }

            }


            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        txtConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtConfirm.getText().toString().isEmpty()) {
                    layoutConfirm.setErrorEnabled(false);
                }

            }


            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private boolean validate () {
        String val = txtEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //je??li chcemy walidacj?? do has??a
        String regex = "^"+
                "(?=.*[0-9])" //przynajmniej jedna cyfra
                + "(?=.*[a-z])(?=.*[A-Z])" //przynajmniej jedna mala i jedna duza litera
                + "(?=.*[@#$%^&+=])" //przynajmniej 1 znak specjalny
                + "(?=\\S+$).{8,20}$"; //bez spacji i przynajmniej 6 znakow, max 20
        if (txtEmail.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Musisz poda?? email");
            return false;
        }
        if (!val.matches(emailPattern)) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Nieprawid??owy mail");
            return false;
        }
        if (txtPassword.getText().toString().length() < 6) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Min. 6 znakow");
            layoutConfirm.setError("Min. 6 znakow");
            return false;
        }
        if (!txtConfirm.getText().toString().equals(txtPassword.getText().toString())) {
            layoutConfirm.setErrorEnabled(true);
            layoutConfirm.setError("Has??a musza by?? identyczne!");
            layoutPassword.setError("Has??a musza by?? identyczne!");
            return false;
        }


        return true;
    }

    private void register() {
        dialog.setMessage("rejestracja w toku.");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response -> {
            //dostajemy odpowiedz jesli jest polaczenie z serwerem
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    //make shared preference user
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putInt("id",user.getInt("id"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    //udana rejestracja
                    startActivity(new Intent(((AuthActivity)getContext()), PreferencesActivity7.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Pomy??lna rejs!", Toast.LENGTH_SHORT).show();


               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }, error -> {
            //b????d jesli nie ma po????czenia
            error.printStackTrace();
            dialog.dismiss();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",txtEmail.getText().toString());
                map.put("password",txtPassword.getText().toString());
                map.put("password_confirmation",txtPassword.getText().toString());
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
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}

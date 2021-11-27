package com.pwszit.singiel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.pwszit.singiel.Fragments.UsersAllAdapter;
import com.pwszit.singiel.Fragments.UsersAllObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {



    TabLayout tabLayout;
    TabItem mchat,mcall;
    ViewPager viewPager;
   // PagerAdapter pagerAdapter;
     androidx.appcompat.widget.Toolbar mtoolbar;

    View rootView;
    Activity mActivity;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private static RecyclerView recyclerView;
    Button logout ;
    Button refresh;
    TextView welcome_name;
    public static ArrayList<UsersAllObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//
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
//            Intent itent=null;
//            if(AppController.getInstance().isLoggedIn())
//                itent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
//            else if(AppController.getInstance().LecturerisLoggedIn())
//                itent = new Intent(getApplicationContext(), GCMRegistrationIntentServiceLecturer.class);
//            getApplicationContext().startService(itent);
//        }
//
//
//        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                data.clear();
//                fetchData();
//            }
//        });
//
//
//        recyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        data = new ArrayList<UsersAllObject>();
//        data.clear();
//        fetchData();
//
//    }
//
//    public void fetchData()
//    {
//        if (AppController.getInstance().isLoggedIn()) {
//            welcome_name.setText("Welcome "+AppController.getInstance().getStudentName());
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FETCH_GETALLLECTURER,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//
//                            data.clear();
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                JSONArray thread = res.getJSONArray("alllecturer");
//                                if (thread.length()<=0)
//                                {
//                                    Toast.makeText(UsersAll.this,"No Lecturer Available",Toast.LENGTH_SHORT).show();
//                                }
//                                for (int i = 0; i < thread.length(); i++) {
//
//                                    JSONObject obj = thread.getJSONObject(i);
//
//
//                                            /*  for (int j=0;j<7;j++)
//                                              {*/
//                                    data.add(new UsersAllObject(obj.getString("id"),obj.getString("name"),obj.getString("photo")));
//
//
//                                    //location = (TextView) view.findViewById(R.id.col);
//
//
//
//
//                                }
//
//                                //type.setText(obj.getString("type"));
//                                adapter = new UsersAllAdapter(data,UsersAll.this);
//                                recyclerView.setAdapter(adapter);
//
//
//
//                            }
//
//
//
//
//
//                            catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                /*if (AppController.getInstance().drisLoggedIn()) {
//                    params.put("from_id", String.valueOf(AppController.getInstance().getDRId()));
//                }*/
//
//
//
//
//                    //   params.put("name", AppController.getInstance().getUserName())
//                    return params;
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(stringRequest);
//        }
//        else if (AppController.getInstance().LecturerisLoggedIn()) {
//            welcome_name.setText("Welcome "+AppController.getInstance().getLecturerName());
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FETCH_GETALLSTUDENT,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            //tbl.removeAllViewsInLayout();
//                            data.clear();
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                JSONArray thread = res.getJSONArray("allstudents");
//                                if (thread.length()<=0)
//                                {
//                                    Toast.makeText(mActivity,"No Lecturer Available",Toast.LENGTH_SHORT).show();
//                                }
//                                for (int i = 0; i < thread.length(); i++) {
//
//                                    JSONObject obj = thread.getJSONObject(i);
//
//
//                                            /*  for (int j=0;j<7;j++)
//                                              {*/
//                                    data.add(new UsersAllObject(obj.getString("id"),obj.getString("name"),obj.getString("photo")));
//
//
//                                    //location = (TextView) view.findViewById(R.id.col);
//
//
//
//
//                                }
//
//                                //type.setText(obj.getString("type"));
//                                adapter = new UsersAllAdapter(data,UsersAll.this);
//                                recyclerView.setAdapter(adapter);
//
//
//                            }
//
//
//
//
//
//                            catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                /*if (AppController.getInstance().drisLoggedIn()) {
//                    params.put("from_id", String.valueOf(AppController.getInstance().getDRId()));
//                }*/
//
//
//
//
//                    //   params.put("name", AppController.getInstance().getUserName())
//                    return params;
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(stringRequest);
//        }
/*


    tabLayout=findViewById(R.id.include);
    mchat=findViewById(R.id.chat);
    mcall=findViewById(R.id.calls);
    viewPager=findViewById(R.id.fragmentcontainer);

    mtoolbar=findViewById(R.id.toolbar);
    setSupportActionBar(mtoolbar);


    pagerAdapter=new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
    viewPager.setAdapter(pagerAdapter);

    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());

            if(tab.getPosition()==0 || tab.getPosition()==1|| tab.getPosition()==2)
            {
                pagerAdapter.notifyDataSetChanged();
            }


        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });

    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
*/




    }




}
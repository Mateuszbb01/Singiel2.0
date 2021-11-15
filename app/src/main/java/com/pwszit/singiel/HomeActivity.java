package com.pwszit.singiel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private View view;
    private RecyclerView RecyclerView;
    private ArrayList<ItemModel> arrayList;
    private Button btnEditProfile;
    private static final String TAG = "HomeActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private SharedPreferences userPref, sharedPreferences;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addList();
        setContentView(R.layout.activity_home);
        mTextViewResult = findViewById(R.id.textView);
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {

                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    Toast.makeText(HomeActivity.this, "♥", Toast.LENGTH_SHORT).show();

                    sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
                    String userlikedid= sharedPreferences.getString("USERLIKEDID", "");
                    StringRequest request = new StringRequest(Request.Method.POST,Constant.USERS_PAIRING, response->{

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                // SharedPreferences.Editor editor = userPref.edit();
                                //editor.putString("photo",object.getString("photo"));
                                // editor.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    },error ->{
                        error.printStackTrace();
                    } ){

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
                            map.put("user_like_id",userlikedid);
                            map.put("like","1");

                            return map;
                        }
                    };



                    RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                    queue.add(request);




                }
                if (direction == Direction.Top){
                    Toast.makeText(HomeActivity.this, "Zdecyduj później", Toast.LENGTH_SHORT).show();


                }
                if (direction == Direction.Left){
                    Toast.makeText(HomeActivity.this, "Odrzuciłeś użytkownika", Toast.LENGTH_SHORT).show();


                    sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
                    String userlikedid= sharedPreferences.getString("USERLIKEDID", "");
                    StringRequest request = new StringRequest(Request.Method.POST,Constant.USERS_PAIRING, response->{

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                // SharedPreferences.Editor editor = userPref.edit();
                                //editor.putString("photo",object.getString("photo"));
                                // editor.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    },error ->{
                        error.printStackTrace();
                    } ){

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
                            map.put("user_like_id",userlikedid);
                            map.put("like","0");

                            return map;
                        }
                    };



                    RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                    queue.add(request);


                }
                if (direction == Direction.Bottom){
                    Toast.makeText(HomeActivity.this, "Zdecyduj później", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);

                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                TextView tvId = view.findViewById(R.id.userlikedid);
                Log.d(TAG, "onCardDisappeared: " + position + ", name: " + tv.getText()+ ", iduderliked: " + tvId.getText());
                sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

                String userlikeid = (String) tvId.getText();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USERLIKEDID", userlikeid);
                editor.apply();



            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);


    }
    /*    private void send()

        {
           int position = manager.getTopPosition().toString();
           mTextViewResult.append(position);

        }*/
    private void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> uptodate = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, uptodate);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setItems(uptodate);
        result.dispatchUpdatesTo(adapter);
    }

    private List<ItemModel> addList() {

        List<ItemModel> items = new ArrayList<>();

        userPref = getSharedPreferences("user", MODE_PRIVATE);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.SHOW_USERS, response -> {


            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONArray array = new JSONArray(object.getString("users"));

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject usersObject = array.getJSONObject(i);
                        //JSONObject userObject = usersObject.getJSONObject("user");

                        String val1 = usersObject.getString("photo");
                        String val2 = usersObject.getString("name");
                        String val3 = usersObject.getString("bornDate");
                        String val4 = usersObject.getString("city");
                        String val5 = usersObject.getString("user_id");
                        String val7 = usersObject.getString("interests");

                        LocalDate today = LocalDate.now();
                        LocalDate birthday = LocalDate.parse(val3);
                        int val6 =  Period.between(birthday, today).getYears();
                        String yearsold=String.valueOf(val6);
                        items.add(new ItemModel(val1, val2, yearsold,val4, val5, val7));
                        // items.add(new ItemModel(val1, val2, val3,val4));
                        //  items.add(new ItemModel("1636651607.jpeg", "val2", "val3","val4"));
                        //items.add(new ItemModel(val1, val2, val3,val4));


                        /*    String bornDate = usersObject.getString("photo");
                         */
                        mTextViewResult.append(val2);


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
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(request);
        return items;

    }




    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Intent intent;
                    switch (menuItem.getItemId()){
                        case R.id.czat:
                            startActivity(new Intent(getApplicationContext()
                                    ,HomeActivity.class));
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
                            return true;

                    }

                    return false;
                }
            };


}
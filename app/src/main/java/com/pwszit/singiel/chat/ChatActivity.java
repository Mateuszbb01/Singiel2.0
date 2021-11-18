package com.pwszit.singiel.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.pwszit.singiel.PagerAdapter;
import com.pwszit.singiel.R;

public class ChatActivity extends AppCompatActivity {



    TabLayout tabLayout;
    TabItem mchat,mcall;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
     androidx.appcompat.widget.Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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
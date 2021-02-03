package com.senpai.firebasepushnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView mProfileLabel, mUsersLabel, mNotificationsLabel;

    private ViewPager mMainPager;
    private PagerViewAdapter mPagerViewAdapter;
    private TabLayout tabLayout;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mProfileLabel = findViewById(R.id.profileLabel);
        mUsersLabel = findViewById(R.id.userLabel);
        mNotificationsLabel = findViewById(R.id.notificationsLabel);
        tabLayout = findViewById(R.id.tabLayout);

        mMainPager = findViewById(R.id.mainPager);
        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);
        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout.setupWithViewPager(mMainPager);



    }

    private void changeTabs(int position) {
        switch (position){
            case 0:

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            sendToLogin();
        }
    }

    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
}
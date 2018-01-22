package com.example.alexander.edadarom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.alexander.edadarom.adapters.ViewPagerAdapter;
import com.example.alexander.edadarom.fragments.FragmentFavorites;
import com.example.alexander.edadarom.fragments.Browse.FragmentBrowse;
import com.example.alexander.edadarom.fragments.FragmentMessages;
import com.example.alexander.edadarom.fragments.FragmentPersonal;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentBrowse fragmentBrowse;
    FragmentFavorites fragmentFavorites;
    FragmentMessages fragmentMessages;
    FragmentPersonal fragmentPersonal;
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    final static String TAG = "myLogs_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_browse:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_add:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_messages:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_profile:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prevMenuItem!=null){
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem=bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentBrowse = new FragmentBrowse();
        fragmentFavorites = new FragmentFavorites();
        fragmentMessages = new FragmentMessages();
        fragmentPersonal = new FragmentPersonal();
        adapter.addFragment(fragmentBrowse);
        adapter.addFragment(fragmentFavorites);
        adapter.addFragment(fragmentMessages);
        adapter.addFragment(fragmentPersonal);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }


    public void btnClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}

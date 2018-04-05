package com.example.alexander.edadarom;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alexander.edadarom.adapters.ViewPagerAdapter;
import com.example.alexander.edadarom.fragments.Browse.FragmentBrowseLastItems;
import com.example.alexander.edadarom.fragments.FragmentPersonal;
import com.example.alexander.edadarom.fragments.MyReservations.FragmentReservations;
import com.example.alexander.edadarom.utils.CustomViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentBrowseLastItems fragmentBrowseLastItems;
    FragmentReservations fragmentReservations;
    FragmentPersonal fragmentPersonal;
    private CustomViewPager viewPager;
    MenuItem prevMenuItem;
    final static String TAG = "myLogs_MainActivity";

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_browse:
                    viewPager.setCurrentItem(0, false);
                    break;
                case R.id.menu_reservations:
                    viewPager.setCurrentItem(1, false);
                    break;
                case R.id.menu_profile:
                    viewPager.setCurrentItem(3, false);
                    break;
            }
            return false;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentBrowseLastItems = new FragmentBrowseLastItems();
        fragmentReservations = new FragmentReservations();
        fragmentPersonal = new FragmentPersonal();
        adapter.addFragment(fragmentBrowseLastItems);
        adapter.addFragment(fragmentReservations);
        adapter.addFragment(fragmentPersonal);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }


    public void btnClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, String> map = new HashMap<>();
        map.put("userId", firebaseUser.getUid());
        map.put("adId", "q4EmJPSY6rpv7ldQ12y4");
        db.collection("pushTest").add(map).addOnSuccessListener(documentReference -> {

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fragmentReservations.isAdded()) {
            FragmentManager childFragmentManager = fragmentReservations.getChildFragmentManager();
            if (childFragmentManager.getBackStackEntryCount() > 0)
                childFragmentManager.popBackStackImmediate();
            else super.onBackPressed();
        } else super.onBackPressed();
    }
}

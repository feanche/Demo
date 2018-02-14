package com.example.alexander.edadarom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.example.alexander.edadarom.Authorization.LoginActivity;
import com.example.alexander.edadarom.adapters.ViewPagerAdapter;
import com.example.alexander.edadarom.fragments.Category.FragmentCategory;
import com.example.alexander.edadarom.fragments.FragmentFavorites;
import com.example.alexander.edadarom.fragments.Browse.FragmentBrowse;
import com.example.alexander.edadarom.fragments.FragmentMessages;
import com.example.alexander.edadarom.fragments.FragmentPersonal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentCategory fragmentCategory;
    FragmentFavorites fragmentFavorites;
    FragmentMessages fragmentMessages;
    FragmentPersonal fragmentPersonal;
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    final static String TAG = "myLogs_MainActivity";
    public int categoryId = -1;

    //Toolbar back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
        fragmentCategory = new FragmentCategory();
        fragmentFavorites = new FragmentFavorites();
        fragmentMessages = new FragmentMessages();
        fragmentPersonal = new FragmentPersonal();
        adapter.addFragment(fragmentCategory);
        adapter.addFragment(fragmentFavorites);
        adapter.addFragment(fragmentMessages);
        adapter.addFragment(fragmentPersonal);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }


    public void btnClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, String> map = new HashMap<>();
        map.put("userId", firebaseUser.getUid());
        map.put("adId", "q4EmJPSY6rpv7ldQ12y4");
        db.collection("pushTest").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onBackPressed() {
        if (fragmentCategory.isAdded()) {
            FragmentManager childFragmentManager = fragmentCategory.getChildFragmentManager();
            //Если сейчас viewPager в позиции 0, то проверяем количество дочерних фрагментов у нулевого фрагмента и нажимаем назад
            if (viewPager.getCurrentItem() == 0 & childFragmentManager.getBackStackEntryCount() > 0)
                childFragmentManager.popBackStackImmediate();
            else super.onBackPressed();
        } else super.onBackPressed();
    }
}

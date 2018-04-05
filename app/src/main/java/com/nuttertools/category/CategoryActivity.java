package com.nuttertools.category;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nuttertools.R;
import com.nuttertools.fragments.Category.FragmentCategory;

public class CategoryActivity extends AppCompatActivity {
    FragmentCategory fragment;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showFragment();
    }

    private void showFragment() {
        fragment = new FragmentCategory();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.content, fragment)
                //.addToBackStack("fragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (fragment.isAdded()) {
            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager.getBackStackEntryCount() > 0)
                childFragmentManager.popBackStackImmediate();
            else super.onBackPressed();
        } else super.onBackPressed();
    }
}

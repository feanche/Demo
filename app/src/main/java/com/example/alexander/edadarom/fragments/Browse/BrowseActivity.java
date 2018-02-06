package com.example.alexander.edadarom.fragments.Browse;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alexander.edadarom.R;

public class BrowseActivity extends AppCompatActivity {

    //Toolbar back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        showFragment();
    }

    private void showFragment() {
        FragmentBrowse fragmentBrowse = new FragmentBrowse();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.rootView, fragmentBrowse)
                .commit();
    }
}

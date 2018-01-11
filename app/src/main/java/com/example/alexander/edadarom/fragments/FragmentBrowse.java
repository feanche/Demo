package com.example.alexander.edadarom.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.NewItem.AddNewItemActivity;
import com.example.alexander.edadarom.R;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentBrowse extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    FloatingActionButton mFab;
   // private UserAdapter adapter;
   public static final int NEW_ITEM = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
       // adapter = new UserAdapter(result);
        //recyclerView.setAdapter(adapter);
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                 startActivityForResult(intent, NEW_ITEM);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

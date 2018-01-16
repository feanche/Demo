package com.example.alexander.edadarom.fragments.Browse;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alexander.edadarom.MapsActivity;
import com.example.alexander.edadarom.NewItem.AddNewItemActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.Browse.adapters.UserAdsAdapter;
import com.example.alexander.edadarom.models.UserAdsModel;

import java.util.ArrayList;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentBrowse extends Fragment implements BrowseFragmentContract.View {

    private BrowseFragmentContract.Presenter presenter;
    private View view;
    private RecyclerView recyclerView;
    FloatingActionButton mFab;
    private UserAdsAdapter adapter;
    public static final int NEW_ITEM = 1;
    public static final int GET_LOCATION = 1;
    private ArrayList<UserAdsModel> arUserAds;
    private Button mapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse, container, false);

        initRecyclerView();
        initButtons();
        presenter = new BrowsePresenter(this);
        presenter.getAds();

        return view;
    }

    private void initButtons() {
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                 startActivityForResult(intent, NEW_ITEM);
            }
        });

        mapButton = (Button)view.findViewById(R.id.button3);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivityForResult(intent, GET_LOCATION);
            }
        });
    }

    private void initRecyclerView() {
        arUserAds = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new UserAdsAdapter(getContext(), arUserAds);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void addDate(ArrayList<UserAdsModel> ar) {
        arUserAds.clear();
        arUserAds.addAll(ar);
        adapter.notifyDataSetChanged();
    }
}

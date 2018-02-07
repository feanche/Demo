package com.example.alexander.edadarom.fragments.Browse;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.FullInfo.FullInfoActivity;
import com.example.alexander.edadarom.MainActivity;
import com.example.alexander.edadarom.NewItemActivity.AddNewItemActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.Browse.adapters.UserAdsAdapter;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.ItemClickSupport;

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
    private ArrayList<UserAdsModel> arUserAds;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    int adId;
    public static Bundle bundle = new Bundle();

    public static FragmentBrowse instance(int id, String name) {
        bundle.putInt("id", id);
        bundle.putString("name", name);
        return new FragmentBrowse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));
        toolbar = view.findViewById(R.id.toolbar);
        String title = bundle.getString("name");
        toolbar.setTitle(title);
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecyclerView();
        initButtons();
        presenter = new BrowsePresenter(this);
        adId = bundle.getInt("id", -1);
        presenter.getAds(adId);

        return view;
    }

    private void initButtons() {
        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddNewItemActivity.class);
             startActivityForResult(intent, NEW_ITEM);
        });
    }

    private void initRecyclerView() {
        arUserAds = new ArrayList<>();

        adapter = new UserAdsAdapter(getContext(), arUserAds);

        recyclerView = view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), FullInfoActivity.class);
                intent.putExtra("key", arUserAds.get(position).getId());
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    presenter.getAds(adId);
                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addDate(ArrayList<UserAdsModel> ar) {
        arUserAds.clear();
        arUserAds.addAll(ar);
        adapter.notifyDataSetChanged();
    }


}

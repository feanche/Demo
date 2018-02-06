package com.example.alexander.edadarom.fragments.Category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.FullInfo.FullInfoActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.Browse.BrowseActivity;
import com.example.alexander.edadarom.fragments.Browse.BrowseFragmentContract;
import com.example.alexander.edadarom.fragments.Browse.FragmentBrowse;
import com.example.alexander.edadarom.fragments.Browse.adapters.UserAdsAdapter;
import com.example.alexander.edadarom.utils.ItemClickSupport;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentCategory extends Fragment implements CategoryMvp.View {

    private View view;
    private CategoryMvp.Presenter presenter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Category> ar = new ArrayList<>();
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        initRecyclerView();
        presenter = new CategoryPresenter();
        presenter.attachView(this);
        presenter.getDate();
        return view;
    }

    private void initRecyclerView() {
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        ar = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), ar);

        recyclerView = view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getContext(), 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                presenter.recyclerItemClick(position);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    presenter.getDate();
                }
        );

    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
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
    public void updateRecyclerView(ArrayList<Category> categories) {
        ar.clear();
        ar.addAll(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openActivity(int id, String name) {
        /*Intent intent = new Intent(getContext(), BrowseActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivity(intent);*/
        showFragment();
    }

    private void showFragment() {
        FragmentBrowse fragmentBrowse = new FragmentBrowse();

        /*FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, fragmentBrowse)
                .addToBackStack("browseFragment")
                .commit();*/

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack("browseFragment");
        transaction.replace(R.id.container, fragmentBrowse)
        .commit();
    }


}

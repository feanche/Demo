package com.nuttertools.fragments.Category;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nuttertools.R;
import com.nuttertools.category.CategoryActivity;
import com.nuttertools.fragments.Browse.FragmentBrowse;
import com.nuttertools.utils.EmptyFragment;
import com.nuttertools.utils.ItemClickSupport;

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
    private ConstraintLayout container_empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        ((CategoryActivity) getActivity()).setSupportActionBar(toolbar);
        ((CategoryActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_categories));
        ((CategoryActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CategoryActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> presenter.recyclerItemClick(position));

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getDate());
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
        FragmentBrowse fragmentBrowse = FragmentBrowse.instance(id, name);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack("browseFragment");
        transaction.replace(R.id.container, fragmentBrowse)
                .commit();
    }

    @Override
    public void emptyCheck() {
        container_empty = view.findViewById(R.id.container_empty_category);
        if (adapter.getItemCount() == 0) {
            EmptyFragment emptyFragment = EmptyFragment.instance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container_empty_category, emptyFragment)
                    .commit();
            container_empty.setVisibility(View.VISIBLE);
        } else {
            container_empty.setVisibility(View.GONE);
        }
    }
}
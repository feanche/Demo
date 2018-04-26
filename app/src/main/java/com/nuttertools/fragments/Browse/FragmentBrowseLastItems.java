package com.nuttertools.fragments.Browse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nuttertools.FullInfo.FullInfoActivity;
import com.nuttertools.MainActivity;
import com.nuttertools.NewItemActivity.AddNewItemActivity;
import com.nuttertools.R;
import com.nuttertools.category.CategoryActivity;
import com.nuttertools.fragments.Browse.adapters.UserAdsAdapter;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.ItemClickSupport;

import java.util.ArrayList;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentBrowseLastItems extends Fragment implements BrowseFragmentContract.View {

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
    private Menu menu;

    public static FragmentBrowseLastItems instance(int id, String name) {
        bundle.putInt("id", id);
        bundle.putString("name", name);
        return new FragmentBrowseLastItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_last_items, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_last_added_items));

        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        initRecyclerView();
        initButtons();
        presenter = new BrowsePresenter(this);
        adId = bundle.getInt("id", -1);
        presenter.getLastAddedItems(20);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(toolbar!=null) ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
    }

    private void initButtons() {
        mFab = view.findViewById(R.id.fab);
        mFab.setVisibility(View.INVISIBLE);
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

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), FullInfoActivity.class);
            intent.putExtra("key", arUserAds.get(position).getId());
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    presenter.getLastAddedItems(20);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(getContext(), CategoryActivity.class));
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
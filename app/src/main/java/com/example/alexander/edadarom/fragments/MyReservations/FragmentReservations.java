package com.example.alexander.edadarom.fragments.MyReservations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.category.CategoryActivity;

import com.example.alexander.edadarom.fragments.MyReservations.Adapters.ReservationAdapter;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.example.alexander.edadarom.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentReservations extends Fragment implements Reservations.View{
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<UserAdsModel> ar = new ArrayList<>();
    private ReservationAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        /*((CategoryActivity) getActivity()).setSupportActionBar(toolbar);
        ((CategoryActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_launcher);*/

        initRecyclerView();
        getDate();
        return view;
    }

    private void getDate() {
        ar.clear();
        swipeRefreshLayout.setRefreshing(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().size() == 0) {
                                    //view.showToast("Нет данных");
                                    swipeRefreshLayout.setRefreshing(false);

                                    return;
                                }

                                for (DocumentSnapshot document : task.getResult()) {
                                    UserAdsModel userAdsModel = document.toObject(UserAdsModel.class);
                                    ar.add(userAdsModel);
                                }
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), "Ошибка " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initRecyclerView() {
        adapter = new ReservationAdapter(getContext(), ar);
        recyclerView = view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ReservationAdapter.DotsClickListener() {
            @Override
            public void onClick(int position) {

            }
        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    getDate();
                }
        );

    }

}

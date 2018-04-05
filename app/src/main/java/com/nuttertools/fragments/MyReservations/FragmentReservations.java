package com.nuttertools.fragments.MyReservations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuttertools.MainActivity;
import com.nuttertools.MyAds.MyAdsFullActivity;
import com.nuttertools.R;

import com.nuttertools.category.CategoryActivity;
import com.nuttertools.fragments.MyReservations.Adapters.ReservationAdapter;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class FragmentReservations extends Fragment {
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<UserAdsModel> ar = new ArrayList<>();
    private ReservationAdapter adapter;
    private RecyclerView recyclerView;
    private MaterialDialog dialog;
    private ConstraintLayout container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservations, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);




        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Мои заказы");

        //((MainActivity) getActivity()).setSupportActionBar(toolbar);
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                createListDialog(position);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentReservationsFull fragmentReservationsFull = new FragmentReservationsFull();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1, fragmentReservationsFull);
                fragmentTransaction.addToBackStack("fragmentReservationFull");
                fragmentTransaction.commit();
                container = view.findViewById(R.id.container1);
                container.setVisibility(View.VISIBLE);

                Bundle b = new Bundle();
                b.putString("id", ar.get(position).getId());
                b.putString("title", ar.get(position).getTitle());
                fragmentReservationsFull.setArguments(b);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    getDate();
                }
        );

    }

    public MaterialDialog createListDialog(int position) {
        return new MaterialDialog.Builder(getContext())
                .items(R.array.dialog_reservation_activity)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                cancelReservation(position);
                                break;
                        }
                    }
                })
                .show();
    }

    public MaterialDialog createDialog() {
        return new MaterialDialog.Builder(getContext())
                .content("Пожалуйста, подождите!")
                .progress(true, 0)
                .show();
    }

    public void cancelReservation(int position) {
        dialog = createDialog();
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserAdsModel ads = ar.get(position);

        if (firebaseUser != null) {
            // Get a new write batch
            WriteBatch batch = db.batch();

            // Update the Ads
            DocumentReference adsRef = db.collection(FirebaseConst.ADS).document(ads.getId());
            DocumentReference myReservationRef = db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(ads.getId());


            //Добавляем её к модели пользователя
            //ads.setReservationInfo(null);
            //добавляем информацию о статусе бронирование
            //ads.setReserved(false);

//            batch.set(adsRef, ads);
            batch.delete(myReservationRef);

            // Commit the batch
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ar.remove(position);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }

    }

}

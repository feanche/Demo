package com.nuttertools.fragments.MyReservations;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuttertools.R;

import com.nuttertools.fragments.MyReservations.Adapters.ReservationAdapter;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.EmptyFragment;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private FragmentReservationsFull fragmentReservationsFull;
    private ConstraintLayout container_empty;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reservations, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_my_orders));

        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setHasOptionsMenu(true);

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
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
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
                    })
                    .addOnFailureListener(e -> {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Ошибка " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(queryDocumentSnapshots -> emptyCheck());
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void emptyCheck() {
        container_empty = view.findViewById(R.id.container_empty_reservations);
        if (adapter.getItemCount() == 0) {
            EmptyFragment emptyFragment = EmptyFragment.instance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container_empty_reservations, emptyFragment)
                    .commit();
            container_empty.setVisibility(View.VISIBLE);
        } else {
            container_empty.setVisibility(View.GONE);
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

        adapter.setClickListener(position -> createListDialog(position));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            fragmentReservationsFull = new FragmentReservationsFull();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_reservations, fragmentReservationsFull);
            fragmentTransaction.addToBackStack("fragmentReservationFull");
            fragmentTransaction.commit();
            container = view.findViewById(R.id.container_reservations);
            container.setVisibility(View.VISIBLE);
            Bundle b = new Bundle();
            b.putString("id", ar.get(position).getId());
            b.putString("title", ar.get(position).getTitle());
            fragmentReservationsFull.setArguments(b);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getDate();
        });
    }

    public MaterialDialog createListDialog(int position) {
        return new MaterialDialog.Builder(getContext())
                .items(R.array.dialog_reservation_activity)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            cancelReservation(position);
                            break;
                    }
                })
                .show();
    }

    public MaterialDialog createDialog() {
        return new MaterialDialog.Builder(getContext())
                .content(getString(R.string.dialog_wait))
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
            WriteBatch batch = db.batch();
            DocumentReference myReservationRef = db
                    .collection(FirebaseConst.USERS)
                    .document(firebaseUser.getUid())
                    .collection(FirebaseConst.MY_RESERVATIONS)
                    .document(ads.getId());
            batch.delete(myReservationRef);
            batch.commit()
                    .addOnCompleteListener(task -> {
                        ar.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        emptyCheck();
                    });
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(fragmentReservationsFull!=null) {
                if (fragmentReservationsFull.isVisible()) fragmentReservationsFull.setMenuVisibility(visible);
            }
        }
    }

}
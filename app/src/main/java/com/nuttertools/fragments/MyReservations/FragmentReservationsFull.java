package com.nuttertools.fragments.MyReservations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nuttertools.MainActivity;
import com.nuttertools.R;
import com.nuttertools.models.ReservationInfo;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.models.Users;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FragmentReservationsFull extends Fragment {

    private View view;
    private TextView tvBrn;
    private ImageView ivOpen;
    private TextView tvProfileTitle;
    private ImageView ivProfile;
    private TextView tvTitle;
    private ImageView ivAd;
    private TextView tvCoastPrice;
    private TextView tvFinishCoast;
    private TextView tvShipPrice;
    private TextView tvFinishCoastPrice;
    private TextView tvStatus;
    private TextView tvReservation;
    private TextView tvAddressTitle;
    private TextView tvAddressSubtitle;
    private CardView cardViewAddress;
    private CardView btnReservation;
    private ConstraintLayout topView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserAdsModel userAdsModel;
    private String adId;
    private String adTitle;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservations_full, container, false);
        initView();
        adId = getArguments().getString("id");
        setHasOptionsMenu(true);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_my_booking));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        getDate();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setUserVisibleHint(false);
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        tvBrn = view.findViewById(R.id.tvBrn);
        ivOpen = view.findViewById(R.id.ivOpen);
        tvProfileTitle = view.findViewById(R.id.tvProfileTitle);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivAd = view.findViewById(R.id.ivAd);
        tvCoastPrice = view.findViewById(R.id.tvCoastPrice);
        tvFinishCoast = view.findViewById(R.id.tvFinishCoast);
        tvShipPrice = view.findViewById(R.id.tvShipPrice);
        tvFinishCoastPrice = view.findViewById(R.id.tvFinishCoastPrice);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvReservation = view.findViewById(R.id.tvReservation);
        tvAddressTitle = view.findViewById(R.id.tvAddressTitle);
        tvAddressSubtitle = view.findViewById(R.id.tvAddressSubtitle);
        cardViewAddress = view.findViewById(R.id.card_view3);

        //Кнопка подтвердить
        btnReservation = view.findViewById(R.id.btnReservation);
        btnReservation.setVisibility(View.INVISIBLE);

        topView = view.findViewById(R.id.topView);
        topView.setVisibility(View.INVISIBLE);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));
        swipeRefreshLayout.setOnRefreshListener(() -> getDate());
    }

    private void getDate() {
        swipeRefreshLayout.setRefreshing(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(adId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userAdsModel = task.getResult().toObject(UserAdsModel.class);
                            getUserDate(userAdsModel);
                        } else {

                        }
                    });
        }
    }

    private void getUserDate(UserAdsModel userAdsModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(FirebaseConst.USERS).document(userAdsModel.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    topView.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()) {
                        Users user = task.getResult().toObject(Users.class);
                        updateUI(user, userAdsModel);
                    } else {

                    }
                });
    }

    private void updateUI(Users user, UserAdsModel u) {
        tvTitle.setText(u.getTitle());
        tvProfileTitle.setText(user.getFirstName());
        GlideApp.with(getContext())
                .asBitmap()
                .load(user.getPhoto())
                .into(ivProfile);

        setDelivery(u.getReservationInfo());
        setStatus(tvStatus, u.getReservationInfo());


        if(userAdsModel.getReservationInfo()!=null) {
            SimpleDateFormat sf = new SimpleDateFormat("d MMMM HH:mm", new Locale("ru", "RU"));
            String date = sf.format(u.getReservationInfo().getReservationDate());
            String dateEnd = sf.format(u.getReservationInfo().getReservationDateEnd());
            tvReservation.setText("Начало брони " + date + "\nКонец брони " + dateEnd);
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(u.getPhotoUrl().get(0))
                    .into(ivAd);
        }
    }

    private void setDelivery(ReservationInfo info) {
        if(info!=null) {
            if (info.isDelivery()) {
                cardViewAddress.setVisibility(View.VISIBLE);
                tvAddressTitle.setText(getString(R.string.tv_delivery_method));
                tvAddressSubtitle.setText(info.getAddress().getLocality());
            } else cardViewAddress.setVisibility(View.INVISIBLE);
        } else cardViewAddress.setVisibility(View.INVISIBLE);

    }

    private void setStatus(TextView textView, ReservationInfo info) {
        btnReservation.setVisibility(View.INVISIBLE);
        if(info!=null) {
            switch (info.getStatus()) {
                case ReservationInfo.STATUS_FREE:

                    break;
                case ReservationInfo.STATUS_WAIT_CONFIRM:
                    textView.setText(getString(R.string.tv_pending_approval));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow_700));

                    btnReservation.setVisibility(View.VISIBLE);
                    tvBrn.setText(getString(R.string.tv_confirm));
                    break;
                case ReservationInfo.STATUS_CONFIRMED:
                    textView.setText(getString(R.string.tv_confirmed));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.green_700));
                    break;
                case ReservationInfo.STATUS_WAIT_RETURN:
                    textView.setText(getString(R.string.tv_waiting_for_return));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow_700));
                    break;
                case ReservationInfo.STATUS_NOT_CONFIRMED:
                    textView.setText(getString(R.string.tv_not_verified));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.red_700));
                    break;
                default:
                    textView.setText(getString(R.string.tv_unknown_status));
                    break;
            }
        } else textView.setText(getString(R.string.tv_not_booked));

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(toolbar!=null) {
                ((MainActivity) getActivity()).setSupportActionBar(toolbar);
                ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
    }
}
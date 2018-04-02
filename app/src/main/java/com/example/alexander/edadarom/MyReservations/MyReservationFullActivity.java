package com.example.alexander.edadarom.MyReservations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.ReservationInfo;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.CreateDialog;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyReservationFullActivity extends AppCompatActivity {

    protected TextView tvBrn;
    protected CardView btnReservation;
    protected ImageView ivOpen;
    protected TextView tvProfileTitle;
    protected ImageView ivProfile;
    protected TextView tvTitle;
    protected TextView tvCoastPrice;
    protected TextView tvFinishCoast;
    protected TextView tvShipPrice;
    protected TextView tvFinishCoastPrice;
    protected TextView tvStatus;
    protected TextView tvReservation;
    protected ImageView ivAd;
    protected TextView tvAddressTitle;
    protected TextView tvAddressSubtitle;
    protected CardView cardViewAddress;
    private ConstraintLayout topView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private String adId;
    private UserAdsModel userAdsModel;

    //Toolbar back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_my_reservation_full);
        initView();
        adId = getIntent().getStringExtra("id");
        getDate();
    }

    private void initView() {
        tvBrn = (TextView) findViewById(R.id.tvBrn);
        ivOpen = (ImageView) findViewById(R.id.ivOpen);
        tvProfileTitle = (TextView) findViewById(R.id.tvProfileTitle);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivAd = (ImageView) findViewById(R.id.ivAd);
        tvCoastPrice = (TextView) findViewById(R.id.tvCoastPrice);
        tvFinishCoast = (TextView) findViewById(R.id.tvFinishCoast);
        tvShipPrice = (TextView) findViewById(R.id.tvShipPrice);
        tvFinishCoastPrice = (TextView) findViewById(R.id.tvFinishCoastPrice);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvReservation = (TextView) findViewById(R.id.tvReservation);
        ivAd = (ImageView) findViewById(R.id.ivAd);
        tvAddressTitle = (TextView) findViewById(R.id.tvAddressTitle);
        tvAddressSubtitle = (TextView) findViewById(R.id.tvAddressSubtitle);
        cardViewAddress = (CardView) findViewById(R.id.card_view3);

        //Кнопка подтвердить
        btnReservation = (CardView) findViewById(R.id.btnReservation);
        btnReservation.setVisibility(View.INVISIBLE);

        topView = findViewById(R.id.topView);
        topView.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDate();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Информация о бронировании");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnReservationListener();
    }

    private void getDate() {
        swipeRefreshLayout.setRefreshing(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(adId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                userAdsModel = task.getResult().toObject(UserAdsModel.class);
                                getUserDate(firebaseUser, userAdsModel);
                            } else {

                            }
                        }
                        });
        }
    }

    private void getUserDate(FirebaseUser firebaseUser, UserAdsModel userAdsModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(FirebaseConst.USERS).document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.INVISIBLE);
                        topView.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Users user = task.getResult().toObject(Users.class);
                            updateUI(user, userAdsModel);
                        } else {

                        }
                    }
                });
    }

    private void confirmAdReservation() {
        MaterialDialog dialog = CreateDialog.createPleaseWaitDialog(this);
        //Бронирование при помощи Batch
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null) {
            WriteBatch batch = db.batch();
            // get reference to Ads
            DocumentReference myAdsRef = db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(userAdsModel.getId());

            userAdsModel.getReservationInfo().setStatus(ReservationInfo.STATUS_CONFIRMED);
            batch.set(myAdsRef, userAdsModel);

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    getDate();
                }
            });
        }
    }



    private void setStatus(TextView textView, ReservationInfo info) {
        btnReservation.setVisibility(View.INVISIBLE);
        if(info!=null) {
            switch (info.getStatus()) {
                case ReservationInfo.STATUS_FREE:

                    break;
                case ReservationInfo.STATUS_WAIT_CONFIRM:
                    textView.setText("В ожидании подтверждения");
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_700));

                    btnReservation.setVisibility(View.VISIBLE);
                    tvBrn.setText("Подтвердить");
                    break;
                case ReservationInfo.STATUS_CONFIRMED:
                    textView.setText("Подтвержден");
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_700));
                    break;
                case ReservationInfo.STATUS_WAIT_RETURN:
                    textView.setText("В ожидании возврата");
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_700));
                    break;
                case ReservationInfo.STATUS_NOT_CONFIRMED:
                    textView.setText("Не подтвержден");
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_700));
                    break;
                default:
                    textView.setText("Статус неизвестен");
                    break;
            }
        } else textView.setText("Не забронирован!");

    }

    private void btnReservationListener() {
        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (userAdsModel.getReservationInfo().getStatus()) {
                        case ReservationInfo.STATUS_FREE:

                            break;
                        case ReservationInfo.STATUS_WAIT_CONFIRM:
                           confirmAdReservation();
                            break;
                    }
            }
        });
    }

    private void setDelivery(ReservationInfo info) {
        if(info!=null) {
            if (info.isDelivery()) {
                cardViewAddress.setVisibility(View.VISIBLE);
                tvAddressTitle.setText("Способ отправки: доставка");
                tvAddressSubtitle.setText(info.getAddress().getLocality());
            } else cardViewAddress.setVisibility(View.INVISIBLE);
        } else cardViewAddress.setVisibility(View.INVISIBLE);

    }

    private void updateUI(Users user, UserAdsModel u) {
        tvTitle.setText(u.getTitle());
        tvProfileTitle.setText(user.getFirstName());
        Picasso.with(this).load(user.getPhoto()).into(ivProfile);

        setDelivery(u.getReservationInfo());
        setStatus(tvStatus, u.getReservationInfo());


        if(userAdsModel.getReservationInfo()!=null) {
            SimpleDateFormat sf = new SimpleDateFormat("d MMMM HH:mm", new Locale("ru", "RU"));
            String date = sf.format(u.getReservationInfo().getReservationDate());
            String dateEnd = sf.format(u.getReservationInfo().getReservationDateEnd());
            tvReservation.setText("Начало брони " + date + "\nКонец брони " + dateEnd);
            Picasso.with(this).load(u.getPhotoUrl().get(0)).into(ivAd);
        }





    }
}

package com.nuttertools.FullInfo;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nuttertools.R;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.models.Users;
import com.nuttertools.utils.GlideApp;

import java.text.DecimalFormat;

public class FullInfoActivity extends AppCompatActivity implements FullInfoContract.View {

    private ConstraintLayout topView;
    private TextView tvToolbarTitle, tvToolbarSubtitle, tvPrice, tvDesc, tvRating, tvCount;
    private TextView tvSellerTitle, tvSellerSubtitle, tvSellerRating;
    private TextView tvReserv;
    private ImageView imgSeller;
    private ImageView imgToolbar;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private AppbarImagesAdapter appbarImagesAdapter;
    public FullInfoContract.Presenter presenter;
    private CardView btnReservation;

    ReservationOptionFragmentListener fgReservListener;

    public interface ReservationOptionFragmentListener {
        void addDate(UserAdsModel userAdModel, Users users);
    }

    public synchronized void registerFgReservListener(ReservationOptionFragmentListener listener) {
        fgReservListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        topView = findViewById(R.id.topView);
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setVisibility(View.INVISIBLE);
        nestedScrollView = findViewById(R.id.content);
        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewpager);

        btnReservation = findViewById(R.id.btnReservation);
        btnReservation.setVisibility(View.INVISIBLE);
        imgToolbar = findViewById(R.id.expandedImage);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarSubtitle = findViewById(R.id.tvToolbarSubtitle);

        tvReserv = findViewById(R.id.tvReserv);
        tvPrice = findViewById(R.id.tvPrice);
        tvDesc = findViewById(R.id.tvDesc);
        tvRating = findViewById(R.id.tvRating);
        tvCount = findViewById(R.id.tvCount);

        tvSellerTitle = findViewById(R.id.tvSellerTitle);
        tvSellerSubtitle = findViewById(R.id.tvSellerDesr);
        tvSellerRating = findViewById(R.id.tvSellerRating);
        imgSeller = findViewById(R.id.imgSeller);

        presenter = new FullInfoPresenter(this);

        //Получаем через Intent id для объявления
        presenter.getAd(getIntent().getStringExtra("key"));

        setStatusBarTranslucent(true);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        appBarLayout.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
        btnReservation.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addDate(UserAdsModel userAdsModel) {
        tvToolbarTitle.setText(userAdsModel.getTitle());
        tvToolbarSubtitle.setText(userAdsModel.getDescription());

        DecimalFormat dfnd = new DecimalFormat("#,###.00");
        tvPrice.setText((dfnd.format(userAdsModel.getPrice())).concat(" \u20BD/").concat(userAdsModel.getPriceType()));


        tvDesc.setText(userAdsModel.getDescription());

        initViewPager(userAdsModel);
    }

    private void initViewPager(UserAdsModel userAdsModel) {
        appbarImagesAdapter = new AppbarImagesAdapter(getApplicationContext(), userAdsModel.getPhotoUrl());
        viewPager.setAdapter(appbarImagesAdapter);
        tvCount.setText(1 + "/" + userAdsModel.getPhotoUrl().size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCount.setText(position + 1 + "/" + userAdsModel.getPhotoUrl().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showUserInfo(Users users) {
        if(users!=null) {
            tvSellerTitle.setText(users.getFirstName());
            tvSellerSubtitle.setText(users.getSecondName());
            tvSellerRating.setText(String.valueOf(users.getRating()));
            if(users.getPhoto()!=null)
                GlideApp.with(getApplicationContext())
                        .load(users.getPhoto())
                        .fitCenter()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgSeller);
            //TODO .fit >>> .fitCenter()
        }
    }

    @Override
    public void showReservationFragment() {
        FragmentReservationOptions fragment = new FragmentReservationOptions();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.topView, fragment)
                .addToBackStack("reservation")
                .commit();

        setTheme(R.style.AppTheme);
        setStatusBarTranslucent(false);

    }


    @Override
    public void hideReservationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() != 0) {
            manager.popBackStack();
        }
        setStatusBarTranslucent(true);
        setTheme(R.style.AppTheme_ActionBar_Transparent);
    }

    @Override
    public void showDateInFragment(UserAdsModel userAdsModel, Users users) {
        if(fgReservListener!=null) {
            fgReservListener.addDate(userAdsModel, users);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reservationBtnEnable(boolean isEnable) {
        btnReservation.setEnabled(isEnable);
        if(!isEnable) tvReserv.setText("Товар забронирован");
        else tvReserv.setText("Забронировать");
    }

    public void reservationClick(View view) {
        presenter.showReservationFragment();
        //presenter.reservationAd();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() != 0) {
            hideReservationFragment();
        } else super.onBackPressed();
    }
}

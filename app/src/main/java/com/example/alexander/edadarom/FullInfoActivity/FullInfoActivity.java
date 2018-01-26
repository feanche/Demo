package com.example.alexander.edadarom.FullInfoActivity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.squareup.picasso.Picasso;

public class FullInfoActivity extends AppCompatActivity implements FullInfoContract.View {

    private TextView tvToolbarTitle, tvToolbarSubtitle, tvPrice, tvDesc, tvRating, tvCount;
    private TextView tvSellerTitle, tvSellerSubtitle, tvSellerRating;
    private ImageView imgSeller;
    private ImageView imgToolbar;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private AppbarImagesAdapter appbarImagesAdapter;
    private FullInfoContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setVisibility(View.INVISIBLE);
        nestedScrollView = findViewById(R.id.content);
        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewpager);

        imgToolbar = findViewById(R.id.expandedImage);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarSubtitle = findViewById(R.id.tvToolbarSubtitle);

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
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addDate(UserAdsModel userAdsModel) {
        tvToolbarTitle.setText(userAdsModel.getTitle());
        tvToolbarSubtitle.setText(userAdsModel.getDescription());

        tvPrice.setText(String.valueOf(userAdsModel.getPrice()));
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
            if(users.getPhoto()!=null) Picasso.with(getApplicationContext()).load(users.getPhoto()).fit().into(imgSeller);
        }
    }

    public void reservationClick(View view) {
        presenter.rezervation();
    }
}

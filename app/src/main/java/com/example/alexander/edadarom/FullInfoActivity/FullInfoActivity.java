package com.example.alexander.edadarom.FullInfoActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.squareup.picasso.Picasso;

public class FullInfoActivity extends AppCompatActivity implements FullInfoContract.View {

    private TextView tvToolbarTitle, tvToolbarSubtitle, tvPrice, tvDesc, tvRating;
    private TextView tvSellerTitle, tvSellerSubtitle, tvSellerRating;
    private ImageView imgSeller;
    private ImageView imgToolbar;

    private FullInfoContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        imgToolbar = findViewById(R.id.expandedImage);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarSubtitle = findViewById(R.id.tvToolbarSubtitle);

        tvPrice = findViewById(R.id.tvPrice);
        tvDesc = findViewById(R.id.tvDesc);
        tvRating = findViewById(R.id.tvRating);

        presenter = new FullInfoPresenter(this);
        presenter.getAd(getIntent().getStringExtra("key"));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void addDate(UserAdsModel userAdsModel) {
        tvToolbarTitle.setText(userAdsModel.getTitle());
        tvToolbarSubtitle.setText(userAdsModel.getDescription());

        tvPrice.setText(String.valueOf(userAdsModel.getPrice()));
        tvDesc.setText(userAdsModel.getDescription());

        Picasso.with(getApplicationContext()).load(userAdsModel.getPhotoUrl()).fit().into(imgToolbar);
    }
}

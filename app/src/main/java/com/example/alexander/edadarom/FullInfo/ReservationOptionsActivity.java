package com.example.alexander.edadarom.FullInfo;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReservationOptionsActivity extends AppCompatActivity {


    protected Toolbar toolbar;
    protected ImageView ivAd;
    protected TextView tvFinishCoast;
    protected TextView tvShipPrice;
    protected TextView tvFinishCoastPrice;
    protected CardView cardView1;
    protected CardView cardView2;
    protected TextInputLayout edDate;
    protected TextInputLayout edTime;
    protected RadioButton radioButton;
    protected RadioButton radioButton2;
    protected TextView tvTitle;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_reservation_options);
        initView();
        getIntent().getStringExtra("key");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivAd = (ImageView) findViewById(R.id.ivAd);
        tvFinishCoast = (TextView) findViewById(R.id.tvFinishCoast);
        tvShipPrice = (TextView) findViewById(R.id.tvShipPrice);
        tvFinishCoastPrice = (TextView) findViewById(R.id.tvFinishCoastPrice);
        cardView1 = (CardView) findViewById(R.id.card_view1);
        cardView2 = (CardView) findViewById(R.id.card_view2);
        edDate = (TextInputLayout) findViewById(R.id.edDate);
        edTime = (TextInputLayout) findViewById(R.id.edTime);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
    }

}

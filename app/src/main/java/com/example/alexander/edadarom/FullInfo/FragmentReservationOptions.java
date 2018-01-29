package com.example.alexander.edadarom.FullInfo;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentReservationOptions extends Fragment implements FullInfoActivity.ReservationOptionFragmentListener {

    private View view;
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
    private FullInfoContract.Presenter presenter;
    private UserAdsModel userAdsModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_reservation_options, container, false);
        presenter = ((FullInfoActivity)getActivity()).presenter;
        presenter.showDateInFragment();
        initView();
        return view;
    }

    private void initView() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ivAd = (ImageView) view.findViewById(R.id.ivAd);
        tvFinishCoast = (TextView) view.findViewById(R.id.tvFinishCoast);
        tvShipPrice = (TextView) view.findViewById(R.id.tvShipPrice);
        tvFinishCoastPrice = (TextView) view.findViewById(R.id.tvFinishCoastPrice);
        cardView1 = (CardView) view.findViewById(R.id.card_view1);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        edDate = (TextInputLayout) view.findViewById(R.id.edDate);
        edTime = (TextInputLayout) view.findViewById(R.id.edTime);
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) view.findViewById(R.id.radioButton2);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
    }

    @Override
    public void addDate(UserAdsModel userAdModel, Users users) {
        tvTitle.setText(userAdModel.getDescription());
    }
}

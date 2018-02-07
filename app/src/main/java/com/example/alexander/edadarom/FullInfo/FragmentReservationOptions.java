package com.example.alexander.edadarom.FullInfo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.ReservationInfo;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentReservationOptions extends Fragment implements FullInfoActivity.ReservationOptionFragmentListener {

    protected TextInputEditText tiDate;
    protected TextInputEditText tiTime;
    protected TextInputEditText tiDateEnd;
    protected TextInputEditText tiTimeEnd;
    protected CardView btnReservation;
    protected ProgressBar progressBar;
    protected View rootView;
    protected TextView tvCoastPrice;
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
    Calendar dateAndTime = Calendar.getInstance();
    Calendar dateAndTimeEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d;
    TimePickerDialog.OnTimeSetListener t;
    DatePickerDialog.OnDateSetListener d2;
    TimePickerDialog.OnTimeSetListener t2;
    FullInfoActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_reservation_options, container, false);
        activity = ((FullInfoActivity) getActivity());
        activity.registerFgReservListener(this);
        initView();
        initToolbar();
        initDateTimePickers();
        editTextClickListener();
        presenter = ((FullInfoActivity) getActivity()).presenter;
        presenter.showDateInFragment();



        return view;
    }

    private void initToolbar() {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Детали бронирования");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        tiDate = (TextInputEditText) view.findViewById(R.id.tiDate);
        tiTime = (TextInputEditText) view.findViewById(R.id.tiTime);
        tiDateEnd = (TextInputEditText) view.findViewById(R.id.tiDateEnd);
        tiTimeEnd = (TextInputEditText) view.findViewById(R.id.tiTimeEnd);
        btnReservation = (CardView) view.findViewById(R.id.btnReservation);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvCoastPrice = (TextView) view.findViewById(R.id.tvCoastPrice);

        btnReservationClick();

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioButton2.setChecked(!isChecked);
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioButton.setChecked(!isChecked);
            }
        });
    }

    private void btnReservationClick() {
        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                ReservationInfo info = new ReservationInfo(new Date(), new Date(dateAndTime.getTimeInMillis()), new Date(dateAndTimeEnd.getTimeInMillis()), radioButton2.isChecked(), "");
                presenter.reservationAd(info);
                //presenter.test();
            }
        });
    }

    private void editTextClickListener() {
        tiDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        tiTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true)
                        .show();
            }
        });

        tiDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), d2,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        tiTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), t2,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true)
                        .show();
            }
        });
    }

    private void initDateTimePickers() {
        // установка обработчика выбора даты
        d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dateAndTime.set(Calendar.YEAR, i);
                dateAndTime.set(Calendar.MONTH, i1);
                dateAndTime.set(Calendar.DAY_OF_MONTH, i2);
                Date date = new Date(dateAndTime.getTimeInMillis());
                //mapPresenter.loadTrack(date);
                //mapPresenter.getTrack("", dateAndTime.getTimeInMillis(), new Date().getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String formattedDate = simpleDateFormat.format(date);
                tiDate.setText(formattedDate);
            }
        };

        t = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTime.set(Calendar.MONTH, hourOfDay);
                dateAndTime.set(Calendar.DAY_OF_MONTH, minute);
                Date date = new Date(dateAndTime.getTimeInMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String formattedTime = simpleDateFormat.format(date);
                tiTime.setText(formattedTime);
            }
        };

        // установка обработчика выбора даты
        d2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dateAndTimeEnd.set(Calendar.YEAR, i);
                dateAndTimeEnd.set(Calendar.MONTH, i1);
                dateAndTimeEnd.set(Calendar.DAY_OF_MONTH, i2);
                Date date = new Date(dateAndTimeEnd.getTimeInMillis());
                //mapPresenter.loadTrack(date);
                //mapPresenter.getTrack("", dateAndTime.getTimeInMillis(), new Date().getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String formattedDate = simpleDateFormat.format(date);
                tiDateEnd.setText(formattedDate);
            }
        };

        t2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTimeEnd.set(Calendar.MONTH, hourOfDay);
                dateAndTimeEnd.set(Calendar.DAY_OF_MONTH, minute);
                Date date = new Date(dateAndTimeEnd.getTimeInMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String formattedTime = simpleDateFormat.format(date);
                tiTimeEnd.setText(formattedTime);
            }
        };
    }

    @Override
    public void addDate(UserAdsModel userAdModel, Users users) {
        tvTitle.setText(userAdModel.getTitle());
//        tvCoastPrice.setText(userAdModel.getPrice());
        Picasso.with(getContext()).load(userAdModel.getPhotoUrl().get(0)).into(ivAd);
    }
}

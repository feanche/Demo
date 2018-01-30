package com.example.alexander.edadarom.FullInfo;

import android.util.Log;

import com.example.alexander.edadarom.models.ReservationQuery;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class FullInfoPresenter implements FullInfoContract.Presenter {

    public static String TAG = "BrowsePresenter";

     private FullInfoContract.View view;
     public UserAdsModel userAdsModel;
     public Users user;

    public FullInfoPresenter(FullInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void getAd(String key) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads").document(key)
                .get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {

                        if (snapshotTask.getResult() == null) {
                            //view.showToast("Нет данных");
                            view.hideLoading();
                            return;
                        }

                        if(snapshotTask.getResult().exists()) {
                            Log.d(TAG, key + " => " + snapshotTask.getResult());
                            userAdsModel = snapshotTask.getResult().toObject(UserAdsModel.class);
                            userAdsModel.setId(snapshotTask.getResult().getId());
                            if (userAdsModel.isReserved()) {
                                view.reservationBtnEnable(false);
                            } else view.reservationBtnEnable(true);

                            //Информация о пользователи или магазине
                            getUserInfo(userAdsModel.getUserId());
                            view.addDate(userAdsModel);
                        } else {
                            view.showToast("Документа не существует!");
                        }

                    }
                });
    }

    @Override
    public void reservationAd(String reservationDate, String reservationTime, Boolean isDelivery, String deliveryAddress) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String token = FirebaseInstanceId.getInstance().getToken();
        ReservationQuery reservationQuery = new ReservationQuery(firebaseUser.getUid(), userAdsModel.getUserId(), userAdsModel.getId(), reservationDate, reservationTime ,isDelivery, deliveryAddress);
        db.collection(FirebaseConst.RESERVATION_QUERY)
                .add(reservationQuery)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       Log.d(TAG, "Document snapshot added");
                       view.showToast("Товар забронирован!");
                       view.hideReservationFragment();
                   }
               });
    }

    @Override
    public void showReservationFragment() {
        view.showReservationFragment();
    }

    @Override
    public void hideReservationFragment() {
        view.hideReservationFragment();
    }

    @Override
    public void showDateInFragment() {
        view.showDateInFragment(userAdsModel, user);
    }

    private void getUserInfo(String id) {
        if (id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(id)
                    .get()
                    .addOnCompleteListener(snapshotTask -> {
                        if (snapshotTask.isSuccessful()) {

                            if (snapshotTask.getResult() == null) {
                                //view.showToast("Нет данных");
                                view.hideLoading();
                                return;
                            }

                            //Если пользователь есть в бд
                            if (snapshotTask.getResult().exists()) {
                                //Log.d(TAG, id + " => " + snapshotTask.getResult());
                                user = snapshotTask.getResult().toObject(Users.class);
                                view.showUserInfo(user);
                                view.hideLoading();
                            }
                        } else view.hideLoading();
                    });
        } {
            view.hideLoading();
        }
    }
    }

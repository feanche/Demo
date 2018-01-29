package com.example.alexander.edadarom.FullInfo;

import android.util.Log;

import com.example.alexander.edadarom.models.ReservationQuery;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
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

                        Log.d(TAG, key + " => " + snapshotTask.getResult());
                        userAdsModel = snapshotTask.getResult().toObject(UserAdsModel.class);
                        userAdsModel.setId(snapshotTask.getResult().getId());
                        //view.hideLoading();

                        //Информация о пользователи или магазине
                        getUserInfo(userAdsModel.getUserId());
                        view.addDate(userAdsModel);

                    }
                });
    }

    @Override
    public void reservationAd() {
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String token = FirebaseInstanceId.getInstance().getToken();
        ReservationQuery reservationQuery = new ReservationQuery(firebaseUser.getUid(), userAdsModel.getUserId(), userAdsModel.getId(), token);
        db.collection("reservationQuery")
                .add(reservationQuery)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       Log.d(TAG, "Document snapshot added");
                   }
               });*/
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

package com.example.alexander.edadarom.FullInfo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alexander.edadarom.models.ReservationInfo;
import com.example.alexander.edadarom.models.ReservationQuery;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Date;

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
    public void reservationAd(ReservationInfo reservationInfo) {
        reservationWithBatch(reservationInfo);
        view.hideReservationFragment();

    }

    private void reservationWithBatch(ReservationInfo reservationInfo) {
        //Бронирование при помощи Batch
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null) {
            // Get a new write batch
            WriteBatch batch = db.batch();
            reservationInfo.setReservedUser(firebaseUser.getUid());
            // get reference to Ads
            DocumentReference adsRef = db.collection(FirebaseConst.ADS).document(userAdsModel.getId());
            DocumentReference myReservationRef = db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(userAdsModel.getId());

            //Добавляем reservationInfo к модели пользователя
            userAdsModel.setReservationInfo(reservationInfo);
            //добавляем информацию о статусе бронирование
            userAdsModel.setReserved(true);

            //update Ads
            batch.set(adsRef, userAdsModel);
            batch.set(myReservationRef, userAdsModel);

            // Commit the batch
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "batch success");
                    view.showToast("Товар забронирован!");
                }
            });
        }
    }

    public void testBatch() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null) {
            // Get a new write batch
            WriteBatch batch = db.batch();

            // Set the value of reservationsQuery
            DocumentReference reservationRef = db.collection("Test").document("Test").collection("Test").document("Test");
            batch.set(reservationRef, new UserAdsModel());

            // Commit the batch
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "batch success");
                    view.showToast("Товар забронирован!");
                }
            });
        }
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

    @Override
    public void test() {
        testBatch();
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

package com.example.alexander.edadarom.FullInfoActivity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.alexander.edadarom.fragments.Browse.BrowseFragmentContract;
import com.example.alexander.edadarom.fragments.FragmentPersonal;
import com.example.alexander.edadarom.models.ReservationQuery;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class FullInfoPresenter implements FullInfoContract.Presenter {

    public static String TAG = "BrowsePresenter";

     private FullInfoContract.View view;
     private UserAdsModel userAdsModel;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
               });
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
                                Users user = snapshotTask.getResult().toObject(Users.class);
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

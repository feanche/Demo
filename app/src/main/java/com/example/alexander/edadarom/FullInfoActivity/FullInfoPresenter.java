package com.example.alexander.edadarom.FullInfoActivity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alexander.edadarom.fragments.Browse.BrowseFragmentContract;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class FullInfoPresenter implements FullInfoContract.Presenter {

    public static String TAG = "BrowsePresenter";

     private FullInfoContract.View view;


    public FullInfoPresenter(FullInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void getAd(String key) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads").document(key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> snapshotTask) {
                        if (snapshotTask.isSuccessful()) {

                            if (snapshotTask.getResult() == null) {
                                //view.showToast("Нет данных");
                                view.hideLoading();
                                return;
                            }

                            Log.d(TAG, key + " => " + snapshotTask.getResult());
                            UserAdsModel userAdsModel = snapshotTask.getResult().toObject(UserAdsModel.class);
                            view.hideLoading();
                            view.addDate(userAdsModel);

                        }
                    }
                });
    }
    }

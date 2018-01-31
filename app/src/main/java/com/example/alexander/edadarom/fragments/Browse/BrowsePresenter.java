package com.example.alexander.edadarom.fragments.Browse;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alexander.edadarom.fragments.Browse.Models.Ad;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class BrowsePresenter implements BrowseFragmentContract.Presenter {

    public static String TAG = "BrowsePresenter";

     private BrowseFragmentContract.View view;
     private ArrayList<UserAdsModel> arAds = new ArrayList<>();

    public BrowsePresenter(BrowseFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void getAds() {
        arAds.clear();
        view.showLoading();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads")
                .orderBy("publicTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().size() == 0) {
                                //view.showToast("Нет данных");
                                view.hideLoading();
                                return;
                            }


                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserAdsModel userAdsModel = document.toObject(UserAdsModel.class);
                                userAdsModel.setId(document.getId());
                                arAds.add(userAdsModel);
                            }

                            view.hideLoading();
                            view.addDate(arAds);
                        }
                    }
                });
    }
    }

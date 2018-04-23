package com.nuttertools.fragments.Browse;

import android.util.Log;

import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.FirebaseConst;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

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
    public void getAds(int id) {
        arAds.clear();
        view.showLoading();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.ADS)
                .whereEqualTo("categoryId", id)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
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
                    } else view.hideLoading();
                });
    }

    @Override
    public void getLastAddedItems(int limit) {
        arAds.clear();
        view.showLoading();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.ADS)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
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
                    } else view.hideLoading();
                });
    }
}

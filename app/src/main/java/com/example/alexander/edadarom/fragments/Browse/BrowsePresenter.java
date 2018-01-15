package com.example.alexander.edadarom.fragments.Browse;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alexander.edadarom.fragments.Browse.Models.Ad;
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

public class BrowsePresenter implements BrowseFragmentContract.Presenter {

    public static String TAG = "BrowsePresenter";

     private BrowseFragmentContract.View view;
     private ArrayList<Ad> arAds;

    public BrowsePresenter(BrowseFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void getAds() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads")
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
                                Ad ad = document.toObject(Ad.class);
                                arAds.add(ad);
                            }
                            view.hideLoading();
                            Collections.reverse(arAds);
                            view.addDate(arAds);
                        }
                    }
                });
    }
    }

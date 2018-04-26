package com.nuttertools.fragments.Category;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.FirebaseConst;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

/**
 * Created by GabdrakhmanovII on 06.02.2018.
 */

public class CategoryPresenter implements CategoryMvp.Presenter {

    private static final String TAG = "CategoryPresenter";
    private CategoryMvp.View view;
    private ArrayList<Category> ar;

    @Override
    public void attachView(CategoryMvp.View view) {
        this.view = view;
        ar = new ArrayList<>();
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getDate() {
        ar.clear();
        view.showLoading();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.CATEGORIES)
                .orderBy(FirebaseConst.CATEGORY_POSITION, Query.Direction.DESCENDING)
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
                                Category category = document.toObject(Category.class);
                                ar.add(category);
                            }

                            view.hideLoading();
                            view.updateRecyclerView(ar);
                        }
                    }
                });
    }

    @Override
    public void recyclerItemClick(int position) {
        view.openActivity(ar.get(position).getCategoryId(), ar.get(position).getName());
    }
}
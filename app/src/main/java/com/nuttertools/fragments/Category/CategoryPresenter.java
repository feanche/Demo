package com.nuttertools.fragments.Category;

import com.nuttertools.utils.FirebaseConst;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * Created by GabdrakhmanovII on 06.02.2018.
 */

public class CategoryPresenter implements CategoryMvp.Presenter {

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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
                            view.hideLoading();
                            return;
                        }
                        for (DocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            ar.add(category);
                        }
                        view.hideLoading();
                        view.updateRecyclerView(ar);
                    }
                })
                .addOnSuccessListener(queryDocumentSnapshots -> view.emptyCheck());
    }

    @Override
    public void recyclerItemClick(int position) {
        view.openActivity(ar.get(position).getCategoryId(), ar.get(position).getName());
    }
}
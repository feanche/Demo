package com.example.alexander.edadarom.fragments.Category;

import com.example.alexander.edadarom.utils.BaseView;

import java.util.ArrayList;

/**
 * Created by GabdrakhmanovII on 06.02.2018.
 */

public interface CategoryMvp {

    interface View extends BaseView {
        void updateRecyclerView(ArrayList<Category> categories);
        void openActivity(int id, String name);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void getDate();
        void recyclerItemClick(int position);
    }
}

package com.nuttertools.fragments.Browse;

import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.BaseView;

import java.util.ArrayList;

/**
 * Created by lAntimat on 15.01.2018.
 */

public interface BrowseFragmentContract {

    interface View extends BaseView{
        void addDate(ArrayList<UserAdsModel> arAds);
    }

    interface Presenter {
        void getAds(int id);
        void getLastAddedItems(int count);
    }
}
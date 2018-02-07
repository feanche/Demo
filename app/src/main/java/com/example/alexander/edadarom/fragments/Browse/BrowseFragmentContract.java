package com.example.alexander.edadarom.fragments.Browse;

import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.BaseView;

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
    }
}

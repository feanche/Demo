package com.example.alexander.edadarom.fragments.Browse;

import com.example.alexander.edadarom.fragments.Browse.Models.Ad;
import com.example.alexander.edadarom.utils.BaseView;

import java.util.ArrayList;

/**
 * Created by lAntimat on 15.01.2018.
 */

public interface BrowseFragmentContract {

    interface View extends BaseView{
        void addDate(ArrayList<Ad> arAds);
    }

    interface Presenter {
        void getAds();
    }
}

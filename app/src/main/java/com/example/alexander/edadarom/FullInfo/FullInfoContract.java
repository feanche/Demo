package com.example.alexander.edadarom.FullInfo;

import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.BaseView;

/**
 * Created by lAntimat on 15.01.2018.
 */

public interface FullInfoContract {

    interface View extends BaseView{
        void addDate(UserAdsModel userAdsModel);
        void showUserInfo(Users users);
        void showReservationFragment();
        void hideReservationFragment();
        void showDateInFragment(UserAdsModel userAdsModel, Users users);
    }

    interface Presenter {
        void getAd(String key);
        void reservationAd();
        void showReservationFragment();
        void hideReservationFragment();
        void showDateInFragment();
    }
}
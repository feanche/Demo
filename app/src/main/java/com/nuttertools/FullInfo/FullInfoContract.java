package com.nuttertools.FullInfo;

import com.nuttertools.models.ReservationInfo;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.models.Users;
import com.nuttertools.utils.BaseView;

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
        void showToast(String message);
        void reservationBtnEnable(boolean isEnable);
    }

    interface Presenter {
        void getAd(String key);
        void reservationAd(ReservationInfo reservationInfo);
        void showReservationFragment();
        void hideReservationFragment();
        void showDateInFragment();
        void test();
    }
}

package com.example.alexander.edadarom.FullInfoActivity;

import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.BaseView;

import java.util.ArrayList;

/**
 * Created by lAntimat on 15.01.2018.
 */

public interface FullInfoContract {

    interface View extends BaseView{
        void addDate(UserAdsModel userAdsModel);
        void showUserInfo(Users users);
    }

    interface Presenter {
        void getAd(String key);
        void rezervation();
    }
}

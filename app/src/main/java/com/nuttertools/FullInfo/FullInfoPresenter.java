package com.nuttertools.FullInfo;

import com.nuttertools.R;
import com.nuttertools.models.ReservationInfo;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.models.Users;
import com.nuttertools.utils.FirebaseConst;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class FullInfoPresenter implements FullInfoContract.Presenter {

    private FullInfoContract.View view;
    public UserAdsModel userAdsModel;
    public Users user;

    public FullInfoPresenter(FullInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void getAd(String key) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.ADS).document(key)
                .get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        if (snapshotTask.getResult() == null) {
                            view.hideLoading();
                            return;
                        }
                        if(snapshotTask.getResult().exists()) {
                            userAdsModel = snapshotTask.getResult().toObject(UserAdsModel.class);
                            userAdsModel.setId(snapshotTask.getResult().getId());
                            if (userAdsModel.isReserved()) {
                                view.reservationBtnEnable(false);
                            } else view.reservationBtnEnable(true);
                            getUserInfo(userAdsModel.getUserId());
                            view.addDate(userAdsModel);
                        } else {
                            view.showToast(String.valueOf(R.string.toast_document_not_exist));
                        }
                    }
                });
    }

    @Override
    public void reservationAd(ReservationInfo reservationInfo) {
        reservationWithBatch(reservationInfo);
        view.hideReservationFragment();
    }

    private void reservationWithBatch(ReservationInfo reservationInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            WriteBatch batch = db.batch();
            reservationInfo.setReservedUser(firebaseUser.getUid());
            DocumentReference adsRef = db.collection(FirebaseConst.ADS).document(userAdsModel.getId());
            DocumentReference myReservationRef = db
                    .collection(FirebaseConst.USERS)
                    .document(firebaseUser.getUid())
                    .collection(FirebaseConst.MY_RESERVATIONS)
                    .document(userAdsModel.getId());
            userAdsModel.setReservationInfo(reservationInfo);
            userAdsModel.setReserved(true);
            batch.set(adsRef, userAdsModel);
            batch.set(myReservationRef, userAdsModel);
            batch.commit().addOnCompleteListener(task -> view.showToast(String.valueOf(R.string.toast_item_reserved)));
        }
    }

    private void testBatch() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            WriteBatch batch = db.batch();
            DocumentReference reservationRef = db
                    .collection("Test")
                    .document("Test")
                    .collection("Test")
                    .document("Test");
            batch.set(reservationRef, new UserAdsModel());
            batch.commit().addOnCompleteListener(task -> view.showToast(String.valueOf(R.string.toast_item_reserved)));
        }
    }

    @Override
    public void showReservationFragment() {
        view.showReservationFragment();
    }

    @Override
    public void hideReservationFragment() {
        view.hideReservationFragment();
    }

    @Override
    public void showDateInFragment() {
        view.showDateInFragment(userAdsModel, user);
    }

    @Override
    public void test() {
        testBatch();
    }

    private void getUserInfo(String id) {
        if (id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseConst.USERS).document(id)
                    .get()
                    .addOnCompleteListener(snapshotTask -> {
                        if (snapshotTask.isSuccessful()) {
                            if (snapshotTask.getResult() == null) {
                                view.hideLoading();
                                return;
                            }
                            if (snapshotTask.getResult().exists()) {
                                user = snapshotTask.getResult().toObject(Users.class);
                                view.showUserInfo(user);
                                view.hideLoading();
                            }
                        }
                        else view.hideLoading();
                    });
        }
        view.hideLoading();
    }

}
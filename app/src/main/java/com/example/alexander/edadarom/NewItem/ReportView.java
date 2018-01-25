package com.example.alexander.edadarom.NewItem;

import com.example.alexander.edadarom.utils.LoadingView;

/**
 * Created by Alexander on 25.01.2018.
 */

public interface ReportView extends LoadingView {

    void onUploadImageSuccess(int position);

    void onUploadImageProgress(int position, int progress);
}

package com.nuttertools.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuttertools.R;

/**
 * Created by GabdrakhmanovII on 09.02.2018.
 */

public class CreateDialog {
    public static MaterialDialog createPleaseWaitDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content(R.string.dialog_please_wait)
                .progress(true, 0)
                .show();
    }
}

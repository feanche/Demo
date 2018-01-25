package com.example.alexander.edadarom.NewItem;

import android.net.Uri;

/**
 * Created by Alexander on 25.01.2018.
 */

public class UploadImage {
    private Uri uri;
    private boolean isLoaded;
    private int progress = 0;

    public UploadImage(Uri uri, boolean isLoaded) {
        this.uri = uri;
        this.isLoaded = isLoaded;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

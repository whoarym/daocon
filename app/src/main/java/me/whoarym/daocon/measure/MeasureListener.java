package me.whoarym.daocon.measure;

import android.support.annotation.NonNull;

public interface MeasureListener {
    void reportProgress(int progress);
    void reportResult(@NonNull String report);
}

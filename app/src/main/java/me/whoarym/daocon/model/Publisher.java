package me.whoarym.daocon.model;

import android.support.annotation.NonNull;

public interface Publisher {
    int getId();
    void setId(int id);
    String getName();
    void setName(@NonNull String name);
}

package me.whoarym.daocon.model;

import android.support.annotation.NonNull;

public interface Owner {
    int getId();
    void setId(int id);
    String getFirstName();
    void setFirstName(@NonNull String firstName);
    String getLastName();
    void setLastName(@NonNull String lastName);
}

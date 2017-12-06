package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Owner;

@Entity(tableName = "tbl_owner")
public class RoomOwner implements Owner {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "first_name")
    private String mFirstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    private String mLastName;

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setId(int id) {
        mId = id;
    }

    @Override
    public String getFirstName() {
        return mFirstName;
    }

    @Override
    public void setFirstName(@NonNull String firstName) {
        mFirstName = firstName;
    }

    @Override
    public String getLastName() {
        return mLastName;
    }

    @Override
    public void setLastName(@NonNull String lastName) {
        mLastName = lastName;
    }
}

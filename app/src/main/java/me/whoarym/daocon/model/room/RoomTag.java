package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Tag;

@Entity(tableName = "tbl_tag", indices = {@Index(value = "name", unique = true)})
public class RoomTag implements Tag {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setId(int id) {
        mId = id;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(@NonNull String name) {
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomTag roomTag = (RoomTag) o;

        if (mId != roomTag.mId) return false;
        return mName.equals(roomTag.mName);
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mName.hashCode();
        return result;
    }
}

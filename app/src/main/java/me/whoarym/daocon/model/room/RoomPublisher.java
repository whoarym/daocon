package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Publisher;

@Entity(tableName = "tbl_publisher", indices = {@Index("name")})
public class RoomPublisher implements Publisher {

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
}

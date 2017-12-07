package me.whoarym.daocon.model.room.optimized;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.room.RoomTag;

class RoomBookTagTuple {
    @ColumnInfo(name = "book_id")
    int mBookId;

    @ColumnInfo(name = "tag_id")
    int mTagId;

    @ColumnInfo(name = "tag_name")
    String mTagName;

    @NonNull
    RoomTag getTag() {
        RoomTag tag = new RoomTag();
        tag.setId(mTagId);
        tag.setName(mTagName);
        return tag;
    }
}

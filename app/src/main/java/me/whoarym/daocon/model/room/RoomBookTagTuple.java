package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

class RoomBookTagTuple {
    @ColumnInfo(name = "book_id")
    int mBookId;

    @ColumnInfo(name = "tag_id")
    private int mTagId;

    @ColumnInfo(name = "tag_name")
    private String mTagName;

    public int getBookId() {
        return mBookId;
    }

    public void setBookId(int bookId) {
        this.mBookId = bookId;
    }

    public int getTagId() {
        return mTagId;
    }

    public void setTagId(int tagId) {
        this.mTagId = tagId;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String tagName) {
        mTagName = tagName;
    }

    @NonNull
    RoomTag getTag() {
        RoomTag tag = new RoomTag();
        tag.setId(mTagId);
        tag.setName(mTagName);
        return tag;
    }
}

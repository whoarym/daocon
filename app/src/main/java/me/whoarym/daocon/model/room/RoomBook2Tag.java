package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Tag;

@Entity(
    tableName = "tbl_book2tag",
    indices = {@Index("book_id"), @Index("tag_id")},
    foreignKeys = {
        @ForeignKey(entity = RoomTag.class, parentColumns = "_id", childColumns = "tag_id"),
        @ForeignKey(entity = RoomBook.class, parentColumns = "_id", childColumns = "book_id")
    },
    primaryKeys = {"book_id", "tag_id"}
)
public class RoomBook2Tag {

    public RoomBook2Tag() {
    }

    public RoomBook2Tag(@NonNull Book book, @NonNull Tag tag) {
        mBookId = book.getId();
        mTagId = tag.getId();
    }

    @NonNull
    @ColumnInfo(name = "book_id")
    private int mBookId;

    @NonNull
    @ColumnInfo(name = "tag_id")
    private int mTagId;

    public int getBookId() {
        return mBookId;
    }

    public void setBookId(int id) {
        mBookId = id;
    }

    public int getTagId() {
        return mTagId;
    }

    public void setTagId(int id) {
        mTagId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomBook2Tag that = (RoomBook2Tag) o;

        if (mBookId != that.mBookId) return false;
        return mTagId == that.mTagId;
    }

    @Override
    public int hashCode() {
        int result = mBookId;
        result = 31 * result + mTagId;
        return result;
    }
}

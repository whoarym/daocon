package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;

@Entity(
    tableName = "tbl_book",
    indices = {
        @Index("name"),
        @Index("year"),
        @Index("author_id"),
        @Index("publisher_id"),
        @Index("owner_id")
    },
    foreignKeys = {
        @ForeignKey(entity = RoomAuthor.class, parentColumns = "_id", childColumns = "author_id"),
        @ForeignKey(entity = RoomPublisher.class, parentColumns = "_id", childColumns = "publisher_id"),
        @ForeignKey(entity = RoomOwner.class, parentColumns = "_id", childColumns = "owner_id")
    }
)
public class RoomBook implements Book {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "annotation")
    private String mAnnotation;

    @NonNull
    @ColumnInfo(name = "year")
    private int mYear;

    @SuppressWarnings("unused")
    @NonNull
    @ColumnInfo(name = "author_id")
    private int mAuthorId;

    @Ignore
    private RoomAuthor mAuthor;

    @SuppressWarnings("unused")
    @NonNull
    @ColumnInfo(name = "publisher_id")
    private int mPublisherId;

    @Ignore
    private RoomPublisher mPublisher;

    @SuppressWarnings("unused")
    @ColumnInfo(name = "owner_id")
    private Integer mOwnerId;

    @Ignore
    private RoomOwner mOwner;

    @Ignore
    private Set<RoomTag> mTags;

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setId(int id) {
        mId = id;
    }

    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(@NonNull String name) {
        mName = name;
    }

    @Nullable
    @Override
    public String getAnnotation() {
        return mAnnotation;
    }

    @Override
    public void setAnnotation(@Nullable String annotation) {
        mAnnotation = annotation;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public void setYear(int year) {
        mYear = year;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int id) {
        mAuthorId = id;
    }

    @NonNull
    @Override
    public Author getAuthor() {
        return mAuthor;
    }

    @Override
    public void setAuthor(@NonNull Author author) {
        mAuthor = (RoomAuthor) author;
        mAuthorId = mAuthor.getId();
    }

    public int getPublisherId() {
        return mPublisherId;
    }

    public void setPublisherId(int id) {
        mPublisherId = id;
    }

    @NonNull
    @Override
    public Publisher getPublisher() {
        return mPublisher;
    }

    @Override
    public void setPublisher(@NonNull Publisher publisher) {
        mPublisher = (RoomPublisher) publisher;
        mPublisherId = mPublisher.getId();
    }

    @NonNull
    @Override
    public Set<? extends Tag> getTags() {
        return mTags;
    }

    @Override
    public void setTags(@NonNull Set<? extends Tag> tags) {
        mTags = (Set<RoomTag>) tags;
    }

    @Nullable
    public Integer getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(@Nullable Integer id) {
        mOwnerId = id;
    }

    @Nullable
    @Override
    public Owner getOwner() {
        return mOwner;
    }

    @Override
    public void setOwner(@Nullable Owner owner) {
        mOwner = (RoomOwner) owner;
        mOwnerId = owner == null ? null : owner.getId();
    }
}

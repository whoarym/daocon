package me.whoarym.daocon.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class RoomBookTuple {
    @ColumnInfo(name = "_id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "annotation")
    private String mAnnotation;

    @ColumnInfo(name = "year")
    private int mYear;

    @ColumnInfo(name = "author_id")
    private int mAuthorId;

    @ColumnInfo(name = "author_name")
    private String mAuthorName;

    @ColumnInfo(name = "publisher_id")
    private int mPublisherId;

    @ColumnInfo(name = "publisher_name")
    private String mPublisherName;

    @ColumnInfo(name = "owner_id")
    private Integer mOwnerId;

    @ColumnInfo(name = "owner_first_name")
    private String mOwnerFirstName;

    @ColumnInfo(name = "owner_last_name")
    private String mOwnerLastName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAnnotation() {
        return mAnnotation;
    }

    public void setAnnotation(String annotation) {
        mAnnotation = annotation;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int authorId) {
        mAuthorId = authorId;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public int getPublisherId() {
        return mPublisherId;
    }

    public void setPublisherId(int publisherId) {
        mPublisherId = publisherId;
    }

    public String getPublisherName() {
        return mPublisherName;
    }

    public void setPublisherName(String publisherName) {
        mPublisherName = publisherName;
    }

    public Integer getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(Integer ownerId) {
        mOwnerId = ownerId;
    }

    public String getOwnerFirstName() {
        return mOwnerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        mOwnerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return mOwnerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        mOwnerLastName = ownerLastName;
    }

    static List<Integer> extractIds(@NonNull List<RoomBookTuple> bookTuples) {
        List<Integer> result = new ArrayList<>(bookTuples.size());
        for (RoomBookTuple tuple : bookTuples) {
            result.add(tuple.getId());
        }
        return result;
    }

    static List<RoomBook> convert(@NonNull List<RoomBookTuple> bookTuples,
                                  @NonNull List<RoomBookTagTuple> tagTuples) {
        SparseArray<Set<RoomTag>> book2tags = new SparseArray<>();
        for (RoomBookTagTuple tuple : tagTuples) {
            if (book2tags.get(tuple.getBookId()) == null) {
                book2tags.append(tuple.getBookId(), new HashSet<>());
            }
            book2tags.get(tuple.getBookId()).add(tuple.getTag());
        }

        List<RoomBook> result = new ArrayList<>(bookTuples.size());
        for (RoomBookTuple tuple : bookTuples) {
            RoomBook book = new RoomBook();
            book.setId(tuple.mId);
            book.setName(tuple.mName);
            book.setAnnotation(tuple.mAnnotation);
            book.setYear(tuple.mYear);

            RoomAuthor author = new RoomAuthor();
            author.setId(tuple.mAuthorId);
            author.setName(tuple.mAuthorName);
            book.setAuthor(author);

            RoomPublisher publisher = new RoomPublisher();
            publisher.setId(tuple.mPublisherId);
            publisher.setName(tuple.mPublisherName);
            book.setPublisher(publisher);

            if (tuple.mOwnerId != null) {
                RoomOwner owner = new RoomOwner();
                owner.setId(tuple.mOwnerId);
                owner.setFirstName(tuple.mOwnerFirstName);
                owner.setLastName(tuple.mOwnerLastName);
                book.setOwner(owner);
            }

            Set<RoomTag> tags = book2tags.get(book.getId());
            if (tags == null) {
                book.setTags(Collections.emptySet());
            } else {
                book.setTags(tags);
            }
            result.add(book);
        }
        return result;
    }
}

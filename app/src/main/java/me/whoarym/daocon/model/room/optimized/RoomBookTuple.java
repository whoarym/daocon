package me.whoarym.daocon.model.room.optimized;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.whoarym.daocon.model.room.RoomAuthor;
import me.whoarym.daocon.model.room.RoomBook;
import me.whoarym.daocon.model.room.RoomOwner;
import me.whoarym.daocon.model.room.RoomPublisher;
import me.whoarym.daocon.model.room.RoomTag;

class RoomBookTuple {

    @Embedded
    RoomBook mBook;

    @ColumnInfo(name = "author_name")
    String mAuthorName;

    @ColumnInfo(name = "publisher_name")
    String mPublisherName;

    @ColumnInfo(name = "owner_first_name")
    String mOwnerFirstName;

    @ColumnInfo(name = "owner_last_name")
    String mOwnerLastName;

    static List<Integer> extractIds(@NonNull List<RoomBookTuple> bookTuples) {
        List<Integer> result = new ArrayList<>(bookTuples.size());
        for (RoomBookTuple tuple : bookTuples) {
            result.add(tuple.mBook.getId());
        }
        return result;
    }

    static List<RoomBook> convert(@NonNull List<RoomBookTuple> bookTuples,
                                  @NonNull List<RoomBookTagTuple> tagTuples) {
        SparseArray<Set<RoomTag>> book2tags = new SparseArray<>();
        for (RoomBookTagTuple tuple : tagTuples) {
            if (book2tags.get(tuple.mBookId) == null) {
                book2tags.append(tuple.mBookId, new HashSet<>());
            }
            book2tags.get(tuple.mBookId).add(tuple.getTag());
        }

        List<RoomBook> result = new ArrayList<>(bookTuples.size());
        for (RoomBookTuple tuple : bookTuples) {
            RoomBook book = tuple.mBook;

            RoomAuthor author = new RoomAuthor();
            author.setId(book.getAuthorId());
            author.setName(tuple.mAuthorName);
            book.setAuthor(author);

            RoomPublisher publisher = new RoomPublisher();
            publisher.setId(book.getPublisherId());
            publisher.setName(tuple.mPublisherName);
            book.setPublisher(publisher);

            if (book.getOwnerId() != null) {
                RoomOwner owner = new RoomOwner();
                owner.setId(book.getOwnerId());
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

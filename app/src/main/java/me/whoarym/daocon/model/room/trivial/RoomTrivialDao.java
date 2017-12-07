package me.whoarym.daocon.model.room.trivial;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.room.RoomBook;
import me.whoarym.daocon.model.room.RoomDaoBase;
import me.whoarym.daocon.model.room.RoomSimpleDao;
import me.whoarym.daocon.model.room.RoomTag;

public class RoomTrivialDao extends RoomDaoBase {

    private final RoomBookTrivialDao mBookDao;

    public RoomTrivialDao(@NonNull RoomSimpleDao simpleDao, @NonNull RoomBookTrivialDao bookDao) {
        super(simpleDao);
        mBookDao = bookDao;
    }

    @NonNull
    @Override
    public String getName() {
        return "ROOM trivial";
    }

    @NonNull
    @Override
    public List<? extends Book> getBooks() {
        return fetchBooks(mBookDao.getBooks());
    }

    @NonNull
    @Override
    public List<? extends Book> getBooksByAuthor(@NonNull Author author) {
        return fetchBooks(mBookDao.getBooksByAuthor(author.getId()));
    }

    @NonNull
    @Override
    public List<? extends Book> getBooksByPublisher(@NonNull Publisher publisher) {
        return fetchBooks(mBookDao.getBooksByPublisher(publisher.getId()));
    }

    @NonNull
    @Override
    public List<? extends Book> getBooksByOwner(@NonNull Owner owner) {
        return fetchBooks(mBookDao.getBooksByOwner(owner.getId()));
    }

    private List<? extends Book> fetchBooks(@NonNull List<RoomBookTuple> tuples) {
        List<RoomBook> result = new ArrayList<>(tuples.size());
        List<Integer> booksIds = new ArrayList<>(tuples.size());
        // convert tuples to books
        for (RoomBookTuple tuple : tuples) {
            RoomBook book = tuple.getBook();
            result.add(book);
            booksIds.add(book.getId());
        }

        // fetch and set tags
        SparseArray<Set<RoomTag>> tagSet = RoomBookTagTuple.remap(mBookDao.getBookTags(booksIds));
        for (RoomBook book : result) {
            Set<RoomTag> tags = tagSet.get(book.getId());
            if (tags == null) {
                book.setTags(Collections.emptySet());
            } else {
                book.setTags(tags);
            }
        }
        return result;
    }
}

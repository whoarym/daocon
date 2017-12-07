package me.whoarym.daocon.model.room.optimized;

import android.support.annotation.NonNull;

import java.util.List;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.room.RoomBook;
import me.whoarym.daocon.model.room.RoomDaoBase;
import me.whoarym.daocon.model.room.RoomSimpleDao;

public class RoomOptimizedDao extends RoomDaoBase {

    private final RoomBookOptimizedDao mBookDao;

    public RoomOptimizedDao(@NonNull RoomSimpleDao simpleDao, @NonNull RoomBookOptimizedDao bookDao) {
        super(simpleDao);
        mBookDao = bookDao;
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

    private List<RoomBook> fetchBooks(List<RoomBookTuple> bookTuples) {
        return RoomBookTuple.convert(bookTuples, mBookDao.getBookTags(RoomBookTuple.extractIds(bookTuples)));
    }
}

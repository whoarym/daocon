package me.whoarym.daocon.model.room;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Dao;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;
import me.whoarym.daocon.model.json.JsonDataContainer;

public abstract class RoomDaoBase implements Dao {

    @NonNull
    private final RoomSimpleDao mSimpleDao;

    public RoomDaoBase(@NonNull RoomSimpleDao simpleDao) {
        mSimpleDao = simpleDao;
    }

    @Override
    public void importData(@NonNull JsonDataContainer dataContainer) {
        Set<RoomBook2Tag> roomBook2Tags = new HashSet<>();
        for (Book book : dataContainer.getBooks()) {
            for (Tag tag : book.getTags()) {
                roomBook2Tags.add(new RoomBook2Tag(book, tag));
            }
            // simple hack to update author adnd publisher ids
            book.setAuthor(book.getAuthor());
            book.setPublisher(book.getPublisher());
        }

        mSimpleDao.insertAll(
                (List<RoomOwner>) dataContainer.getOwners(),
                (List<RoomAuthor>) dataContainer.getAuthors(),
                (List<RoomPublisher>) dataContainer.getPublishers(),
                (List<RoomTag>) dataContainer.getTags(),
                (List<RoomBook>) dataContainer.getBooks(),
                new ArrayList<>(roomBook2Tags)
        );
    }

    @Override
    public void clearData() {
        mSimpleDao.deleteAll();
    }

    @NonNull
    @Override
    public List<? extends Author> getAuthors() {
        return mSimpleDao.getAuthors();
    }

    @NonNull
    @Override
    public List<? extends Publisher> getPublishers() {
        return mSimpleDao.getPublishers();
    }

    @NonNull
    @Override
    public List<? extends Owner> getOwners() {
        return mSimpleDao.getOwners();
    }

    @NonNull
    @Override
    public List<? extends Tag> getTags() {
        return mSimpleDao.getTags();
    }

    @Override
    public void saveAuthor(@NonNull Author author) {
        mSimpleDao.insertAuthor((RoomAuthor) author);
    }

    @Override
    public void savePublisher(@NonNull Publisher publisher) {
        mSimpleDao.insertPublisher((RoomPublisher) publisher);
    }

    @Override
    public void saveOwner(@NonNull Owner owner) {
        mSimpleDao.insertOwner((RoomOwner) owner);
    }

    @Override
    public void saveTag(@NonNull Tag tag) {
        mSimpleDao.insertTag((RoomTag) tag);
    }

    @Override
    public void saveBook(@NonNull Book book) {
        mSimpleDao.saveBook((RoomBook) book);
    }
}

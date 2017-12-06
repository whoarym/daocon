package me.whoarym.daocon.model.room;

import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.ModelFactory;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;

public class RoomModelFactory implements ModelFactory {
    @NonNull
    @Override
    public Author createAuthor() {
        return new RoomAuthor();
    }

    @NonNull
    @Override
    public Publisher createPublisher() {
        return new RoomPublisher();
    }

    @NonNull
    @Override
    public Owner createOwner() {
        return new RoomOwner();
    }

    @NonNull
    @Override
    public Tag createTag() {
        return new RoomTag();
    }

    @NonNull
    @Override
    public Book createBook() {
        return new RoomBook();
    }
}

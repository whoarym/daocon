package me.whoarym.daocon.model.sqlite;

import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.ModelFactory;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;

public class SqliteModelFactory implements ModelFactory {
    @NonNull
    @Override
    public Author createAuthor() {
        return new SqlAuthor();
    }

    @NonNull
    @Override
    public Publisher createPublisher() {
        return new SqlPublisher();
    }

    @NonNull
    @Override
    public Owner createOwner() {
        return new SqlOwner();
    }

    @NonNull
    @Override
    public Tag createTag() {
        return new SqlTag();
    }

    @NonNull
    @Override
    public Book createBook() {
        return new SqlBook();
    }
}

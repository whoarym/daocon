package me.whoarym.daocon.model;

import android.support.annotation.NonNull;

import java.util.List;

import me.whoarym.daocon.model.json.JsonDataContainer;

public interface Dao {

    @NonNull
    String getName();

    void importData(@NonNull JsonDataContainer dataContainer);
    void clearData();

    @NonNull
    List<? extends Author> getAuthors();
    @NonNull
    List<? extends Publisher> getPublishers();
    @NonNull
    List<? extends Owner> getOwners();
    @NonNull
    List<? extends Tag> getTags();
    @NonNull
    List<? extends Book> getBooks();
    @NonNull
    List<? extends Book> getBooksByAuthor(@NonNull Author author);
    @NonNull
    List<? extends Book> getBooksByPublisher(@NonNull Publisher publisher);
    @NonNull
    List<? extends Book> getBooksByOwner(@NonNull Owner owner);

    void saveAuthor(@NonNull Author author);
    void savePublisher(@NonNull Publisher publisher);
    void saveOwner(@NonNull Owner owner);
    void saveTag(@NonNull Tag tag);
    void saveBook(@NonNull Book book);
}

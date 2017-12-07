package me.whoarym.daocon.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.whoarym.daocon.model.Tag;

@Dao
public abstract class RoomSimpleDao {

    ///////////////////////////////////////////////////////////////////////////
    // insert section
    ///////////////////////////////////////////////////////////////////////////
    @Insert
    public abstract void insertOwners(@NonNull List<RoomOwner> owners);

    @Insert
    public abstract void insertAuthors(@NonNull List<RoomAuthor> authors);

    @Insert
    public abstract void insertPublishers(@NonNull List<RoomPublisher> publishers);

    @Insert
    public abstract void insertTags(@NonNull List<RoomTag> tags);

    @Insert
    public abstract void insertBooks(@NonNull List<RoomBook> books);

    @Insert
    public abstract void insertBookTagLinks(@NonNull List<RoomBook2Tag> book2Tag);

    @Transaction
    public void insertAll(@NonNull List<RoomOwner> owners,
                          @NonNull List<RoomAuthor> authors,
                          @NonNull List<RoomPublisher> publishers,
                          @NonNull List<RoomTag> tags,
                          @NonNull List<RoomBook> books,
                          @NonNull List<RoomBook2Tag> book2Tag) {
        insertOwners(owners);
        insertAuthors(authors);
        insertPublishers(publishers);
        insertTags(tags);
        insertBooks(books);
        insertBookTagLinks(book2Tag);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAuthor(@NonNull RoomAuthor author);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertOwner(@NonNull RoomOwner owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPublisher(@NonNull RoomPublisher publisher);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTag(@NonNull RoomTag tag);

    ///////////////////////////////////////////////////////////////////////////
    // delete section
    ///////////////////////////////////////////////////////////////////////////
    @Query("DELETE FROM tbl_author")
    public abstract void clearAuthors();

    @Query("DELETE FROM tbl_book")
    public abstract void clearBooks();

    @Query("DELETE FROM tbl_book2tag")
    public abstract void clearBook2Tag();

    @Query("DELETE FROM tbl_owner")
    public abstract void clearOwners();

    @Query("DELETE FROM tbl_publisher")
    public abstract void clearPublishers();

    @Query("DELETE FROM tbl_tag")
    public abstract void clearTags();

    @Transaction
    public void deleteAll() {
        clearBook2Tag();
        clearBooks();
        clearTags();
        clearOwners();
        clearPublishers();
        clearAuthors();
    }

    ///////////////////////////////////////////////////////////////////////////
    // get section
    ///////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM tbl_author ORDER BY name")
    public abstract List<RoomAuthor> getAuthors();

    @Query("SELECT * FROM tbl_publisher ORDER BY name")
    public abstract List<RoomPublisher> getPublishers();

    @Query("SELECT * FROM tbl_owner ORDER BY last_name")
    public abstract List<RoomOwner> getOwners();

    @Query("SELECT * FROM tbl_tag ORDER BY name")
    public abstract List<RoomTag> getTags();

    ///////////////////////////////////////////////////////////////////////////
    // save book implementation
    ///////////////////////////////////////////////////////////////////////////
    @Query("DELETE FROM tbl_book2tag WHERE book_id = :bookId")
    public abstract void deleteBookTagLinks(int bookId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBook(@NonNull RoomBook book);

    @Transaction
    public void saveBook(@NonNull RoomBook book) {
        deleteBookTagLinks(book.getId());
        insertBook(book);

        List<RoomBook2Tag> bookTagLinks = new ArrayList<>(book.getTags().size());
        for (Tag tag : book.getTags()) {
            bookTagLinks.add(new RoomBook2Tag(book, tag));
        }
        insertBookTagLinks(bookTagLinks);
    }
}

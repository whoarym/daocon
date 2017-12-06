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
public abstract class RoomBookDao {

    private static final String SELECT =
        "SELECT " +
        "  tbl_book._id as _id," +
        "  tbl_book.name as name," +
        "  tbl_book.annotation as annotation," +
        "  tbl_book.year as year," +
        "  tbl_book.author_id as author_id," +
        "  tbl_book.publisher_id as publisher_id," +
        "  tbl_book.owner_id as owner_id," +
        "  tbl_author.name as author_name," +
        "  tbl_publisher.name as publisher_name," +
        "  tbl_owner.first_name as owner_first_name," +
        "  tbl_owner.last_name as owner_last_name " +
        "FROM tbl_book " +
        "LEFT OUTER JOIN tbl_author ON author_id = tbl_author._id " +
        "LEFT OUTER JOIN tbl_publisher ON publisher_id = tbl_publisher._id " +
        "LEFT OUTER JOIN tbl_owner ON owner_id = tbl_owner._id";
    private static final String BY_AUTHOR = " WHERE tbl_author._id = :authorId";
    private static final String BY_PUBLISHER = " WHERE tbl_publisher._id = :publisherId";
    private static final String BY_OWNER = " WHERE tbl_owner._id = :ownerId";
    private static final String ORDER = " ORDER BY name;";

    private static final String TAG_SELECT =
        "SELECT book_id, tag_id, tbl_tag.name as tag_name " +
        "FROM tbl_book2tag LEFT OUTER JOIN tbl_tag ON tbl_tag._id = tag_id " +
        "WHERE book_id IN (:bookIds)";

    @Query(SELECT + ORDER)
    public abstract List<RoomBookTuple> getBooks();

    @Query(SELECT + BY_AUTHOR + ORDER)
    public abstract List<RoomBookTuple> getBooksByAuthor(int authorId);

    @Query(SELECT + BY_PUBLISHER + ORDER)
    public abstract List<RoomBookTuple> getBooksByPublisher(int publisherId);

    @Query(SELECT + BY_OWNER + ORDER)
    public abstract List<RoomBookTuple> getBooksByOwner(int ownerId);

    @Query(TAG_SELECT)
    public abstract List<RoomBookTagTuple> getBookTags(List<Integer> bookIds);

    ///////////////////////////////////////////////////////////////////////////
    // save book implementation
    ///////////////////////////////////////////////////////////////////////////
    @Query("DELETE FROM tbl_book2tag WHERE book_id = :bookId")
    public abstract void deleteBookTagLinks(int bookId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBook(@NonNull RoomBook book);

    @Insert
    public abstract void insertBookTagLinks(@NonNull List<RoomBook2Tag> links);

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

package me.whoarym.daocon.model.room.trivial;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface RoomBookTrivialDao {

    @Transaction
    @Query("SELECT * FROM tbl_book ORDER BY name;")
    List<RoomBookTuple> getBooks();

    @Transaction
    @Query("SELECT * FROM tbl_book WHERE author_id = :authorId ORDER BY name;")
    List<RoomBookTuple> getBooksByAuthor(int authorId);

    @Transaction
    @Query("SELECT * FROM tbl_book WHERE publisher_id = :publisherId ORDER BY name;")
    List<RoomBookTuple> getBooksByPublisher(int publisherId);

    @Transaction
    @Query("SELECT * FROM tbl_book WHERE owner_id = :ownerId ORDER BY name;")
    List<RoomBookTuple> getBooksByOwner(int ownerId);

    @Transaction
    @Query("SELECT * FROM tbl_book2tag WHERE book_id IN (:bookIds);")
    List<RoomBookTagTuple> getBookTags(List<Integer> bookIds);
}

package me.whoarym.daocon.model.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import me.whoarym.daocon.model.room.optimized.RoomBookOptimizedDao;
import me.whoarym.daocon.model.room.trivial.RoomBookTrivialDao;

@Database(
    version = 1,
    entities = {
            RoomAuthor.class,
            RoomPublisher.class,
            RoomOwner.class,
            RoomTag.class,
            RoomBook.class,
            RoomBook2Tag.class
    })
public abstract class RoomDaoDatabase extends RoomDatabase {
    abstract public RoomSimpleDao getSimpleDao();
    abstract public RoomBookOptimizedDao getBookOptimizedDao();
    abstract public RoomBookTrivialDao getBookTrivialDao();
}

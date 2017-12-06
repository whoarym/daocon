package me.whoarym.daocon.model.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

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
public abstract class DaoDatabase extends RoomDatabase {
    abstract public RoomSimpleDao getSimpleDao();
    abstract public RoomBookDao getBookDao();
}

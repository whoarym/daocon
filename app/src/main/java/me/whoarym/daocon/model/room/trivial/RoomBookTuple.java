package me.whoarym.daocon.model.room.trivial;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.List;

import me.whoarym.daocon.model.room.RoomAuthor;
import me.whoarym.daocon.model.room.RoomBook;
import me.whoarym.daocon.model.room.RoomOwner;
import me.whoarym.daocon.model.room.RoomPublisher;

class RoomBookTuple {

    @Embedded
    RoomBook mBook;

    @Relation(entity = RoomAuthor.class, entityColumn = "_id", parentColumn = "author_id")
    List<RoomAuthor> mAuthors;

    @Relation(entity = RoomPublisher.class, entityColumn = "_id", parentColumn = "publisher_id")
    List<RoomPublisher> mPublishers;

    @Relation(entity = RoomOwner.class, entityColumn = "_id", parentColumn = "owner_id")
    List<RoomOwner> mOwners;

    @NonNull
    RoomBook getBook() {
        mBook.setAuthor(mAuthors.get(0));
        mBook.setPublisher(mPublishers.get(0));
        if (mOwners != null && !mOwners.isEmpty()) {
            mBook.setOwner(mOwners.get(0));
        }
        return mBook;
    }
}

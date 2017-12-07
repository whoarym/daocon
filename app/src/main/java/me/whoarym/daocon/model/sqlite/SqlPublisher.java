package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Publisher;

import static me.whoarym.daocon.model.sqlite.SqlDaoBase.TBL_PUBLISHER;

public class SqlPublisher implements Publisher {
    private int mId;
    private String mName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqlPublisher publisher = (SqlPublisher) o;

        if (mId != publisher.mId) return false;
        return mName != null ? mName.equals(publisher.mName) : publisher.mName == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }

    static SqlDaoBase.DataFetcher<Publisher> fetcher() {
        return DATA_FETCHER;
    }

    private static final SqlDaoBase.DataFetcher<Publisher> DATA_FETCHER =
            new SqlDaoBase.DataFetcher<Publisher>() {
        @NonNull
        @Override
        public Cursor getCursor(@NonNull SQLiteDatabase db) {
            return db.query(TBL_PUBLISHER,
                    PublisherColumns.PROJECTION,
                    null,
                    null,
                    null,
                    null,
                    PublisherColumns.NAME);
        }

        @NonNull
        @Override
        public ContentValues to(@NonNull Publisher publisher) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PublisherColumns._ID, publisher.getId());
            contentValues.put(PublisherColumns.NAME, publisher.getName());
            return contentValues;
        }

        @NonNull
        @Override
        public Class<? extends Publisher> getEntityClass() {
            return SqlPublisher.class;
        }
    };

    public interface PublisherColumns extends BaseColumns {
        String PREFIX = "publisher_";
        String NAME = "name";

        String[] PROJECTION = new String[] {
                BaseColumns._ID,
                NAME
        };
    }
}

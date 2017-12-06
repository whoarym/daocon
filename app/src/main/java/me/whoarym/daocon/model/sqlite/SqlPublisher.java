package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.whoarym.daocon.model.Publisher;

import static me.whoarym.daocon.model.sqlite.SqlDao.TBL_PUBLISHER;

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

    static SqlDao.DataFetcher<Publisher> fetcher() {
        return DATA_FETCHER;
    }

    private static final SqlDao.DataFetcher<Publisher> DATA_FETCHER =
            new SqlDao.DataFetcher<Publisher>() {
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

        @Nullable
        @Override
        public Publisher from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(prefix + PublisherColumns._ID);
            if (id == null) {
                return null;
            }
            SqlPublisher sqlPublisher = new SqlPublisher();
            sqlPublisher.setId(id);
            sqlPublisher.setName(contentValues.getAsString(prefix + PublisherColumns.NAME));
            return sqlPublisher;
        }

        @Nullable
        @Override
        public Publisher from(@NonNull Cursor cursor, @NonNull String prefix) {
            int id = cursor.getColumnIndexOrThrow(prefix + PublisherColumns._ID);
            int name = cursor.getColumnIndexOrThrow(prefix + PublisherColumns.NAME);
            if (cursor.isNull(id)) {
                return null;
            }
            SqlPublisher publisher = new SqlPublisher();
            publisher.setId(cursor.getInt(id));
            publisher.setName(cursor.getString(name));
            return publisher;
        }
    };

    interface PublisherColumns extends BaseColumns {
        String PREFIX = "publisher_";
        String NAME = "name";

        String[] PROJECTION = new String[] {
                BaseColumns._ID,
                NAME
        };
    }
}

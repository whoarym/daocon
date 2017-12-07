package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Tag;

import static me.whoarym.daocon.model.sqlite.SqlDaoBase.TBL_TAG;

public class SqlTag implements Tag {
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

        SqlTag sqlTag = (SqlTag) o;

        if (mId != sqlTag.mId) return false;
        return mName != null ? mName.equals(sqlTag.mName) : sqlTag.mName == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }

    @NonNull
    static SqlDaoBase.DataFetcher<Tag> fetcher() {
        return DATA_FETCHER;
    }

    private static final SqlDaoBase.DataFetcher<Tag> DATA_FETCHER = new SqlDaoBase.DataFetcher<Tag>() {
        @NonNull
        @Override
        public Cursor getCursor(@NonNull SQLiteDatabase db) {
            return db.query(TBL_TAG,
                    TagColumns.PROJECTION,
                    null, null, null, null,
                    TagColumns.NAME);
        }

        @NonNull
        @Override
        public ContentValues to(@NonNull Tag tag) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TagColumns._ID, tag.getId());
            contentValues.put(TagColumns.NAME, tag.getName());
            return contentValues;
        }

        @NonNull
        @Override
        public Class<? extends Tag> getEntityClass() {
            return SqlTag.class;
        }
    };

    public interface TagColumns extends BaseColumns {
        String NAME = "name";

        String[] PROJECTION = new String[] {
                BaseColumns._ID,
                NAME
        };
    }
}

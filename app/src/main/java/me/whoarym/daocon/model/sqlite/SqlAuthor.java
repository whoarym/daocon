package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Author;

import static me.whoarym.daocon.model.sqlite.SqlDaoBase.TBL_AUTHOR;

public class SqlAuthor implements Author {
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

        SqlAuthor sqlAuthor = (SqlAuthor) o;

        if (mId != sqlAuthor.mId) return false;
        return mName != null ? mName.equals(sqlAuthor.mName) : sqlAuthor.mName == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }

    static SqlDaoBase.DataFetcher<Author> fetcher() {
        return DATA_FETCHER;
    }

    private static SqlDaoBase.DataFetcher<Author> DATA_FETCHER = new SqlDaoBase.DataFetcher<Author>() {
        @NonNull
        @Override
        public Cursor getCursor(@NonNull SQLiteDatabase db) {
            return db.query(TBL_AUTHOR,
                            AuthorColumns.PROJECTION,
                            null,
                            null,
                            null,
                            null,
                            AuthorColumns.NAME);
        }

        @NonNull
        @Override
        public ContentValues to(@NonNull Author author) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AuthorColumns._ID, author.getId());
            contentValues.put(AuthorColumns.NAME, author.getName());
            return contentValues;
        }

        @NonNull
        @Override
        public Class<? extends Author> getEntityClass() {
            return SqlAuthor.class;
        }
    };

    public interface AuthorColumns extends BaseColumns {
        String PREFIX = "author_";
        String NAME = "name";

        String[] PROJECTION = new String[] {
                BaseColumns._ID,
                NAME
        };
    }
}

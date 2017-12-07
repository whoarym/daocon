package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.Owner;

import static me.whoarym.daocon.model.sqlite.SqlDaoBase.TBL_OWNER;

public class SqlOwner implements Owner {
    private int mId;
    private String mFirstName;
    private String mLastName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    @Override
    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqlOwner sqlOwner = (SqlOwner) o;

        if (mId != sqlOwner.mId) return false;
        if (mFirstName != null ? !mFirstName.equals(sqlOwner.mFirstName) : sqlOwner.mFirstName != null)
            return false;
        return mLastName != null ? mLastName.equals(sqlOwner.mLastName) : sqlOwner.mLastName == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mFirstName != null ? mFirstName.hashCode() : 0);
        result = 31 * result + (mLastName != null ? mLastName.hashCode() : 0);
        return result;
    }

    @NonNull
    static SqlDaoBase.DataFetcher<Owner> fetcher() {
        return DATA_FETCHER;
    }

    private static final SqlDaoBase.DataFetcher<Owner> DATA_FETCHER = new SqlDaoBase.DataFetcher<Owner>() {
        @NonNull
        @Override
        public Cursor getCursor(@NonNull SQLiteDatabase db) {
            return db.query(TBL_OWNER,
                    OwnerColumns.PROJECTION,
                    null,
                    null,
                    null,
                    null,
                    OwnerColumns.LAST_NAME);
        }

        @NonNull
        @Override
        public ContentValues to(@NonNull Owner owner) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(OwnerColumns._ID, owner.getId());
            contentValues.put(OwnerColumns.FIRST_NAME, owner.getFirstName());
            contentValues.put(OwnerColumns.LAST_NAME, owner.getLastName());
            return contentValues;
        }

        @NonNull
        @Override
        public Class<? extends Owner> getEntityClass() {
            return SqlOwner.class;
        }
    };

    public interface OwnerColumns extends BaseColumns {
        String PREFIX = "owner_";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";

        String[] PROJECTION = new String[] {
                BaseColumns._ID,
                FIRST_NAME,
                LAST_NAME
        };
    }
}

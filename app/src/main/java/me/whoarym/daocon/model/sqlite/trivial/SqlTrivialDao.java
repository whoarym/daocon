package me.whoarym.daocon.model.sqlite.trivial;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.sqlite.SqlBook;
import me.whoarym.daocon.model.sqlite.SqlDaoBase;
import me.whoarym.daocon.model.sqlite.SqlTag;

public class SqlTrivialDao extends SqlDaoBase {
    public SqlTrivialDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    @NonNull
    @Override
    public String getName() {
        return "SQL trivial";
    }

    @Override
    protected <T> T from(@NonNull Cursor cursor,
                         @NonNull Class<? extends T> entityClass,
                         @NonNull String prefix) {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
        return Converter.get(entityClass).from(contentValues, prefix);
    }

    @Override
    protected TagInfo getTagInfo(@NonNull Cursor cursor) {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
        Integer bookId = contentValues.getAsInteger(SqlBook.Book2TagColumns.BOOK_ID);
        SqlTag tag = Converter.get(SqlTag.class).from(contentValues, "");

        return new TagInfo(bookId, tag);
    }
}

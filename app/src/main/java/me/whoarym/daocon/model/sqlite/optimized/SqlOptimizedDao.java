package me.whoarym.daocon.model.sqlite.optimized;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.sqlite.SqlBook;
import me.whoarym.daocon.model.sqlite.SqlDaoBase;
import me.whoarym.daocon.model.sqlite.SqlTag;

public class SqlOptimizedDao extends SqlDaoBase {
    public SqlOptimizedDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    @NonNull
    @Override
    public String getName() {
        return "SQL optimized";
    }

    @Override
    protected <T> T from(@NonNull Cursor cursor,
                         @NonNull Class<? extends T> entityClass,
                         @NonNull String prefix) {
        return Converter.get(entityClass).from(cursor, prefix);
    }

    @Override
    protected TagInfo getTagInfo(@NonNull Cursor cursor) {
        Integer bookId = null;
        int bookIdIdx = cursor.getColumnIndexOrThrow(SqlBook.Book2TagColumns.BOOK_ID);
        if (!cursor.isNull(bookIdIdx)) {
            bookId = cursor.getInt(bookIdIdx);
        }

        SqlTag tag = Converter.get(SqlTag.class).from(cursor, "");
        return new TagInfo(bookId, tag);
    }
}

package me.whoarym.daocon.model.sqlite.optimized;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import me.whoarym.daocon.model.sqlite.SqlAuthor;
import me.whoarym.daocon.model.sqlite.SqlBook;
import me.whoarym.daocon.model.sqlite.SqlOwner;
import me.whoarym.daocon.model.sqlite.SqlPublisher;
import me.whoarym.daocon.model.sqlite.SqlTag;

abstract class Converter<T> {
    abstract T from(@NonNull Cursor cursor, @NonNull String prefix);

    private static final Converter<SqlAuthor> AUTHOR = new Converter<SqlAuthor>() {
        @Override
        SqlAuthor from(@NonNull Cursor cursor, @NonNull String prefix) {
            int id = cursor.getColumnIndexOrThrow(prefix + SqlAuthor.AuthorColumns._ID);
            if (cursor.isNull(id)) {
                return null;
            }
            int name = cursor.getColumnIndexOrThrow(prefix + SqlAuthor.AuthorColumns.NAME);
            SqlAuthor author = new SqlAuthor();
            author.setId(cursor.getInt(id));
            author.setName(cursor.getString(name));
            return author;
        }
    };

    private static final Converter<SqlPublisher> PUBLISHER = new Converter<SqlPublisher>() {
        @Override
        SqlPublisher from(@NonNull Cursor cursor, @NonNull String prefix) {
            int id = cursor.getColumnIndexOrThrow(prefix + SqlPublisher.PublisherColumns._ID);
            if (cursor.isNull(id)) {
                return null;
            }
            int name = cursor.getColumnIndexOrThrow(prefix + SqlPublisher.PublisherColumns.NAME);
            SqlPublisher publisher = new SqlPublisher();
            publisher.setId(cursor.getInt(id));
            publisher.setName(cursor.getString(name));
            return publisher;
        }
    };

    private static final Converter<SqlOwner> OWNER = new Converter<SqlOwner>() {
        @Override
        SqlOwner from(@NonNull Cursor cursor, @NonNull String prefix) {
            int id = cursor.getColumnIndexOrThrow(prefix + SqlOwner.OwnerColumns._ID);
            if (cursor.isNull(id)) {
                return null;
            }
            int firstName = cursor.getColumnIndexOrThrow(prefix + SqlOwner.OwnerColumns.FIRST_NAME);
            int lastName = cursor.getColumnIndexOrThrow(prefix + SqlOwner.OwnerColumns.LAST_NAME);
            SqlOwner owner = new SqlOwner();
            owner.setId(cursor.getInt(id));
            owner.setFirstName(cursor.getString(firstName));
            owner.setLastName(cursor.getString(lastName));
            return owner;
        }
    };

    private static final Converter<SqlTag> TAG = new Converter<SqlTag>() {
        @Override
        SqlTag from(@NonNull Cursor cursor, @NonNull String prefix) {
            int tagId = cursor.getColumnIndexOrThrow(prefix + SqlTag.TagColumns._ID);
            if (cursor.isNull(tagId)) {
                return null;
            }
            int nameId = cursor.getColumnIndexOrThrow(prefix + SqlTag.TagColumns.NAME);
            SqlTag tag = new SqlTag();
            tag.setId(cursor.getInt(tagId));
            tag.setName(cursor.getString(nameId));
            return tag;
        }
    };

    private static final Converter<SqlBook> BOOK = new Converter<SqlBook>() {
        @Override
        SqlBook from(@NonNull Cursor cursor, @NonNull String prefix) {
            int id = cursor.getColumnIndexOrThrow(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns._ID);
            if (cursor.isNull(id)) {
                return null;
            }

            int name = cursor.getColumnIndexOrThrow(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.NAME);
            int annotation = cursor.getColumnIndexOrThrow(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.ANNOTATION);
            int year = cursor.getColumnIndexOrThrow(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.YEAR);

            SqlBook book = new SqlBook();
            book.setId(cursor.getInt(id));
            book.setName(cursor.getString(name));
            book.setAnnotation(cursor.getString(annotation));
            book.setYear(cursor.getInt(year));

            book.setAuthor(AUTHOR.from(cursor, SqlAuthor.AuthorColumns.PREFIX));
            book.setPublisher(PUBLISHER.from(cursor, SqlPublisher.PublisherColumns.PREFIX));
            book.setOwner(OWNER.from(cursor, SqlOwner.OwnerColumns.PREFIX));

            return book;
        }
    };

    private static final Converter EMPTY = new Converter() {
        @Override
        Object from(@NonNull Cursor cursor, @NonNull String prefix) {
            return null;
        }
    };

    private static final Map<Class, Converter> CONVERTER_MAP = new HashMap<>();
    static {
        CONVERTER_MAP.put(SqlAuthor.class, AUTHOR);
        CONVERTER_MAP.put(SqlPublisher.class, PUBLISHER);
        CONVERTER_MAP.put(SqlOwner.class, OWNER);
        CONVERTER_MAP.put(SqlTag.class, TAG);
        CONVERTER_MAP.put(SqlBook.class, BOOK);
    }

    @NonNull
    static <R> Converter<R> get(Class<? extends R> entityClass) {
        Converter converter = CONVERTER_MAP.get(entityClass);
        if (converter == null) {
            return EMPTY;
        }
        return converter;
    }
}

package me.whoarym.daocon.model.sqlite.trivial;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import me.whoarym.daocon.model.sqlite.SqlAuthor;
import me.whoarym.daocon.model.sqlite.SqlBook;
import me.whoarym.daocon.model.sqlite.SqlOwner;
import me.whoarym.daocon.model.sqlite.SqlPublisher;
import me.whoarym.daocon.model.sqlite.SqlTag;

abstract class Converter<T> {
    abstract T from(@NonNull ContentValues contentValues, @NonNull String prefix);

    private static final Converter<SqlAuthor> AUTHOR = new Converter<SqlAuthor>() {
        @Override
        SqlAuthor from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(prefix + SqlAuthor.AuthorColumns._ID);
            if (id == null) {
                return null;
            }
            SqlAuthor sqlAuthor = new SqlAuthor();
            sqlAuthor.setId(id);
            sqlAuthor.setName(contentValues.getAsString(prefix + SqlAuthor.AuthorColumns.NAME));
            return sqlAuthor;
        }
    };

    private static final Converter<SqlPublisher> PUBLISHER = new Converter<SqlPublisher>() {
        @Override
        SqlPublisher from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(prefix + SqlPublisher.PublisherColumns._ID);
            if (id == null) {
                return null;
            }
            SqlPublisher sqlPublisher = new SqlPublisher();
            sqlPublisher.setId(id);
            sqlPublisher.setName(contentValues.getAsString(prefix + SqlPublisher.PublisherColumns.NAME));
            return sqlPublisher;
        }
    };

    private static final Converter<SqlOwner> OWNER = new Converter<SqlOwner>() {
        @Override
        SqlOwner from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(prefix + SqlOwner.OwnerColumns._ID);
            if (id == null) {
                return null;
            }
            SqlOwner owner = new SqlOwner();
            owner.setId(id);
            owner.setFirstName(contentValues.getAsString(prefix + SqlOwner.OwnerColumns.FIRST_NAME));
            owner.setLastName(contentValues.getAsString(prefix + SqlOwner.OwnerColumns.LAST_NAME));
            return owner;
        }
    };

    private static final Converter<SqlTag> TAG = new Converter<SqlTag>() {
        @Override
        SqlTag from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(prefix + SqlTag.TagColumns._ID);
            if (id == null) {
                return null;
            }
            SqlTag tag = new SqlTag();
            tag.setId(id);
            tag.setName(contentValues.getAsString(prefix + SqlTag.TagColumns.NAME));
            return tag;
        }
    };

    private static final Converter<SqlBook> BOOK = new Converter<SqlBook>() {
        @Override
        SqlBook from(@NonNull ContentValues contentValues, @NonNull String prefix) {
            Integer id = contentValues.getAsInteger(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns._ID);
            if (id == null) {
                return null;
            }
            SqlBook book = new SqlBook();
            book.setId(id);
            book.setName(contentValues.getAsString(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.NAME));
            book.setAnnotation(contentValues.getAsString(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.ANNOTATION));
            book.setYear(contentValues.getAsInteger(SqlBook.BookColumns.PREFIX + SqlBook.BookColumns.YEAR));

            book.setAuthor(AUTHOR.from(contentValues, SqlAuthor.AuthorColumns.PREFIX));
            book.setPublisher(PUBLISHER.from(contentValues, SqlPublisher.PublisherColumns.PREFIX));
            book.setOwner(OWNER.from(contentValues, SqlOwner.OwnerColumns.PREFIX));

            return book;
        }
    };

    private static final Converter EMPTY = new Converter() {
        @Override
        Object from(@NonNull ContentValues contentValues, @NonNull String prefix) {
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

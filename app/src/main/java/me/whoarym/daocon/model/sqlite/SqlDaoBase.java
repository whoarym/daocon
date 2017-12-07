package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Dao;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;
import me.whoarym.daocon.model.json.JsonDataContainer;

public abstract class SqlDaoBase implements Dao {

    static final String TBL_AUTHOR = "tbl_author";
    static final String TBL_BOOK = "tbl_book";
    static final String TBL_OWNER = "tbl_owner";
    static final String TBL_PUBLISHER = "tbl_publisher";
    static final String TBL_TAG = "tbl_tag";
    static final String TBL_BOOK2TAG = "tbl_book2tag";

    private static final String TAGS_QUERY =
            "SELECT _id, name, book_id, tag_id" +
            " FROM " + TBL_BOOK2TAG +
            " LEFT OUTER JOIN " + TBL_TAG + " ON _id = tag_id" +
            " WHERE book_id IN ";

    @NonNull
    private final SQLiteDatabase mDb;

    public SqlDaoBase(@NonNull SQLiteDatabase db) {
        mDb = db;
    }

    @Override
    public void importData(@NonNull JsonDataContainer dataContainer) {
        mDb.beginTransaction();

        try {
            for (Tag tag : dataContainer.getTags()) {
                mDb.insert(TBL_TAG, null, SqlTag.fetcher().to(tag));
            }

            for (Author author : dataContainer.getAuthors()) {
                mDb.insert(TBL_AUTHOR, null, SqlAuthor.fetcher().to(author));
            }

            for (Publisher publisher : dataContainer.getPublishers()) {
                mDb.insert(TBL_PUBLISHER, null, SqlPublisher.fetcher().to(publisher));
            }

            for (Owner owner : dataContainer.getOwners()) {
                mDb.insert(TBL_OWNER, null, SqlOwner.fetcher().to(owner));
            }

            for (Book book : dataContainer.getBooks()) {
                mDb.insert(TBL_BOOK, null, SqlBook.fetcher().to(book));
                for (Tag tag : book.getTags()) {
                    mDb.insert(TBL_BOOK2TAG, null, SqlBook.toContentValues(book, tag));
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    @Override
    public void clearData() {
        mDb.beginTransaction();
        try {
            mDb.execSQL("DELETE FROM " + TBL_BOOK2TAG);
            mDb.execSQL("DELETE FROM " + TBL_TAG);
            mDb.execSQL("DELETE FROM " + TBL_BOOK);
            mDb.execSQL("DELETE FROM " + TBL_OWNER);
            mDb.execSQL("DELETE FROM " + TBL_PUBLISHER);
            mDb.execSQL("DELETE FROM " + TBL_AUTHOR);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    @NonNull
    @Override
    public List<Author> getAuthors() {
        return fetch(SqlAuthor.fetcher());
    }

    @NonNull
    @Override
    public List<Publisher> getPublishers() {
        return fetch(SqlPublisher.fetcher());
    }

    @NonNull
    @Override
    public List<Owner> getOwners() {
        return fetch(SqlOwner.fetcher());
    }

    @NonNull
    @Override
    public List<? extends Tag> getTags() {
        return fetch(SqlTag.fetcher());
    }

    @NonNull
    @Override
    public List<Book> getBooks() {
        return fetchTags(fetch(SqlBook.fetcher()));
    }

    @NonNull
    @Override
    public List<Book> getBooksByAuthor(@NonNull Author author) {
        return fetchTags(fetch(SqlBook.fetcher(author)));
    }

    @NonNull
    @Override
    public List<Book> getBooksByPublisher(@NonNull Publisher publisher) {
        return fetchTags(fetch(SqlBook.fetcher(publisher)));
    }

    @NonNull
    @Override
    public List<Book> getBooksByOwner(@NonNull Owner owner) {
        return fetchTags(fetch(SqlBook.fetcher(owner)));
    }

    @Override
    public void saveAuthor(@NonNull Author author) {
        mDb.beginTransaction();
        mDb.replace(TBL_AUTHOR, null, SqlAuthor.fetcher().to(author));
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    @Override
    public void savePublisher(@NonNull Publisher publisher) {
        mDb.beginTransaction();
        mDb.replace(TBL_PUBLISHER, null, SqlPublisher.fetcher().to(publisher));
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    @Override
    public void saveOwner(@NonNull Owner owner) {
        mDb.beginTransaction();
        mDb.replace(TBL_OWNER, null, SqlOwner.fetcher().to(owner));
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    @Override
    public void saveTag(@NonNull Tag tag) {
        mDb.beginTransaction();
        mDb.replace(TBL_TAG, null, SqlTag.fetcher().to(tag));
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    @Override
    public void saveBook(@NonNull Book book) {
        mDb.beginTransaction();
        mDb.replace(TBL_BOOK, null, SqlBook.fetcher().to(book));
        for (Tag tag : book.getTags()) {
            mDb.replace(TBL_BOOK2TAG, null, SqlBook.toContentValues(book, tag));
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    protected abstract <T> T from(@NonNull Cursor cursor,
                                  @NonNull Class<? extends T> entityClass,
                                  @NonNull String prefix);

    protected abstract TagInfo getTagInfo(@NonNull Cursor cursor);

    private <T> List<T> fetch(@NonNull DataFetcher<T> dataFetcher) {
        List<T> result;
        try (Cursor cursor = dataFetcher.getCursor(mDb)) {
            result = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                T item = from(cursor, dataFetcher.getEntityClass(), "");
                if (item != null) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    @NonNull
    private List<Book> fetchTags(@NonNull List<Book> books) {
        if (books.isEmpty()) {
            return books;
        }

        StringBuilder builder = new StringBuilder(TAGS_QUERY);
        builder.append("(");

        boolean first = true;
        for (Book book : books) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(book.getId());
        }
        builder.append(");");

        SparseArray<Set<Tag>> book2tag = new SparseArray<>(books.size());
        try (Cursor cursor = mDb.rawQuery(builder.toString(), null)) {
            while (cursor.moveToNext()) {
                TagInfo tagInfo = getTagInfo(cursor);
                Integer bookId = tagInfo.mBookId;
                Tag tag = tagInfo.mTag;

                if (bookId != null && tag != null) {
                    if (book2tag.get(bookId) == null) {
                        book2tag.append(bookId, new HashSet<>());
                    }
                    book2tag.get(bookId).add(tag);
                }
            }
        }

        for (Book book : books) {
            Set<Tag> tags = book2tag.get(book.getId());
            if (tags == null) {
                book.setTags(Collections.emptySet());
            } else {
                book.setTags(tags);
            }
        }

        return books;
    }

    protected static class TagInfo {
        Integer mBookId;
        SqlTag mTag;

        public TagInfo(@Nullable Integer bookId, @NonNull SqlTag tag) {
            mBookId = bookId;
            mTag = tag;
        }
    }

    interface DataFetcher<T> {
        @NonNull Cursor getCursor(@NonNull SQLiteDatabase db);
        @NonNull ContentValues to(@NonNull T value);
        @NonNull Class<? extends T> getEntityClass();
    }
}

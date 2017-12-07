package me.whoarym.daocon.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;

public class SqlBook implements Book {
    private int mId;
    private String mName;
    private String mAnnotation;
    private int mYear;
    private SqlAuthor mAuthor;
    private SqlPublisher mPublisher;
    private Set<SqlTag> mTags;
    private SqlOwner mOwner;

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setId(int id) {
        this.mId = id;
    }

    @Override
    @NonNull
    public String getName() {
        return mName;
    }

    @Override
    public void setName(@NonNull String name) {
        mName = name;
    }

    @Override
    @Nullable
    public String getAnnotation() {
        return mAnnotation;
    }

    @Override
    public void setAnnotation(@Nullable String annotation) {
        mAnnotation = annotation;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public void setYear(int year) {
        mYear = year;
    }

    @Override
    @NonNull
    public Author getAuthor() {
        return mAuthor;
    }

    @Override
    public void setAuthor(@NonNull Author author) {
        mAuthor = (SqlAuthor) author;
    }

    @Override
    @NonNull
    public Publisher getPublisher() {
        return mPublisher;
    }

    @Override
    public void setPublisher(@NonNull Publisher publisher) {
        mPublisher = (SqlPublisher) publisher;
    }

    @Override
    @NonNull
    public Set<? extends Tag> getTags() {
        return mTags;
    }

    @Override
    public void setTags(@NonNull Set<? extends Tag> tags) {
        mTags = (Set<SqlTag>) tags;
    }

    @Override
    @Nullable
    public Owner getOwner() {
        return mOwner;
    }

    @Override
    public void setOwner(@Nullable Owner owner) {
        mOwner = (SqlOwner) owner;
    }

    static SqlDaoBase.DataFetcher<Book> fetcher() {
        return new BookDataFetcher();
    }

    static SqlDaoBase.DataFetcher<Book> fetcher(@NonNull Author author) {
        return new BookDataFetcher(author);
    }

    static SqlDaoBase.DataFetcher<Book> fetcher(@NonNull Publisher publisher) {
        return new BookDataFetcher(publisher);
    }

    static SqlDaoBase.DataFetcher<Book> fetcher(@NonNull Owner owner) {
        return new BookDataFetcher(owner);
    }

    private static class BookDataFetcher implements SqlDaoBase.DataFetcher<Book> {

        private final String mQuery;

        BookDataFetcher() {
            mQuery = new QueryBuilder().build();
        }

        BookDataFetcher(@NonNull Author author) {
            mQuery = new QueryBuilder(author).build();
        }

        BookDataFetcher(@NonNull Publisher publisher) {
            mQuery = new QueryBuilder(publisher).build();
        }

        BookDataFetcher(@NonNull Owner owner) {
            mQuery = new QueryBuilder(owner).build();
        }

        @NonNull
        @Override
        public Cursor getCursor(@NonNull SQLiteDatabase db) {
            return db.rawQuery(mQuery, null);
        }

        @NonNull
        @Override
        public ContentValues to(@NonNull Book book) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookColumns._ID, book.getId());
            contentValues.put(BookColumns.NAME, book.getName());
            contentValues.put(BookColumns.ANNOTATION, book.getAnnotation());
            contentValues.put(BookColumns.YEAR, book.getYear());

            Author author = book.getAuthor();
            contentValues.put(BookColumns.AUTHOR, author.getId());

            Publisher publisher = book.getPublisher();
            contentValues.put(BookColumns.PUBLISHER, publisher.getId());

            SqlOwner owner = (SqlOwner) book.getOwner();
            contentValues.put(BookColumns.OWNER, owner == null ? null : owner.getId());

            return contentValues;
        }

        @NonNull
        @Override
        public Class<? extends Book> getEntityClass() {
            return SqlBook.class;
        }
    }

    private static class QueryBuilder {
        private static final String SELECT =
            "SELECT " +
            "  tbl_book._id as book__id, " +
            "  tbl_book.name as book_name, " +
            "  tbl_book.annotation as book_annotation, " +
            "  tbl_book.year as book_year, " +
            "  tbl_author._id as author__id, " +
            "  tbl_author.name as author_name, " +
            "  tbl_publisher._id as publisher__id, " +
            "  tbl_publisher.name as publisher_name, " +
            "  tbl_owner._id as owner__id, " +
            "  tbl_owner.first_name as owner_first_name, " +
            "  tbl_owner.last_name as owner_last_name " +
            "FROM tbl_book " +
            "LEFT OUTER JOIN tbl_author ON tbl_book.author_id = tbl_author._id " +
            "LEFT OUTER JOIN tbl_publisher ON tbl_book.publisher_id = tbl_publisher._id " +
            "LEFT OUTER JOIN tbl_owner ON tbl_book.owner_id = tbl_owner._id";
        private static final String WHERE = " WHERE ";
        private static final String AUTHOR = "tbl_book.author_id = ";
        private static final String PUBLISHER = "tbl_book.publisher_id = ";
        private static final String OWNER = "tbl_book.owner_id = ";
        private static final String ORDER = " ORDER BY book_name;";


        private StringBuilder mBuilder = new StringBuilder(SELECT);
        private boolean mWhere = false;

        QueryBuilder() {
        }

        QueryBuilder(@NonNull Author author) {
            mBuilder
                .append(WHERE)
                .append(AUTHOR)
                .append(author.getId());
        }

        QueryBuilder(@NonNull Publisher publisher) {
            mBuilder
                .append(WHERE)
                .append(PUBLISHER)
                .append(publisher.getId());
        }

        QueryBuilder(@NonNull Owner owner) {
            mBuilder
                .append(WHERE)
                .append(OWNER)
                .append(owner.getId());
        }

        String build() {
            mBuilder.append(ORDER);
            return mBuilder.toString();
        }
    }

    @NonNull
    static ContentValues toContentValues(@NonNull Book book, @NonNull Tag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Book2TagColumns.BOOK_ID, book.getId());
        contentValues.put(Book2TagColumns.TAG_ID, tag.getId());
        return contentValues;
    }

    public interface Book2TagColumns extends BaseColumns {
        String BOOK_ID = "book_id";
        String TAG_ID = "tag_id";
    }

    public interface BookColumns extends BaseColumns {
        String PREFIX = "book_";
        String NAME = "name";
        String ANNOTATION = "annotation";
        String YEAR = "year";
        String AUTHOR = "author_id";
        String PUBLISHER = "publisher_id";
        String OWNER = "owner_id";
    }
}

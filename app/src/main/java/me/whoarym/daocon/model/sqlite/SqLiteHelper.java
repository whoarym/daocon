package me.whoarym.daocon.model.sqlite;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okio.BufferedSource;
import okio.Okio;

public class SqLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SqLiteHelper";

    private static final String DATABASE_NAME = "sql_contest.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE = "create.sql";
    private static final String SQL_DROP = "drop.sql";

    @NonNull
    private final Context mContext;

    public SqLiteHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = loadSql(SQL_CREATE);
        sqLiteDatabase.beginTransaction();
        for (String sqlRow : sql.split("\n")) {
            sqLiteDatabase.execSQL(sqlRow);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = loadSql(SQL_DROP);
        sqLiteDatabase.beginTransaction();
        for (String sqlRow : sql.split("\n")) {
            sqLiteDatabase.execSQL(sqlRow);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        onCreate(sqLiteDatabase);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @NonNull
    private String loadSql(@NonNull String sql) {
        AssetManager assetManager = mContext.getAssets();
        try {
            BufferedSource source = Okio.buffer(Okio.source(assetManager.open(sql)));
            return source.readString(Charset.forName("utf-8"));
        } catch (IOException e) {
            Log.e(TAG, "loadSql()", e);
            throw new RuntimeException(e);
        }
    }
}

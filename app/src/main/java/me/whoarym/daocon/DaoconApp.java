package me.whoarym.daocon;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import me.whoarym.daocon.model.room.DaoDatabase;
import me.whoarym.daocon.model.sqlite.SqLiteHelper;

public class DaoconApp extends Application {

    @NonNull
    private SqLiteHelper mSqLiteHelper;

    @NonNull
    private DaoDatabase mRoomDb;

    @Override
    public void onCreate() {
        super.onCreate();

        mSqLiteHelper = new SqLiteHelper(this);
        mRoomDb = Room.databaseBuilder(this, DaoDatabase.class, "room.db").build();
    }

    @NonNull
    public SQLiteDatabase getSqLiteDb() {
        return mSqLiteHelper.getWritableDatabase();
    }

    @NonNull
    public DaoDatabase getRoomDb() {
        return mRoomDb;
    }
}

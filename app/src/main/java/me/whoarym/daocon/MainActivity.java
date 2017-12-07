package me.whoarym.daocon;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.whoarym.daocon.measure.MeasureAsyncTask;
import me.whoarym.daocon.measure.MeasureListener;
import me.whoarym.daocon.model.json.JsonDataContainer;
import me.whoarym.daocon.model.room.RoomDaoDatabase;
import me.whoarym.daocon.model.room.RoomModelFactory;
import me.whoarym.daocon.model.room.optimized.RoomOptimizedDao;
import me.whoarym.daocon.model.room.trivial.RoomTrivialDao;
import me.whoarym.daocon.model.sqlite.SqlDao;
import me.whoarym.daocon.model.sqlite.SqliteModelFactory;

public class MainActivity extends Activity {

    @BindView(R.id.sql_opt_switch)
    Switch mSqlSwitch;
    @BindView(R.id.sql_progress)
    TextView mSqlProgress;
    @BindView(R.id.sql_result)
    TextView mSqlResult;

    @BindView(R.id.room_opt_switch)
    Switch mRoomSwitch;
    @BindView(R.id.room_progress)
    TextView mRoomProgress;
    @BindView(R.id.room_result)
    TextView mRoomResult;

    @NonNull
    private SqlDao mSqlTrivialDao;
    @NonNull
    private SqlDao mSqlOptimizedDao;
    @NonNull
    private RoomTrivialDao mRoomTrivialDao;
    @NonNull
    private RoomOptimizedDao mRoomOptimizedDao;

    private boolean mSqlOptimized;
    private boolean mRoomOptimized;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSqlTrivialDao = new SqlDao(((DaoconApp) getApplication()).getSqLiteDb(), false);
        mSqlOptimizedDao = new SqlDao(((DaoconApp) getApplication()).getSqLiteDb(), true);

        RoomDaoDatabase roomDb = ((DaoconApp) getApplication()).getRoomDb();
        mRoomTrivialDao = new RoomTrivialDao(roomDb.getSimpleDao(), roomDb.getBookTrivialDao());
        mRoomOptimizedDao = new RoomOptimizedDao(roomDb.getSimpleDao(), roomDb.getBookOptimizedDao());

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSqlSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> mSqlOptimized = isChecked);
        mRoomSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> mRoomOptimized = isChecked);
    }

    @OnClick(R.id.sql_start)
    public void onSqlStartClick(View view) {
        JsonDataContainer dataContainer =
                new JsonDataContainer(getApplicationContext(), new SqliteModelFactory());
        MeasureListener listener = new MeasureListener() {
            @Override
            public void reportProgress(int progress) {
                StringBuilder builder = new StringBuilder();
                builder.append(progress).append(" / ").append(MeasureAsyncTask.COUNT);
                mSqlProgress.setText(builder);
            }

            @Override
            public void reportResult(@NonNull String report) {
                mSqlProgress.setText("done!");
                mSqlResult.setText(report);
                view.setEnabled(true);
            }
        };
        new MeasureAsyncTask(dataContainer, listener).execute(mSqlOptimized ? mSqlOptimizedDao : mSqlTrivialDao);
        view.setEnabled(false);
    }

    @OnClick(R.id.room_start)
    public void onRoomStartClick(View view) {
        JsonDataContainer dataContainer =
                new JsonDataContainer(getApplicationContext(), new RoomModelFactory());
        MeasureListener listener = new MeasureListener() {
            @Override
            public void reportProgress(int progress) {
                StringBuilder builder = new StringBuilder();
                builder.append(progress).append(" / ").append(MeasureAsyncTask.COUNT);
                mRoomProgress.setText(builder);
            }

            @Override
            public void reportResult(@NonNull String report) {
                mRoomProgress.setText("done!");
                mRoomResult.setText(report);
                view.setEnabled(true);
            }
        };
        new MeasureAsyncTask(dataContainer, listener).execute(mRoomOptimized ? mRoomOptimizedDao : mRoomTrivialDao);
        view.setEnabled(false);
    }
}

package me.whoarym.daocon;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.whoarym.daocon.measure.MeasureAsyncTask;
import me.whoarym.daocon.measure.MeasureListener;
import me.whoarym.daocon.model.json.JsonDataContainer;
import me.whoarym.daocon.model.room.DaoDatabase;
import me.whoarym.daocon.model.room.RoomDao;
import me.whoarym.daocon.model.room.RoomModelFactory;
import me.whoarym.daocon.model.sqlite.SqlDao;
import me.whoarym.daocon.model.sqlite.SqliteModelFactory;

public class MainActivity extends Activity {

    private static final String TAG  = "MainActivity";

    @BindView(R.id.sql_opt_switch)
    Switch mSqlSwitch;
    @BindView(R.id.sql_progress)
    TextView mSqlProgress;
    @BindView(R.id.sql_result)
    TextView mSqlResult;
    @BindView(R.id.room_progress)
    TextView mRoomProgress;
    @BindView(R.id.room_result)
    TextView mRoomResult;

    @NonNull
    private SqlDao mSqlDao;
    @NonNull
    private SqlDao mSqlDaoOptimized;
    @NonNull
    private RoomDao mRoomDao;

    private boolean mSqlOptimized;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSqlDao = new SqlDao(((DaoconApp) getApplication()).getSqLiteDb(), false);
        mSqlDaoOptimized = new SqlDao(((DaoconApp) getApplication()).getSqLiteDb(), true);

        DaoDatabase roomDb = ((DaoconApp) getApplication()).getRoomDb();
        mRoomDao = new RoomDao(roomDb.getSimpleDao(), roomDb.getBookDao());

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSqlSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> mSqlOptimized = isChecked);
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
        new MeasureAsyncTask(dataContainer, listener).execute(mSqlOptimized ? mSqlDaoOptimized : mSqlDao);
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
        new MeasureAsyncTask(dataContainer, listener).execute(mRoomDao);
        view.setEnabled(false);
    }
}

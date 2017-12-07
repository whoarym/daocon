package me.whoarym.daocon;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.whoarym.daocon.measure.MeasureAsyncTask;
import me.whoarym.daocon.measure.MeasureListener;
import me.whoarym.daocon.model.Dao;
import me.whoarym.daocon.model.ModelFactory;
import me.whoarym.daocon.model.json.JsonDataContainer;
import me.whoarym.daocon.model.room.RoomDaoDatabase;
import me.whoarym.daocon.model.room.RoomModelFactory;
import me.whoarym.daocon.model.room.optimized.RoomOptimizedDao;
import me.whoarym.daocon.model.room.trivial.RoomTrivialDao;
import me.whoarym.daocon.model.sqlite.SqliteModelFactory;
import me.whoarym.daocon.model.sqlite.optimized.SqlOptimizedDao;
import me.whoarym.daocon.model.sqlite.trivial.SqlTrivialDao;

public class MainActivity extends Activity {

    @BindView(R.id.dao_spinner)
    Spinner mDaoSpinner;
    @BindView(R.id.start_button)
    Button mStartButton;
    @BindView(R.id.result)
    TextView mResult;

    @NonNull
    private SqlTrivialDao mSqlTrivialDao;
    @NonNull
    private SqlOptimizedDao mSqlOptimizedDao;
    @NonNull
    private RoomTrivialDao mRoomTrivialDao;
    @NonNull
    private RoomOptimizedDao mRoomOptimizedDao;

    private int mSelectedDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDaos();

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dao_contest, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDaoSpinner.setAdapter(adapter);
        mDaoSpinner.setOnItemSelectedListener(mDaoItemsListener);
    }

    private void initDaos() {
        mSqlTrivialDao = new SqlTrivialDao(((DaoconApp) getApplication()).getSqLiteDb());
        mSqlOptimizedDao = new SqlOptimizedDao(((DaoconApp) getApplication()).getSqLiteDb());

        RoomDaoDatabase roomDb = ((DaoconApp) getApplication()).getRoomDb();
        mRoomTrivialDao = new RoomTrivialDao(roomDb.getSimpleDao(), roomDb.getBookTrivialDao());
        mRoomOptimizedDao = new RoomOptimizedDao(roomDb.getSimpleDao(), roomDb.getBookOptimizedDao());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.start_button)
    public void onStartClick(@NonNull Button button) {
        mStartButton.setEnabled(false);
        mResult.setText(null);
        new MeasureAsyncTask(getDataContainer(), mMeasureListener).execute(getDao());
    }

    @NonNull
    private JsonDataContainer getDataContainer() {
        ModelFactory modelFactory;
        switch (mSelectedDao) {
            case 0:
            case 1:
                modelFactory = new SqliteModelFactory();
                break;
            default:
                modelFactory = new RoomModelFactory();
                break;
        }
        return new JsonDataContainer(getApplicationContext(), modelFactory);
    }

    @NonNull
    private Dao getDao() {
        switch (mSelectedDao) {
            case 0:
                return mSqlTrivialDao;
            case 1:
                return mSqlOptimizedDao;
            case 2:
                return mRoomTrivialDao;
            case 3:
                return mRoomOptimizedDao;
        }
        return mSqlTrivialDao;
    }

    private AdapterView.OnItemSelectedListener mDaoItemsListener =
            new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSelectedDao = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            mSelectedDao = 0;
        }
    };

    private MeasureListener mMeasureListener = new MeasureListener() {
        @Override
        public void reportProgress(int progress) {
            StringBuilder builder = new StringBuilder();
            builder.append(progress).append(" / ").append(MeasureAsyncTask.COUNT);
            mStartButton.setText(builder);
        }

        @Override
        public void reportResult(@NonNull String report) {
            mStartButton.setEnabled(true);
            mStartButton.setText(R.string.start_button);
            mResult.setText(report);
        }
    };
}

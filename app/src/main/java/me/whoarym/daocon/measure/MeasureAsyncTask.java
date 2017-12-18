package me.whoarym.daocon.measure;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Dao;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;
import me.whoarym.daocon.model.json.JsonDataContainer;

public class MeasureAsyncTask extends AsyncTask<Dao, Integer, Measure> {
    private static final String TAG = "MeasureAsyncTask";

    public static final int COUNT = 1000;

    @NonNull
    private final JsonDataContainer mDataContainer;
    @NonNull
    private final WeakReference<MeasureListener> mListenerHolder;

    public MeasureAsyncTask(@NonNull JsonDataContainer dataContainer,
                            @NonNull MeasureListener listener) {
        mDataContainer = dataContainer;
        mListenerHolder = new WeakReference<>(listener);
    }

    @Override
    protected Measure doInBackground(Dao... daos) {
        Dao dao = daos[0];

        Measure measure = new Measure(dao.getName());

        for (int i = 0; i < COUNT; i++) {
            if (isCancelled()) {
                Log.d(TAG, "doInBackground(): cancelled");
                break;
            }
            measure.track("Import", () -> dao.importData(mDataContainer));
            measure.track("GetAuthors", () -> {
                List<? extends Author> authors = dao.getAuthors();
                authors.get(0).getName();
            });
            measure.track("GetPublishers", () -> {
                List<? extends Publisher> publishers = dao.getPublishers();
                publishers.get(0).getName();
            });
            measure.track("GetOwners", () -> {
                List<? extends Owner> owners = dao.getOwners();
                owners.get(0).getFirstName();
            });
            measure.track("GetTags", () -> {
                List<? extends Tag> tags = dao.getTags();
                tags.get(0).getName();
            });
            measure.track("GetBooks", () -> {
                List<? extends Book> books = dao.getBooks();
                books.get(0).getAuthor();
                books.get(0).getPublisher();
                books.get(0).getOwner();
            });
            measure.track("Clear", dao::clearData);
            if (!isCancelled()) {
                publishProgress(i);
            }
        }

        return measure;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int val = values[0];
        MeasureListener listener = mListenerHolder.get();
        if (listener != null) {
            listener.reportProgress(val);
        }
    }

    @Override
    protected void onPostExecute(Measure measure) {
        MeasureListener listener = mListenerHolder.get();
        if (listener != null) {
            listener.reportResult(measure.report());
        }
    }
}

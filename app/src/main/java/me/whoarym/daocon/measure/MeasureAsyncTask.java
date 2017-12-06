package me.whoarym.daocon.measure;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.Dao;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;
import me.whoarym.daocon.model.json.JsonDataContainer;

public class MeasureAsyncTask extends AsyncTask<Dao, Integer, Measure> {

    public static final int COUNT = 1000;

    @NonNull
    private final JsonDataContainer mDataContainer;
    @NonNull
    private final MeasureListener mListener;

    public MeasureAsyncTask(@NonNull JsonDataContainer dataContainer,
                            @NonNull MeasureListener listener) {
        mDataContainer = dataContainer;
        mListener = listener;
    }

    @Override
    protected Measure doInBackground(Dao... daos) {
        Dao dao = daos[0];

        Measure measure = new Measure();

        for (int i = 0; i < COUNT; i++) {
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
            publishProgress(i);
        }

        return measure;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int val = values[0];
        mListener.reportProgress(val);
    }

    @Override
    protected void onPostExecute(Measure measure) {
        super.onPostExecute(measure);
        mListener.reportResult(measure.report());
    }

}

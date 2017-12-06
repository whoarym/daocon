package me.whoarym.daocon.model.json;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import okio.Okio;

public class JsonBook {
    private static final String TAG = "Json::SqlBook";

    @Nullable
    static List<JsonBook> load(@NonNull Context context) {
        AssetManager assetManager = context.getAssets();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, JsonBook.class);
        JsonAdapter<List<JsonBook>> adapter = moshi.adapter(type);
        try {
            InputStream inputStream = assetManager.open("books.json");
            List<JsonBook> result = adapter.fromJson(Okio.buffer(Okio.source(inputStream)));
            inputStream.close();
            return result;
        } catch (Throwable t) {
            Log.d(TAG, "load()", t);
        }
        return null;
    }

    private String name;
    private String publisher;
    private String author;
    private String annotation;
    private int year;
    private List<String> tags;

    private JsonBook() {
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getAuthor() {
        return author;
    }

    public String getAnnotation() {
        return annotation;
    }

    public int getYear() {
        return year;
    }

    public List<String> getTags() {
        return tags;
    }
}

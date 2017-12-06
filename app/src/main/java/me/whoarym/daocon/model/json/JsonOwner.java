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

public class JsonOwner {
    private static final String TAG = "Json::SqlOwner";

    @Nullable
    static List<JsonOwner> load(@NonNull Context context) {
        AssetManager assetManager = context.getAssets();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, JsonOwner.class);
        JsonAdapter<List<JsonOwner>> adapter = moshi.adapter(type);
        try {
            InputStream inputStream = assetManager.open("owners.json");
            List<JsonOwner> result = adapter.fromJson(Okio.buffer(Okio.source(inputStream)));
            inputStream.close();
            return result;
        } catch (Throwable t) {
            Log.d(TAG, "load()", t);
        }
        return null;
    }

    private String first_name;
    private String last_name;

    private JsonOwner() {
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}

package me.whoarym.daocon.measure;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Measure {

    private Map<String, List<Long>> mTracks = new LinkedHashMap<>();

    private final String mName;

    Measure(@NonNull String name) {
        mName = name;
    }

    void track(@NonNull String event, @NonNull Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long duration = System.currentTimeMillis() - start;
        if (!mTracks.containsKey(event)) {
            mTracks.put(event, new ArrayList<>(MeasureAsyncTask.COUNT));
        }
        mTracks.get(event).add(duration);
    }

    @NonNull
    String report() {
        StringBuilder builder = new StringBuilder(mName);
        builder.append("\n\n");

        long total = 0L;
        for (Map.Entry<String, List<Long>> entry : mTracks.entrySet()) {
            String event = entry.getKey();
            List<Long> values = entry.getValue();
            Collections.sort(values);

            long min = values.get(0);
            long max = values.get(values.size() - 1);
            long med = values.get(values.size() / 2);
            long eventTotal = 0L;
            for (Long l : values) {
                eventTotal += l;
                total += l;
            }
            builder
                .append(event)
                .append(": m = ")
                .append(med)
                .append(", mm = [")
                .append(min)
                .append(", ")
                .append(max)
                .append("], total = ")
                .append(eventTotal)
                .append("\n");
        }
        builder.append("Total = ").append(total);

        return builder.toString();
    }

}

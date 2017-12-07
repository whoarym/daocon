package me.whoarym.daocon.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

public interface Book {
    int getId();
    void setId(int id);
    @NonNull String getName();
    void setName(@NonNull String name);
    @Nullable String getAnnotation();
    void setAnnotation(@Nullable String annotation);
    int getYear();
    void setYear(int year);
    @NonNull Author getAuthor();
    void setAuthor(@NonNull Author author);
    @NonNull Publisher getPublisher();
    void setPublisher(@NonNull Publisher publisher);
    @NonNull Set<? extends Tag> getTags();
    void setTags(@NonNull Set<? extends Tag> tags);
    @Nullable Owner getOwner();
    void setOwner(@Nullable Owner owner);
}

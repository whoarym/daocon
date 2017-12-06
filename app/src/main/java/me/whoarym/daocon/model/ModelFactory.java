package me.whoarym.daocon.model;

import android.support.annotation.NonNull;

public interface ModelFactory {
    @NonNull Author createAuthor();
    @NonNull Publisher createPublisher();
    @NonNull Owner createOwner();
    @NonNull Tag createTag();
    @NonNull Book createBook();
}

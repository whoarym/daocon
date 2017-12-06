package me.whoarym.daocon.model.json;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import me.whoarym.daocon.model.Author;
import me.whoarym.daocon.model.Book;
import me.whoarym.daocon.model.ModelFactory;
import me.whoarym.daocon.model.Owner;
import me.whoarym.daocon.model.Publisher;
import me.whoarym.daocon.model.Tag;

public class JsonDataContainer {

    private static final float OWNER_PROBABILITY = 0.6f;

    @NonNull
    private final List<Tag> mTags;
    @NonNull
    private final List<Author> mAuthors;
    @NonNull
    private final List<Publisher> mPublishers;
    @NonNull
    private final List<Owner> mOwners;
    @NonNull
    private final List<Book> mBooks;

    public JsonDataContainer(@NonNull Context context,
                             @NonNull ModelFactory factory) {
        List<JsonBook> books = JsonBook.load(context);
        List<JsonOwner> owners = JsonOwner.load(context);

        if (books == null || owners == null) {
            throw new RuntimeException("Import error");
        }

        // fill owners
        mOwners = new ArrayList<>(owners.size());
        for (int i = 0; i < owners.size(); i++) {
            Owner sqlOwner = factory.createOwner();
            sqlOwner.setId(i);
            sqlOwner.setFirstName(owners.get(i).getFirstName());
            sqlOwner.setLastName(owners.get(i).getLastName());
            mOwners.add(sqlOwner);
        }

        Map<String, Tag> modelTagMap = new HashMap<>();
        Map<String, Author> modelAuthorMap = new HashMap<>();
        Map<String, Publisher> modelPublisherMap = new HashMap<>();

        // fill books
        mBooks = new ArrayList<>(books.size());
        for (int i = 0; i < books.size(); i++) {
            JsonBook jsonBook = books.get(i);

            // fill simple book properties
            Book modelBook = factory.createBook();
            modelBook.setId(i);
            modelBook.setName(jsonBook.getName());
            modelBook.setAnnotation(jsonBook.getAnnotation());
            modelBook.setYear(jsonBook.getYear());

            // fill author
            if (!modelAuthorMap.containsKey(jsonBook.getAuthor())) {
                Author modelAuthor = factory.createAuthor();
                modelAuthor.setName(jsonBook.getAuthor());
                modelAuthorMap.put(jsonBook.getAuthor(), modelAuthor);
            }
            modelBook.setAuthor(modelAuthorMap.get(jsonBook.getAuthor()));

            // fill publisher
            if (!modelPublisherMap.containsKey(jsonBook.getPublisher())) {
                Publisher modelPublisher = factory.createPublisher();
                modelPublisher.setName(jsonBook.getPublisher());
                modelPublisherMap.put(jsonBook.getPublisher(), modelPublisher);
            }
            modelBook.setPublisher(modelPublisherMap.get(jsonBook.getPublisher()));

            // fill tags
            Set<Tag> bookTags = new HashSet<>();
            for (String tag : jsonBook.getTags()) {
                if (!modelTagMap.containsKey(tag)) {
                    Tag sqlTag = factory.createTag();
                    sqlTag.setName(tag);
                    modelTagMap.put(tag, sqlTag);
                }
                bookTags.add(modelTagMap.get(tag));
            }
            modelBook.setTags(bookTags);

            // set owner with some probability
            if (Math.random() > OWNER_PROBABILITY) {
                int ownerId = ThreadLocalRandom.current().nextInt(mOwners.size());
                modelBook.setOwner(mOwners.get(ownerId));
            }

            mBooks.add(modelBook);
        }

        // fill tags and tag ids
        mTags = new ArrayList<>(modelTagMap.values());
        for (int i = 0; i < mTags.size(); i++) {
            mTags.get(i).setId(i);
        }

        // fill authors and author ids
        mAuthors = new ArrayList<>(modelAuthorMap.values());
        for (int i = 0; i < mAuthors.size(); i++) {
            mAuthors.get(i).setId(i);
        }

        // fill publishers and publisher ids
        mPublishers = new ArrayList<>(modelPublisherMap.values());
        for (int i = 0; i < mPublishers.size(); i++) {
            mPublishers.get(i).setId(i);
        }
    }

    @NonNull
    public List<? extends Tag> getTags() {
        return mTags;
    }

    @NonNull
    public List<? extends Author> getAuthors() {
        return mAuthors;
    }

    @NonNull
    public List<? extends Publisher> getPublishers() {
        return mPublishers;
    }

    @NonNull
    public List<? extends Owner> getOwners() {
        return mOwners;
    }

    @NonNull
    public List<? extends Book> getBooks() {
        return mBooks;
    }
}

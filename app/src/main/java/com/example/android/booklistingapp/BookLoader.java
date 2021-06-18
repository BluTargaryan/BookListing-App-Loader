package com.example.android.booklistingapp;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 * Load a list of books using AsyncTask toperform  network request to url
 */
public class BookLoader extends AsyncTaskLoader<List<Book>> {
    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL*/
private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    /**
     * This is on a background thread.
     */

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        List<Book> books = Utils.fetchBookData(mUrl);
        return books;
    }


}

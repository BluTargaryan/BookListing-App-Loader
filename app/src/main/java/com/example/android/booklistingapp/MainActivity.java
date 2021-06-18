package com.example.android.booklistingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    //Value for log tag
    public static final String LOG_TAG = MainActivity.class.getName();

private boolean isConnected;
    private BookAdapter mAdapter;
    private String searchstring ;
    private View loadingIndicator ;
    public String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=&key=AIzaSyBj-DvovphccjcXKS89r3Z_TA9gSMumulc";

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a new loader for the given URL
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        // Set empty state text
        mEmptyStateTextView.setText("Search for a real book.");
        // Hide loading indicator because the data has been loaded
       loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.listview);
//Set empty textview up
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        //Find a reference to the edittext and button in the layout
        final EditText text = (EditText) findViewById(R.id.search_main);
        Button button = (Button) findViewById(R.id.search_button);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = LoaderManager.getInstance(this);
        /**
         * To check for network connection
         **/
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            Log.e(LOG_TAG, "Loader initialized");
        }else{
            loadingIndicator = findViewById(R.id.loading_indicator);
            //Setting progress bar visibility to none
            loadingIndicator.setVisibility(View.GONE);
            // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText("No internet connection.");
        }





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start loading indicator because the data has not been loaded
                loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);
                // Set empty state text to nothing so as to make app look better
                mEmptyStateTextView.setText(" ");
                //Value for user search input
                searchstring = text.getText().toString();
                USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + searchstring + "&key=AIzaSyBj-DvovphccjcXKS89r3Z_TA9gSMumulc";
                Log.e(LOG_TAG, searchstring);
                //restart loader
               loaderManager.restartLoader(BOOK_LOADER_ID,null,MainActivity.this);
            }
        });



        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }
}

/**
 * TO trigger need to commit
 */




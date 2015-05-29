package com.shahar.eldad.webclipstoringtone;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by shahar on 5/29/2015.
 */
public class retrieveFeedTask extends AsyncTask<String, Void, Document> {

    private static final String TAG = "retrieveFeedTask";

    private Exception exception;
    private FragmentActivity _activity;

    public retrieveFeedTask(FragmentActivity activity) {

        _activity = activity;
    }

    protected Document doInBackground(String... urls) {

        Log.d(TAG, "doInBackground.start");

        String urlToSearch = urls[0];
        Document document = null;
        Log.d(TAG, "urlToSearch: " + urlToSearch);

        try {
            document = Jsoup.connect(urlToSearch).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document == null) {
            Toast.makeText(_activity, _activity.getString(R.string.SearchYoutubeFailed), Toast.LENGTH_SHORT).show();
            return null;
        }

        return document;
    }

    protected void onPostExecute(Document document) {

        Log.d(TAG, "onPostExecute.start");

        parseResponse(document);
    }

    private void parseResponse(Document document){

        Log.d(TAG, "parseResponse.start");

        Toast.makeText(_activity, document.select("title").text(), Toast.LENGTH_SHORT).show();
        Toast.makeText(_activity, _activity.getString(R.string.NotImplementedException), Toast.LENGTH_SHORT).show();
    }
}

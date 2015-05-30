package com.shahar.eldad.webclipstoringtone;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

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

        Elements liElements = getLiElements(document);

        Iterator<Element> liElementsIterator = liElements.iterator();
        while (liElementsIterator.hasNext()){
            VideoModel model = CreateVideoModel(liElementsIterator.next());
        }

        Toast.makeText(_activity, _activity.getString(R.string.NotImplementedException), Toast.LENGTH_SHORT).show();
    }

    private Elements getLiElements(Document document) {
        Elements resultsDiv = document.select("div#results");
        Elements olItemSelection = resultsDiv.select("ol.item-section");
        return olItemSelection.select("li");
    }

    private VideoModel CreateVideoModel(Element element) {

        Log.d(TAG, "CreateVideoModel.start");

        VideoModel model = new VideoModel();

        Log.d(TAG, "CreateVideoModel.VideoModel created");
        
        Element imgElement = element.select("imgElement").first();
        if (imgElement != null){
            model.setThumbnail(imgElement.attr("src"));
        }

        Log.d(TAG, "CreateVideoModel.passed imgElement");

        Element aTitleElement = element.select(".yt-lockup-title").select("aTitleElement").first();
        if (aTitleElement != null){
            model.setTitle(aTitleElement.text());
            model.setVideoId(aTitleElement.attr("href"));
        }

        Log.d(TAG, "CreateVideoModel.passed aTitleElement");

        Log.d(TAG, "CreateVideoModel.model.title: " + model.getTitle());

        return model;
    }
}

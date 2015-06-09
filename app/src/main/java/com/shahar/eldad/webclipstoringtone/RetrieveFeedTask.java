package com.shahar.eldad.webclipstoringtone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shahar on 5/29/2015.
 */
public class RetrieveFeedTask extends AsyncTask<String, Void, List<SearchResult>> {

    private static final String TAG = "RetrieveFeedTask";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;

    private SearchListFragment mFragmentActivity;
    ProgressDialog pdLoading;

    public RetrieveFeedTask(SearchListFragment activity) {

        mFragmentActivity = activity;
        pdLoading = new ProgressDialog(mFragmentActivity.getActivity());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    protected List<SearchResult> doInBackground(String... searchStrings) {

        Log.d(TAG, "doInBackground.start");

        String queryTerm = searchStrings[0];
        Log.d(TAG, "queryTerm: " + queryTerm);

        List<SearchResult> searchResultList = null;

        try {
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey("AIzaSyD2x4lnEZ41R08vElsmwElaZG9zIlZrPMQ");
            search.setQ(queryTerm);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            searchResultList = searchResponse.getItems();

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return searchResultList;
    }

    protected void onPostExecute(List<SearchResult> searchResultList) {

        Log.d(TAG, "onPostExecute.start");

        if (searchResultList != null) {
            List<VideoModel> VideoModels = parseResponse(searchResultList.iterator());
            setResultInListView(VideoModels);
        }
        pdLoading.dismiss();
    }

    private void setResultInListView(List<VideoModel> VideoModels) {

        Log.d(TAG, "setResultInListView.start");
        mFragmentActivity.populateAdapterWithVideoModel(VideoModels);
    }

    private List<VideoModel> parseResponse(Iterator<SearchResult> iteratorSearchResults){

        Log.d(TAG, "parseResponse.start");

        List<VideoModel> collection = new ArrayList<>();

        while (iteratorSearchResults.hasNext()){
            VideoModel model = CreateVideoModel(iteratorSearchResults.next());
            collection.add(model);
        }

        return collection;
    }

    private VideoModel CreateVideoModel(SearchResult result) {

        Log.d(TAG, "CreateVideoModel.start");

        VideoModel model = new VideoModel();

        Log.d(TAG, "CreateVideoModel.VideoModel created");

        model.setVideoId(result.getId().getVideoId());
        model.setThumbnail(result.getSnippet().getThumbnails().getDefault().getUrl());
        model.setTitle(result.getSnippet().getTitle());

        Log.d(TAG, "CreateVideoModel.model.id: " + model.getVideoId() + ", title: " + model.getTitle());

        return model;
    }
}
package com.shahar.eldad.Cliptone;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.util.List;

public class SearchListFragment extends Fragment {

    private static final String TAG = "SearchListFragment";

    // File url to download
    private static String file_url = "http://youtubeinmp3.com/fetch/?video=http://www.youtube.com/watch?v=";

    private EditText mSearchStringEditText;
    private VideoModelAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView.Started");

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated.start");

        mAdapter = new VideoModelAdapter(getActivity(), R.layout.video_item);

        mSearchStringEditText = (EditText)getActivity().findViewById(R.id.searchStringEditText);
        ListView mResultListView = (ListView) getActivity().findViewById(R.id.resultListView);

        mResultListView.setAdapter(mAdapter);

        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG, "onActivityCreated.mResultListView.onClick.start");

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);

                TextView videoItemTitleTextView = (TextView) view.findViewById(R.id.videoItemTitleTextView);
                VideoModel model = (VideoModel) videoItemTitleTextView.getTag();
                new DownloadFileFromURL(SearchListFragment.this, model).execute(file_url + model.getVideoId());

                //DownloadFileUsingDownloadManager(model);
            }

        });

        Button mSearchButton = (Button) getActivity().findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onActivityCreated.mSearchButton.onClick.start");

                String searchKeyWords = mSearchStringEditText.getText().toString();
                if (searchKeyWords == null || searchKeyWords.isEmpty())
                    return;

                if (isOnline() == false){
                    ShowAlertDialogNoConnection();
                    return;
                }

                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(SearchListFragment.this);

                retrieveFeedTask.execute(searchKeyWords);
            }
        });
    }

    private void DownloadFileUsingDownloadManager(VideoModel model) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file_url + model.getVideoId()));
        request.setDescription("MP3 ringtone download");
        request.setTitle(model.getTitle());
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        String fileName = model.getTitle().replaceAll("[^à-úa-zA-Z0-9.-]", "_");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_RINGTONES, fileName + ".mp3");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void ShowAlertDialogNoConnection() {

        Log.d(TAG, "ShowAlertDialogNoConnection.Started");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.NoInternetConnection))
                .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG, "ShowAlertDialogNoConnection.setNeutralButton.Clicked");

                        return;
                    }
                }).show();
    }

    public boolean isOnline() {

        Log.d(TAG, "isOnline.Started");

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void populateAdapterWithVideoModel(List<VideoModel> collection){

        Log.d(TAG, "populateAdapterWithVideoModel.start");

        Log.d(TAG, "collection.size: " + collection.size());
        mAdapter.clear();
        mAdapter.addAll(collection);

        Log.d(TAG, "notifyDataSetChanged.calling");
        mAdapter.notifyDataSetChanged();
    }
}

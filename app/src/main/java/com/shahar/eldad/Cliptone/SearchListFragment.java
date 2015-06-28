package com.shahar.eldad.Cliptone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            }

        });

        Button mSearchButton = (Button) getActivity().findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onActivityCreated.mSearchButton.onClick.start");

                if (isOnline() == false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.SetDefaultRingtoneDialog))
                            .setNeutralButton(getString(R.string.NoInternetConnection), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                    return;
                }


                String searchKeyWords = mSearchStringEditText.getText().toString();

                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(SearchListFragment.this);

                retrieveFeedTask.execute(searchKeyWords);
            }
        });
    }

    public boolean isOnline() {
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

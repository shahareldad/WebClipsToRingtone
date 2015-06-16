package com.shahar.eldad.webclipstoringtone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

//        mResultListView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                Log.d(TAG, "onActivityCreated.mResultListView.onClick.start");
//
//                TextView videoItemTitleTextView = (TextView) view.findViewById(R.id.videoItemTitleTextView);
//                VideoModel model = (VideoModel) videoItemTitleTextView.getTag();
//                new DownloadFileFromURL(SearchListFragment.this, model).execute(file_url + model.getVideoId());
//            }
//        });

        Button mSearchButton = (Button) getActivity().findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onActivityCreated.mSearchButton.onClick.start");

                String searchKeyWords = mSearchStringEditText.getText().toString();

                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(SearchListFragment.this);

                retrieveFeedTask.execute(searchKeyWords);
            }
        });
    }

    public void populateAdapterWithVideoModel(List<VideoModel> collection){

        Log.d(TAG, "populateAdapterWithVideoModel.start");

        Log.d(TAG, "collection.size: " + collection.size());
        mAdapter.addAll(collection);

        Log.d(TAG, "notifyDataSetChanged.calling");
        //mAdapter.notifyDataSetChanged();
    }
}

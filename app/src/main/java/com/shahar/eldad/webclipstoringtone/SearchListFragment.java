package com.shahar.eldad.webclipstoringtone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by shahar on 6/2/2015.
 */
public class SearchListFragment extends Fragment {

    private static final String TAG = "SearchListFragment";

    private EditText mSearchStringEditText;
    private Button mSearchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated.start");

        mSearchStringEditText = (EditText)getActivity().findViewById(R.id.searchStringEditText);
        mSearchButton = (Button)getActivity().findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyWords = mSearchStringEditText.getText().toString();

                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(SearchListFragment.this);

                retrieveFeedTask.execute(searchKeyWords);
            }
        });
    }

    public void populateAdapterWithVideoModel(List<VideoModel> collection){

        Log.d(TAG, "populateAdapterWithVideoModel.start");


    }
}

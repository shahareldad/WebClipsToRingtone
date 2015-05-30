package com.shahar.eldad.webclipstoringtone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private SearchListFragment mSearchListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate.start");

        mSearchListFragment = new SearchListFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mSearchListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SearchListFragment extends Fragment {

        private static final String TAG = "SearchListFragment";

//        String youtubeUrl ="https://m.youtube.com/";
//        String url ="results?q=%s&sm=3";
        String youtubeUrl ="https://www.youtube.com/";
        String url ="results?search_query=%s";

        private EditText mSearchStringEditText;
        private Button mSearchButton;
        private TextView mTextViewTemp;

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
            mTextViewTemp = (TextView)getActivity().findViewById(R.id.textViewTemp);

            mSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchKeyWords = mSearchStringEditText.getText().toString().replace(' ', '+');

                    retrieveFeedTask retrieveFeedTask = new com.shahar.eldad.webclipstoringtone.retrieveFeedTask(getActivity());

                    retrieveFeedTask.execute(String.format(youtubeUrl + url, searchKeyWords));
                }
            });
        }
    }
}

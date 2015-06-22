package com.shahar.eldad.webclipstoringtone;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private static final String TAG = "DownloadFileFromURL";
    private SearchListFragment mSearchListFragment;
    private VideoModel mModel;
    private boolean mDownloadSuccess = true;

    public DownloadFileFromURL(SearchListFragment searchListFragment, VideoModel model) {

        Log.d(TAG, "DownloadFileFromURL_ctor.Started");

        mSearchListFragment = searchListFragment;
        mModel = model;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {

        Log.d(TAG, "onPreExecute.Started");

        super.onPreExecute();
        Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadStarted), Toast.LENGTH_SHORT).show();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {

        Log.d(TAG, "doInBackground.Started");

        int count;
        try {
            mDownloadSuccess = true;
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a typical 0-100%
            // progress bar
            int lengthOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            File file = new File(path, mModel.getTitle() + ".mp3");

            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            mDownloadSuccess = false;
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {

        Log.d(TAG, "onProgressUpdate.Started");

        // setting progress percentage
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {

        Log.d(TAG, "onPostExecute.Started");

        // dismiss the dialog after the file was downloaded
        if (mDownloadSuccess)
            Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadOver), Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadFailed), Toast.LENGTH_SHORT).show();
        }
    }

}

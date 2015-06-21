package com.shahar.eldad.webclipstoringtone;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {


    private SearchListFragment mSearchListFragment;
    private VideoModel mModel;

    public DownloadFileFromURL(SearchListFragment searchListFragment, VideoModel model) {

        mSearchListFragment = searchListFragment;
        mModel = model;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadStarted), Toast.LENGTH_SHORT).show();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
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
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            OutputStream output = new FileOutputStream(file + "/" + mModel.getTitle() + ".mp3");

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
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadOver), Toast.LENGTH_SHORT).show();

    }

}

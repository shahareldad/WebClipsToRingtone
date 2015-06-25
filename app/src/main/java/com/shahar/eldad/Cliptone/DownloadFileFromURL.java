package com.shahar.eldad.Cliptone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private File mFile;
    private int mLengthOfFile;

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
//        Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadStarted), Toast.LENGTH_SHORT).show();

        StartProgressDialog();
    }

    private void StartProgressDialog() {
        pDialog = new ProgressDialog(mSearchListFragment.getActivity());
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
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
            mLengthOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            mFile = new File(path, mModel.getTitle() + ".mp3");

            OutputStream output = new FileOutputStream(mFile);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / mLengthOfFile));

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
        pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {

        Log.d(TAG, "onPostExecute.Started");

        // dismiss the dialog after the file was downloaded
        if (mDownloadSuccess == false)
            Toast.makeText(mSearchListFragment.getActivity(), mSearchListFragment.getString(R.string.DownloadFailed), Toast.LENGTH_SHORT).show();
        else{

            ScanNewDownloadedFile();

            DialogInterface.OnClickListener dialogClickListener = GetDialogClickListener();

            DisplaySetDefaultRingtonePopup(dialogClickListener);
        }

        pDialog.dismiss();
    }

    private void ScanNewDownloadedFile() {
        MediaScannerConnection.scanFile(
                mSearchListFragment.getActivity(),
                new String[]{mFile.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private void DisplaySetDefaultRingtonePopup(DialogInterface.OnClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mSearchListFragment.getActivity());
        builder.setMessage(mSearchListFragment.getString(R.string.SetDefaultRingtoneDialog))
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private DialogInterface.OnClickListener GetDialogClickListener() {
        return new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SetDefaultRingtone();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                // do nothing
                                break;
                        }
                    }
                };
    }

    private void SetDefaultRingtone(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, mFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, mModel.getTitle());
        values.put(MediaStore.MediaColumns.SIZE, mLengthOfFile);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "");
        values.put(MediaStore.Audio.Media.DURATION, 300);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

//Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(mFile.getAbsolutePath());
        Uri newUri = mSearchListFragment.getActivity().getContentResolver().insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(
                mSearchListFragment.getActivity(),
                RingtoneManager.TYPE_RINGTONE,
                newUri
        );
    }
}

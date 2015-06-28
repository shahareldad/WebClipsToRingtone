package com.shahar.eldad.Cliptone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadImageTask";
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {

        Log.d(TAG, "DownloadImageTask.Started");

        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {

        Log.d(TAG, "doInBackground.Started");

        String url = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        Log.d(TAG, "onPostExecute.Started");

        bmImage.setImageBitmap(result);
    }
}

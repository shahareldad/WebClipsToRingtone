package com.shahar.eldad.webclipstoringtone;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoModelAdapter extends ArrayAdapter<VideoModel> {

    private static final String TAG = "VideoModelAdapter";

    public VideoModelAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView.start");

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.video_item, parent, false);
        }

        VideoModel model = getItem(position);

        ImageView videoItemThumbnailImageView = (ImageView)v.findViewById(R.id.videoItemThumbnailImageView);
        videoItemThumbnailImageView.setImageURI(Uri.parse(model.getThumbnail()));

        TextView videoItemTitleTextView = (TextView)v.findViewById(R.id.videoItemTitleTextView);
        videoItemTitleTextView.setText(model.getTitle());
        videoItemTitleTextView.setTag(model);

        return super.getView(position, convertView, parent);
    }
}

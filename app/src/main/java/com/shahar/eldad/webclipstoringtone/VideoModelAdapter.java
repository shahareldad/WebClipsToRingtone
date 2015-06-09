package com.shahar.eldad.webclipstoringtone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by se250071 on 03/06/2015.
 */
public class VideoModelAdapter extends ArrayAdapter<VideoModel> {

    public VideoModelAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }
}

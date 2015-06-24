package com.shahar.eldad.Cliptone;

public class VideoModel {

    private String mThumbnail;
    private String mTitle;
    private String mVideoId;

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setThumbnail(String imageSource) {
        mThumbnail = imageSource;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }
}

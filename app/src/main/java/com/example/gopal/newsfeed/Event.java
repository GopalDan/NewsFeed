package com.example.gopal.newsfeed;

/**
 * Created by Gopal on 11/1/2018.
 */

public class Event {
    private String mWebTitle;
    private String mThumbnailResourceId;
    private String mWebUrl;
    private String mAuthor;
    private String mPublishingDate;
    public Event(String webTitle, String thumbnailResourceId,String webUrl, String author, String publishingDate){
        mWebTitle = webTitle;
        mThumbnailResourceId = thumbnailResourceId;
        mWebUrl = webUrl;
        mAuthor = author;
        mPublishingDate = publishingDate;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmThumbnailResourceId() {
        return mThumbnailResourceId;
    }

    public String getmWebUrl() {return mWebUrl;}

    public String getmAuthor() {return mAuthor;}

    public String getmPublishingDate() {return mPublishingDate;}
}

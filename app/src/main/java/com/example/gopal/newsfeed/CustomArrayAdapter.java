package com.example.gopal.newsfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gopal on 11/1/2018.
 */

public class CustomArrayAdapter extends ArrayAdapter<Event>{
    private String mPublishedDate;
    public CustomArrayAdapter(Context context, ArrayList<Event> news){
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Event currentEvent = getItem(position);
        TextView webTitle = listItemView.findViewById(R.id.web_title);
        webTitle.setText(currentEvent.getmWebTitle());
        ImageView imageView = listItemView.findViewById(R.id.thumbnail);
        TextView author = listItemView.findViewById(R.id.author);
        String authorName = "By "+ currentEvent.getmAuthor();
        author.setText(authorName);
        TextView publishedDateView = listItemView.findViewById(R.id.published_date);
        String date = currentEvent.getmPublishingDate();
        mPublishedDate = dateFormatter(date);
        publishedDateView.setText(mPublishedDate);

        Picasso.get().load(currentEvent.getmThumbnailResourceId())
                .into(imageView);

        return listItemView;
    }
    private String dateFormatter(String dateInString) {

        //Specifying the pattern of input date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        //   String dateString = "22-03-2017 11:18:32";
        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(dateInString.replaceAll("Z$", "+0000 "));
            String[] dateArray = date.toString().split("\\s");
            mPublishedDate = dateArray[2] + "-" + dateArray[1];

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mPublishedDate;
    }
}




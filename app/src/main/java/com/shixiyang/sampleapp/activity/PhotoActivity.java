package com.shixiyang.sampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.shixiyang.sampleapp.R;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = ThumbnailActivity.class.getName();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private String id;
    private String albumId;
    private String title;
    private String url;
    private ImageView photoImageView;
    private TextView albumTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //Get albumId and photoId from ThumbnailActivity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id = extras.getString("id");
            albumId = extras.getString("albumId");
            title = extras.getString("title");
            url = extras.getString("url");
        }

        this.setTitle("Album ID: " + albumId + "    " +
                "Photo ID: " + id);
        photoImageView = findViewById(R.id.image_photo);
        albumTitleTextView = findViewById(R.id.text_album_title);
        albumTitleTextView.setText(title);

        //Load image using Glide
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build());
        Glide.with(getApplicationContext()).load(glideUrl).into(photoImageView);
    }
}
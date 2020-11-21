package com.shixiyang.sampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import com.shixiyang.sampleapp.viewmodel.MainViewModel;
import com.shixiyang.sampleapp.viewmodel.PhotoViewModel;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = ThumbnailActivity.class.getName();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private ImageView photoImageView;
    private TextView albumTitleTextView;
    private PhotoViewModel photoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        //Get albumId and photoId from ThumbnailActivity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            photoViewModel.id = extras.getString("id");
            photoViewModel.albumId = extras.getString("albumId");
            photoViewModel.title = extras.getString("title");
            photoViewModel.url = extras.getString("url");
        }

        this.setTitle("Album ID: " + photoViewModel.albumId + "    " +
                "Photo ID: " + photoViewModel.id);
        photoImageView = findViewById(R.id.image_photo);
        albumTitleTextView = findViewById(R.id.text_album_title);
        albumTitleTextView.setText(photoViewModel.title);

        //Load image using Glide
        GlideUrl glideUrl = new GlideUrl(photoViewModel.url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build());
        Glide.with(getApplicationContext()).load(glideUrl).into(photoImageView);
    }
}
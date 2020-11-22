package com.shixiyang.sampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shixiyang.sampleapp.R;
import com.shixiyang.sampleapp.adapters.AlbumsListAdapter;
import com.shixiyang.sampleapp.model.Album;
import com.shixiyang.sampleapp.model.User;
import com.shixiyang.sampleapp.util.Service;
import com.shixiyang.sampleapp.viewmodel.MainViewModel;
import com.shixiyang.sampleapp.viewmodel.ThumbnailViewModel;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThumbnailActivity extends AppCompatActivity {

    private static final String TAG = ThumbnailActivity.class.getName();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private ListView albumsListView;
    private ThumbnailViewModel thumbnailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);
        thumbnailViewModel = ViewModelProviders.of(this).get(ThumbnailViewModel.class);
        albumsListView = findViewById(R.id.list_albums);

        if (thumbnailViewModel.userId == null) {
            //Get clicked id from MainActivity
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                thumbnailViewModel.userId = extras.getString("id");
            }
        }

        this.setTitle("Album ID: " + thumbnailViewModel.userId);

        if (thumbnailViewModel.albumsList.size() == 0) {
            getAlbumsInfo();
        }
    }

    public void getAlbumsInfo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service service = retrofit.create(Service.class);
        Call<List<Album>> albumsCall = service.getAlbums(thumbnailViewModel.userId);

        albumsCall.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<Album> albums = response.body();

                for (Album album : albums) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("albumId", Integer.toString(album.getAlbumId()));
                    map.put("id", Integer.toString(album.getId()));
                    map.put("title", album.getTitle());
                    map.put("url", album.getUrl());
                    map.put("thumbnailUrl", album.getThumbnailUrl());
                    thumbnailViewModel.albumsList.add(map);
                }

                if (thumbnailViewModel.albumsList.size() > 0) {
                    //Update ListView
                    AlbumsListAdapter adapter = new AlbumsListAdapter(thumbnailViewModel.albumsList, getApplicationContext());
                    albumsListView.setAdapter(adapter);

                    //Set onClick Listener
                    albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                            HashMap<String, String> selectedThumbnail = (HashMap<String, String>) adapter.getItem(position);
                            intent.putExtra("id", selectedThumbnail.get("id"));
                            intent.putExtra("albumId", selectedThumbnail.get("albumId"));
                            intent.putExtra("title", selectedThumbnail.get("title"));
                            intent.putExtra("url", selectedThumbnail.get("url"));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to get user info: " + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }
}
package com.shixiyang.sampleapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.shixiyang.sampleapp.R;
import com.shixiyang.sampleapp.activity.ThumbnailActivity;
import com.shixiyang.sampleapp.model.Album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumsListAdapter extends ArrayAdapter<HashMap<String, String>> {

    private static final String TAG = AlbumsListAdapter.class.getName();
    private ArrayList<HashMap<String, String>> albums;
    Context mContext;

    private static class ViewHolder {
        TextView textTitle;
        ImageView thumbnailImageView;
    }

    public AlbumsListAdapter(ArrayList<HashMap<String, String>> data, Context context) {
        super(context, R.layout.list_item_album, data);
        this.albums = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HashMap<String, String> map = getItem(position);
        ViewHolder viewHolder;
        View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_album, parent, false);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.text_album_title);
            viewHolder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.image_thumbnail);
            result = convertView;
            result.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.textTitle.setText(map.get("title"));
        //Load image using Glide
        String thumbnailUrl = this.albums.get(position).get("thumbnailUrl");
        GlideUrl url = new GlideUrl(thumbnailUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build());
        Glide.with(mContext).load(url).into(viewHolder.thumbnailImageView);

        return convertView;
    }
}

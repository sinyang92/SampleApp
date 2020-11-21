package com.shixiyang.sampleapp.util;

import com.shixiyang.sampleapp.model.Album;
import com.shixiyang.sampleapp.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("photos")
    Call<List<Album>> getAlbums(@Query("albumId") String albumId);
}

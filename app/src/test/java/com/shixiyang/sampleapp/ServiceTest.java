package com.shixiyang.sampleapp;

import com.shixiyang.sampleapp.model.Album;
import com.shixiyang.sampleapp.model.User;
import com.shixiyang.sampleapp.util.Service;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceTest {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @Test
    public void getUsers_getsUsersList() {
        // Arrange
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service service = retrofit.create(Service.class);
        Call<List<User>> usersCall = service.getUsers();

        // Act
        try {
            Response<List<User>> response = usersCall.execute();
            List<User> users = response.body();

            // Assert
            assertTrue(response.isSuccessful());
            assertEquals(users.size(), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAlbums_validParameter_getsAlbumsList() {
        // Arrange
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service service = retrofit.create(Service.class);
        Call<List<Album>> albumsCall = service.getAlbums("3");

        // Act
        try {
            Response<List<Album>> response = albumsCall.execute();
            List<Album> albums = response.body();

            // Assert
            assertTrue(response.isSuccessful());
            assertEquals(albums.size(), 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAlbums_albumIdInvalid_getsEmptyAlbumsList() {
        // Arrange
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service service = retrofit.create(Service.class);
        Call<List<Album>> albumsCall = service.getAlbums("8888");

        // Act
        try {
            Response<List<Album>> response = albumsCall.execute();
            List<Album> albums = response.body();

            // Assert
            assertTrue(response.isSuccessful());
            assertEquals(albums.size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

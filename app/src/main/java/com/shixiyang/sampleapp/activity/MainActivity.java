package com.shixiyang.sampleapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shixiyang.sampleapp.R;
import com.shixiyang.sampleapp.model.User;
import com.shixiyang.sampleapp.util.Service;
import com.shixiyang.sampleapp.viewmodel.MainViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String USER_INFO = "User Info";
    private ListView usersListView;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(USER_INFO);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        usersListView = findViewById(R.id.list_users);
        getUsersInfo();
    }

    public void getUsersInfo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service service = retrofit.create(Service.class);
        Call<List<User>> usersCall = service.getUsers();

        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<User> users = response.body();

                for (User user : users) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", Integer.toString(user.getId()));
                    map.put("name", user.getName());
                    map.put("email", user.getEmail());
                    map.put("phone", user.getPhone());
                    mainViewModel.usersList.add(map);
                }

                if (mainViewModel.usersList.size() > 0) {
                    //Update ListView
                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, mainViewModel.usersList, R.layout.list_item_user, new String[]{"id", "name", "email", "phone"},
                            new int[]{R.id.text_id, R.id.text_name, R.id.text_email, R.id.text_phone}
                    );
                    usersListView.setAdapter(adapter);

                    //Set onClick Listener
                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), ThumbnailActivity.class);
                            HashMap<String, String> selectedUser = (HashMap<String, String>) adapter.getItem(position);
                            intent.putExtra("id", selectedUser.get("id"));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to get user info: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
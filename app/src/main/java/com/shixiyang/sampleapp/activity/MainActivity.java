package com.shixiyang.sampleapp.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shixiyang.sampleapp.R;
import com.shixiyang.sampleapp.model.User;
import com.shixiyang.sampleapp.util.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    private ListView usersListView;
    ArrayList<HashMap<String, String>> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersList = new ArrayList<>();
        usersListView = findViewById(R.id.list_users);

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
                    usersList.add(map);
                }

                //Update ListView
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this, usersList, R.layout.list_item_user, new String[]{"id", "name", "email", "phone"},
                        new int[]{R.id.text_id, R.id.text_name, R.id.text_email, R.id.text_phone}
                );
                usersListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
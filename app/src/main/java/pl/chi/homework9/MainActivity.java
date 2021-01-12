package pl.chi.homework9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.screenlife.com/api/get-channels";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String channel_array  = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(channel_array);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String channelId = object.getString("id");
                            String title = object.getString("title");
                            String titleLocalized = object.getString("titleLocalized");
                            arrayList.add(channelId + " " + title + "  " + titleLocalized );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        });

        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), arrayList.get(position),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, VideosActivity.class));
            }
        });


    }
}
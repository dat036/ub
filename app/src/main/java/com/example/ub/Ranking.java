package com.example.ub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity {
    private String urlGetUser = "http://uberwaste.000webhostapp.com/file_php/getUser.php";
    ArrayList<User> arrayUser;
    ListView lvUser;
    RankingDataAdapter rankingDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        lvUser = (ListView) findViewById(R.id.listviewUser);
        arrayUser = new ArrayList<>();
        getData(urlGetUser);
//        new getData1();
        loadData();
        Log.d("CC", "1");
        rankingDataAdapter = new RankingDataAdapter(this, R.layout.rank_row_layout, arrayUser);
        lvUser.setAdapter(rankingDataAdapter);

    }
    private void getData(String url) {
        Log.d("CCC", "2");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("CCC", "1");
                        for (int i = 0; i < response.length(); i++) {
                            Log.d("CCC", String.valueOf(response.length()));
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Log.d("AAA", object.getString("volunteer_lastName"));
                                arrayUser.add(new User(
                                        object.getString("volunteer_lastName"),
                                        object.getString("volunteer_job"),
                                        object.getInt("volunteer_score"),
                                        object.getString("volunteer_image")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        rankingDataAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void loadData(){
        class LoadData extends AsyncTask<Void, Void, Boolean> {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.d("abc","1");
                getData(urlGetUser);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                LoadData ld = new LoadData();
                ld.execute();
            }

            //            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                LoadData ld = new LoadData();
//                ld.execute();
//            }
        }
    }
}
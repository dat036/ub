package com.example.ub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity  {
    private String urlGetUser = "http://192.168.1.7/ub/getUser.php";
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
        rankingDataAdapter = new RankingDataAdapter(this,R.layout.rank_row_layout, arrayUser);
        lvUser.setAdapter(rankingDataAdapter);

    }
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i= 0 ; i < response.length();i++){
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
}
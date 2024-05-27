package com.example.doramyclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

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

public class Parser extends AppCompatActivity{
    private RecyclerView recyclerViewShows;
    private ArrayList<ShowModel> arrayListAllShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayListAllShows = new ArrayList<>();

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,"https://api.tvmaze.com/shows", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++){

                                JSONObject info = response.getJSONObject(i);

                                arrayListAllShows.add(new ShowModel(
                                        info.getString("name"),
                                        info.getString("language"),
                                        info.getString("year"),
                                        info.getString("summary"),
                                        info.getJSONObject("image").getString("medium"),
                                        info.getString("id")
                                ));
                            }
                            if(response.length()>0){
                                recyclerViewShows.setHasFixedSize(true);
                                recyclerViewShows.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}

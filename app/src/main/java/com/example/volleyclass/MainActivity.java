package com.example.volleyclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.volleyclass.model.GithubFollowersPojo;
import com.example.volleyclass.model.GithubUsersPojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView txt, user,idd;
    ImageView imageview;
    RecyclerView recyclerView;
   // List<GithubFollowersPojo> list = new ArrayList<>();
    private ArrayList<GithubFollowersPojo> list = new ArrayList<GithubFollowersPojo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        txt = findViewById(R.id.txt);
        user = findViewById(R.id.username);
        idd = findViewById(R.id.id);
        imageview = findViewById(R.id.imageView2);
        //callVolley();
        //JsonObjectR();
        //callMyPojo();
        getCMPundhirFollowers();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void callVolley() //This Method gives us HTML Doc format of API  data from Google.
    {
        String url = "https://www.google.com/";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             txt.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //txt.setText("error"+ error);
            }
        });
        RequestQueue requestQueue =  Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private  void  JsonObjectR() //This method gives us the Json format of API data + user profile image from Github.
    {
        String url = "https://api.github.com/users/akshaysingh5";
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             //txt.setText(response.toString());
                try {
                    String username = response.getString("login");
                    long id = response.getLong("id");
                    user.setText(username);
                    //idd.setText(id+"" );
                    String imhgUrl = response.getString("avatar_url");
                    Glide.with(MainActivity.this).load(imhgUrl).into(imageview);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txt.setText("error : "+error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void callMyPojo() ////This method converts the Json format to Gson format and gives the flexible API data + user profile image from Github.
    {
        String url = " https://api.github.com/users/akshaysingh5";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MainActivity",response);
                Gson gson = new Gson();
                GithubUsersPojo pojo = gson.fromJson(response,GithubUsersPojo.class);
                user.setText(pojo.getLogin());
                //idd.setText(pojo.getId()+"");
                Glide.with(MainActivity.this).load(pojo.getAvatarUrl()).into(imageview);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            // txt.setText("error :" +error.getMessage());
             Log.d("MainActivity","error : "+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    private void getCMPundhirFollowers(){
        String url = "https://api.github.com/users/cmpundhir/followers";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //txt.setText(response);
                Log.d("main_activity", response);
                Gson gson = new Gson();
                list = gson.fromJson(response, list.getClass());
                Log.d("main_act", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt.setText(error.getMessage());
                Log.d("main_activity", "error : "+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.followersdesign,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GithubFollowersPojo data = list.get(position);
            holder.ID.setText(data.getId());
            holder.UserName.setText(data.getLogin());
            holder.img.setImageDrawable(getDrawable(Integer.parseInt(data.getAvatarUrl())));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView UserName,ID;
            ImageView img;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                UserName = itemView.findViewById(R.id.username);
                ID = itemView.findViewById(R.id.id);
                img = itemView.findViewById(R.id.imageView2);
            }
        }
    }


}

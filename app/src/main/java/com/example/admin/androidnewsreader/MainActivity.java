package com.example.admin.androidnewsreader;

import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.androidnewsreader.Adapter.ListSourceAdapter;
import com.example.admin.androidnewsreader.Common.Common;
import com.example.admin.androidnewsreader.Interface.NewsService;
import com.example.admin.androidnewsreader.Model.NewsData;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcl_source;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter sourceAdapter;
    AlertDialog alertDialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init cache
        Paper.init(this);

        //Init Service
        mService = Common.getNewsService();

        //Init View
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        rcl_source = (RecyclerView) findViewById(R.id.rcl_source);
        rcl_source.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rcl_source.setLayoutManager(layoutManager);

        alertDialog = new SpotsDialog(this);

        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed){
        if (!isRefreshed){
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty()){ // If have cache
                NewsData newsData = new Gson().fromJson(cache, NewsData.class);
                sourceAdapter = new ListSourceAdapter(getBaseContext(), newsData);
                sourceAdapter.notifyDataSetChanged();

                rcl_source.setAdapter(sourceAdapter);
            }
            else {     // If not have cache
                alertDialog.show();
                //Fetch new data
                mService.getSources().enqueue(new Callback<NewsData>() {
                    @Override
                    public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                        sourceAdapter =  new ListSourceAdapter(getBaseContext(), response.body());
                        sourceAdapter.notifyDataSetChanged();
                        rcl_source.setAdapter(sourceAdapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<NewsData> call, Throwable t) {

                    }
                });
            }
        }
        else // If from Swipe to Refresh
        {
            alertDialog.show();
            //Fetch new data
            mService.getSources().enqueue(new Callback<NewsData>() {
                @Override
                public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                    sourceAdapter  = new ListSourceAdapter(getBaseContext(),response.body());
                    sourceAdapter.notifyDataSetChanged();
                    rcl_source.setAdapter(sourceAdapter);

                    //Save to cache
                    Paper.book().write("cache",new Gson().toJson(response.body()));

                    //Dismiss refresh progressring
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<NewsData> call, Throwable t) {

                }
            });

        }
    }
}

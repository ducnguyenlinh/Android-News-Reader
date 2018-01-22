package com.example.admin.androidnewsreader.Interface;

import com.example.admin.androidnewsreader.Model.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsService {
    @GET("v2/sources?apiKey=6c68f06c03b04018b00ef06402d3b42a")
    Call<NewsData> getSources();
}

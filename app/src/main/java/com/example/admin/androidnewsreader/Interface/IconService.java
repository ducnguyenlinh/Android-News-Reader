package com.example.admin.androidnewsreader.Interface;

import com.example.admin.androidnewsreader.Model.IconBetterIdea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IconService {
    @GET
    Call<IconBetterIdea> getIconUrl(@Url String url);
}

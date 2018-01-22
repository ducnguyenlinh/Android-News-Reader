package com.example.admin.androidnewsreader.Common;

import com.example.admin.androidnewsreader.Interface.IconService;
import com.example.admin.androidnewsreader.Interface.NewsService;
import com.example.admin.androidnewsreader.Remote.IconClient;
import com.example.admin.androidnewsreader.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";
    private static final String API_KEY = "6c68f06c03b04018b00ef06402d3b42a";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconService getIconService(){
        return IconClient.getClient(BASE_URL).create(IconService.class);
    }
}

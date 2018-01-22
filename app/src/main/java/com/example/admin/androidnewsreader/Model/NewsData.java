package com.example.admin.androidnewsreader.Model;

import java.util.List;

public class NewsData {
    private String status;
    private List<Source> sources;

    public NewsData(String status, List<Source> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}

package com.example.admin.androidnewsreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.androidnewsreader.Common.Common;
import com.example.admin.androidnewsreader.Interface.IconService;
import com.example.admin.androidnewsreader.Interface.ItemClickListener;
import com.example.admin.androidnewsreader.ListNews;
import com.example.admin.androidnewsreader.Model.IconBetterIdea;
import com.example.admin.androidnewsreader.Model.NewsData;
import com.example.admin.androidnewsreader.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        source_image = (CircleImageView) itemView.findViewById(R.id.source_image);
        source_title = (TextView)itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>{

    private Context context;
    private NewsData newsData;
    private IconService mIconService;

    public ListSourceAdapter(Context context, NewsData newsData) {
        this.context = context;
        this.newsData = newsData;

        mIconService = Common.getIconService();
    }

    @Override
    public ListSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_source, parent, false);

        return new ListSourceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListSourceViewHolder holder, int position) {
        StringBuilder iconBetterAPI = new StringBuilder("https://icons.better-idea.org/allicons.json?url=");
        iconBetterAPI.append(newsData.getSources().get(position).getUrl());

        mIconService.getIconUrl(iconBetterAPI.toString())
                .enqueue(new Callback<IconBetterIdea>() {
                    @Override
                    public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                        if(response.body().getIcons().size() > 0)
                        {
                            Picasso.with(context)
                                    .load(response.body().getIcons().get(0).getUrl())
                                    .into(holder.source_image);
                        }
                    }

                    @Override
                    public void onFailure(Call<IconBetterIdea> call, Throwable t) {

                    }
                });

        holder.source_title.setText(newsData.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, Boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source",newsData.getSources().get(position).getId());
                intent.putExtra("sortBy",newsData.getSources().get(position).getSortBysAvailable().get(0)); // get Defailt sortBy method
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsData.getSources().size();
    }
}

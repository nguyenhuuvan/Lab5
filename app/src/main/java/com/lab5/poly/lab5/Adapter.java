package com.lab5.poly.lab5;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder> {
    private Context context;
    private List<Model> news;

    public Adapter(Context context, List<Model> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Model itemNews = news.get(position);
        holder.tvTitle.setText(itemNews.title);
        holder.tvDate.setText(itemNews.pubDate);
        holder.tvDes.setText(itemNews.link);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Main2Activity.class);
                intent.putExtra("data", itemNews.link);
                context.startActivity(intent);
            }
        });
        try {
            String a = itemNews.description;
            Log.e("des", a);
            String[] words = a.split("\\<img width=165 src=\"",2);
            String[] words2 = words[1].split("\" />",2);
            Log.e("aa",words2[0]);
            Glide.with(context).load(words2[0]).into(holder.imgThumbs);
        }catch (Exception e){
            Glide.with(context).load(itemNews.image).into(holder.imgThumbs);
        }

    }


    @Override
    public int getItemCount() {
        if (news == null) return 0;
        return news.size();

    }
}

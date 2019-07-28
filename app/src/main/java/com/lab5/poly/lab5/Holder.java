package com.lab5.poly.lab5;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Holder extends RecyclerView.ViewHolder {
    public final TextView tvTitle;
    public final TextView tvDes;
    public final TextView tvDate;
    public final ImageView imgThumbs;

    public Holder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvDes = itemView.findViewById(R.id.tvDes);
        tvDate = itemView.findViewById(R.id.tvDate);
        imgThumbs = itemView.findViewById(R.id.imgThumbs);
    }
}

package com.firebase.test.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.test.R;
import com.firebase.test.activity.GalleryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OldProductAdapter extends RecyclerView.Adapter<OldProductAdapter.ViewHolder> {

    private int itemHeight;
    private List<String> data;
    private Activity activity;

    public OldProductAdapter(Activity activity, List<String> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Activity context = (Activity) recyclerView.getContext();
        Point windowDimensions = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(windowDimensions);
        itemHeight = Math.round(windowDimensions.y * 0.7f);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_product_gallery, parent, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                itemHeight);
        v.setLayoutParams(params);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.get()
                .load(data.get(position))
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, GalleryActivity.class).putExtra("image", data.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View overlay;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            overlay = itemView.findViewById(R.id.overlay);
        }

        public void setOverlayColor(@ColorInt int color) {
            overlay.setBackgroundColor(color);
        }
    }
}
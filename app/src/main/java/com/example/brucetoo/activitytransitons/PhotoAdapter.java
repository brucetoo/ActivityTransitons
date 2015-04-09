package com.example.brucetoo.activitytransitons;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by brucetoo
 * On 15/4/9.
 * RecyclerAdapter使用步骤
 * 1⃣️  继承RecyclerView.Adapter<RecyclerView.ViewHolder>
 * 2⃣️  在  onCreateViewHolder 中inflate view ，返回一个自定义的RecyclerView.ViewHolder
 * 3⃣️  自定义ViewHolder继承RecyclerView.ViewHolder
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item,viewGroup,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PhotoViewHolder holder = (PhotoViewHolder) viewHolder;
        holder.iv_photo.setImageResource(R.mipmap.test);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    /**
     * photo viewholder
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_photo)
        ImageView iv_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}

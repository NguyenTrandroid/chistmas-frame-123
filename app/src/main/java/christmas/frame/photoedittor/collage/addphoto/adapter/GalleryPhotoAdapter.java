package christmas.frame.photoedittor.collage.addphoto.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.addphoto.OnPhotoSelectt;



public class GalleryPhotoAdapter extends RecyclerView.Adapter<GalleryPhotoAdapter.ViewHolder> {
    ArrayList<String> listResource;
    OnPhotoSelectt onPhotoSelect;
    int maxImage = 0;
    Context context;
    Context context1;

    public GalleryPhotoAdapter(ArrayList<String> listResource, Context context, Context context1) {
        this.listResource = listResource;
        this.onPhotoSelect = (OnPhotoSelectt) context;
        this.context1 = context1;
        this.maxImage = maxImage;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_galleryphoto2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        /**
         * Chuyen Photo Path qua PhotoFragment bang interface
         */

        Glide.with(context1).load(listResource.get(position)).into(holder.photo);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhotoSelect.sendPhoto(listResource.get(position),true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listResource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_photo);
        }
    }



}
package christmas.frame.photoedittor.collage.addphoto.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import christmas.frame.photoedittor.collage.R;


public class GalleryFolderSelectAdapter extends RecyclerView.Adapter<GalleryFolderSelectAdapter.ViewHolder> {
    ArrayList<String> listfolder;
    ArrayList<String> listimage;
    OnDataPass dataPasser;
    Context context;


    public GalleryFolderSelectAdapter(ArrayList<String> listfolder, Context context, ArrayList<String> listimage, OnDataPass dataPasser){
        this.listfolder =listfolder;
        this.listimage =listimage;
        this.dataPasser = dataPasser;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_galleryselect, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(listimage.get(position)).into(holder.photo);
        holder.name.setText(listfolder.get(position));
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataPasser.onDataPass(position);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataPasser.onDataPass(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listimage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cv_folderimage);
            photo = cardView.findViewById(R.id.iv_folderimage);
            name = itemView.findViewById(R.id.tv_foldername);
        }
    }
    public interface OnDataPass {
        void onDataPass(int data);
    }


}
package christmas.frame.photoedittor.collage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.model.PickerImageMenu;


public class StickerMenuAdapter extends RecyclerView.Adapter<StickerMenuAdapter.ViewHolder> {
    List<PickerImageMenu> listResource;
    OnDataPass dataPasser;
    Context context;

    public StickerMenuAdapter(List<PickerImageMenu> listResource, Context context) {
        this.listResource = listResource;
        this.context = context;
        dataPasser = (OnDataPass) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_stickermenu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.stickermenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testmenu", listResource.get(position).getDir());
                dataPasser.onPassDir(listResource.get(position).getDir());
            }
        });
        holder.stickermenu.setText(listResource.get(position).getName());
        Glide.with(context).load("http://45.32.99.2/image/sticker/" + listResource.get(position).getDir() + "/avatar.png").into(holder.stickeravatar);
    }

    @Override
    public int getItemCount() {
        return listResource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stickermenu;
        ImageView stickeravatar;

        public ViewHolder(View itemView) {
            super(itemView);
            stickeravatar = itemView.findViewById(R.id.iv_avatar);
            stickermenu = itemView.findViewById(R.id.tv_stickermenu);
        }
    }

    public interface OnDataPass {
        void onPassDir(String dir);
    }


}
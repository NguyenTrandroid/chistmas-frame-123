package christmas.frame.photoedittor.collage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.tab.OnColorSelect;

public class AddImgLibAdapter extends RecyclerView.Adapter<AddImgLibAdapter.ViewHolder> {
    ArrayList<String> listImg;
    Context context;
    OnColorSelect onColorSelect;

    public AddImgLibAdapter(ArrayList<String> listImg, Context context) {
        this.listImg = listImg;
        this.context = context;
        onColorSelect = (OnColorSelect) context;

    }

    @NonNull
    @Override
    public AddImgLibAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_addbgr_lib, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(listImg.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorSelect.sendPathLib(listImg.get(position));
                Log.d("AAA", listImg.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listImg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_lib);
        }
    }
}

package christmas.frame.photoedittor.collage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.frame.OnFrameSelect;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    List<String> listFrameAsset;
    Context context;
    OnFrameSelect onFrameSelect;

    public FrameAdapter(List<String> listFrameAsset, Context context) {
        this.listFrameAsset = listFrameAsset;
        this.context = context;
        onFrameSelect = (OnFrameSelect) context;

    }

    @NonNull
    @Override
    public FrameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(listFrameAsset.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listFrameAsset.get(position).contains("nonee")) {
                    onFrameSelect.sendFrame("none");
                } else if (listFrameAsset.get(position).contains("addd")) {
                    onFrameSelect.sendFrame("add");
                } else {
                    onFrameSelect.sendFrame(listFrameAsset.get(position));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return listFrameAsset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_photo);
        }
    }
}

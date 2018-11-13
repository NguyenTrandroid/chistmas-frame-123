package christmas.frame.photoedittor.collage.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.text.OnFontSelect;


public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    ArrayList<String> typefaces;
    Context context;
    OnFontSelect onFontSelect;

    public FontAdapter(ArrayList<String> typefaces, Context context, OnFontSelect onFontSelect) {
        this.typefaces = typefaces;
        this.context = context;
        this.onFontSelect = onFontSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_font, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.font.setTypeface(Typeface.createFromAsset(context.getAssets(), typefaces.get(position)));
        holder.font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFontSelect.sendFont(typefaces.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return typefaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView font;

        public ViewHolder(View itemView) {
            super(itemView);
            font = itemView.findViewById(R.id.tv_font);
        }
    }
}
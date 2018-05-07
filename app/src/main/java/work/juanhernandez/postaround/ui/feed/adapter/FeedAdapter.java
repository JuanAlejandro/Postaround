package work.juanhernandez.postaround.ui.feed.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.data.model.RecentMedia;

/**
 * Created by juan.hernandez on 5/4/18.
 * Feed Adapter
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private List<RecentMedia> recentMedia;

    public FeedAdapter(List<RecentMedia> recentMedia) {
        this.recentMedia = recentMedia;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_feed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recentMedia.get(position));
    }

    @Override
    public int getItemCount() {
        return recentMedia.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIGPhoto, civProfilePhoto;
        TextView tvUsername, tvComment;

        ViewHolder(View itemView) {
            super(itemView);
            ivIGPhoto = itemView.findViewById(R.id.ivIGPhoto);
            civProfilePhoto = itemView.findViewById(R.id.civProfilePhoto);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
        }

        void bind(RecentMedia recentMedia) {
            Picasso.get()
                    .load(recentMedia.getImages()
                            .getStandardResolution()
                            .getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(ivIGPhoto);
            ivIGPhoto.setVisibility(View.VISIBLE);

            tvUsername.setText(recentMedia.getUser().getUsername());

            tvComment.setText(recentMedia.getCaption().getText());
        }
    }
}

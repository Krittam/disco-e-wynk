package in.wynk.disco.disco_e_wynk;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements View.OnClickListener {
    private List<ModelClass> modelClassList;
    public Map<String, ContentPojo> contentData;
    private OnPlayClickedListener onPlayClickedListener;
//    private static final String DELETE_TAG = "Song deleted";

    public Adapter(List<ModelClass> modelClassList) {

        contentData = new HashMap<String, ContentPojo>();
        this.modelClassList = modelClassList;
    }

    public OnPlayClickedListener getOnPlayClickedListener() {
        return onPlayClickedListener;
    }

    public void setOnPlayClickedListener(OnPlayClickedListener onPlayClickedListener) {
        this.onPlayClickedListener = onPlayClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        ModelClass modelClass = modelClassList.get(position);
        final String songId = modelClass.getSongId();
//        String resource = modelClass.getImageResource();
//        String title = modelClass.getTitle();
//        String body = modelClass.getBody();

        if (contentData.containsKey(modelClass.getSongId())) {
            ContentPojo contentPojo = contentData.get(modelClass.getSongId());
            viewHolder.setData(modelClass.getSongId(), contentPojo.getSmallImage(), contentPojo.getTitle(), contentPojo.getAlbum());
        } else {
            viewHolder.setData(modelClass.getSongId(),"", modelClass.getSongId(), "");
        }

        viewHolder.playButton.setTag(R.id.exo_prev, songId);
        viewHolder.playButton.setOnClickListener(this);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public void remove(int position){
        modelClassList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, modelClassList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView title;
        private TextView body;
        private ImageButton playButton;
        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            playButton = itemView.findViewById(R.id.playButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        private void setData(String songId, String imageUrl, String titleText, String bodyText){
            Glide.with(imageView)
                    .load(imageUrl)
                    .into(imageView);
            title.setText(titleText);
            body.setText(bodyText);
        }


    }

    @Override
    public void onClick(View view) {
        String songId = (String) view.getTag(R.id.exo_prev);
        if (onPlayClickedListener != null) {
            onPlayClickedListener.onPlayClicked(songId);
        }
    }


    interface OnPlayClickedListener {
        void onPlayClicked(String songId);
    }
}

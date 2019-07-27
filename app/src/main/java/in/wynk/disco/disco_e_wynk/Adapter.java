package in.wynk.disco.disco_e_wynk;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<ModelClass> modelClassList;
    public Map<String, ContentPojo> contentData;
//    private static final String DELETE_TAG = "Song deleted";

    public Adapter(List<ModelClass> modelClassList) {
        contentData = new HashMap<String, ContentPojo>();
        this.modelClassList = modelClassList;
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

        if (contentData.containsKey(modelClass.getSongId())) {
            ContentPojo contentPojo = contentData.get(modelClass.getSongId());
            viewHolder.setData(modelClass.getSongId(), contentPojo.getSmallImage(), contentPojo.getTitle(), contentPojo.getAlbum());
        } else {
            viewHolder.setData(modelClass.getSongId(),"", modelClass.getSongId(), "");
        }
        viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play this song
            }
        });

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

        private void setData(String songId, String resource, String titleText, String bodyText){

            imageView.setImageResource( R.drawable.ic_launcher_background);
            title.setText(titleText);
            body.setText(bodyText);

        }

    }

}

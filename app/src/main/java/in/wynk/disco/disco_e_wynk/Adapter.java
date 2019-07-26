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

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<ModelClass> modelClassList;
//    private static final String DELETE_TAG = "Song deleted";

    public Adapter(List<ModelClass> modelClassList) {

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
        //fetch data
        ModelClass modelClass = modelClassList.get(position);
        int songId = modelClass.getSongId();
        int resource = modelClass.getImageResource();
        String title = modelClass.getTitle();
        String body = modelClass.getBody();

        viewHolder.setData(songId, resource, title, body);

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

        private void setData(int songId, int resource, String titleText, String bodyText){

            imageView.setImageResource((resource));
            title.setText(titleText);
            body.setText(bodyText);

        }

    }

}

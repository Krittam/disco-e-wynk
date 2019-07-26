package in.wynk.disco.disco_e_wynk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ModelClass> modelClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        modelClassList = new ArrayList<>();
        modelClassList.add(new ModelClass(1, R.drawable.ic_launcher_background, "Song 1", "Singer 1"));
        modelClassList.add(new ModelClass(2, R.drawable.ic_launcher_background, "Song 2", "Singer 2"));
        modelClassList.add(new ModelClass(3, R.drawable.ic_launcher_background, "Song 3", "Singer 3"));
        modelClassList.add(new ModelClass(4, R.drawable.ic_launcher_background, "Song 4", "Singer 4"));
        modelClassList.add(new ModelClass(5, R.drawable.ic_launcher_background, "Song 5", "Singer 5"));

        Adapter adapter = new Adapter(modelClassList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

}

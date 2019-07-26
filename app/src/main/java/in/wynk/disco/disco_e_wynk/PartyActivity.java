package in.wynk.disco.disco_e_wynk;

import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartyActivity extends AppCompatActivity {
    private Boolean isHost;
    private String userId;
    private List<String> queue;
    FirebaseDatabase database;
    private String hostId;
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter< String > searchAdapter;
    Context context;

    private RecyclerView recyclerView;
    private EditText copyLinkTextView;
    private List<ModelClass> modelClassList;

    private Button copyButton;

    private ClipboardManager clipboardManager;
    private static final String TAG = "Party_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        isHost = getIntent().getExtras().getBoolean("isHost", false);
        MainActivity.setUid(this);
        userId = MainActivity.getUid(this);
        hostId = userId;
        database = FirebaseDatabase.getInstance();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        handleSearch();

        setDeepLinkState();

        handleQueue();
    }

    public void handleSearch(){
        context=this;
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);
        list = new ArrayList<>();
        searchAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("tag", "hey");
                list.add("dilar2");
                list.add("dilbar3");
                list.add("dilbar3");
                list.add("dilbar3");
                searchAdapter.notifyDataSetChanged();
                Log.d("tag", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //TODO implement song enque
                Log.d("tag", Integer.toString(position));

                list.clear();
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchAdapter.notifyDataSetInvalidated();
            }
        });
    }

    public void handleQueue(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        modelClassList = new ArrayList<>();
        modelClassList.add(new ModelClass(1, R.drawable.ic_launcher_background, "Song 1", "Singer 1"));
        modelClassList.add(new ModelClass(2, R.drawable.ic_launcher_background, "Song 2", "Singer 2"));
        modelClassList.add(new ModelClass(3, R.drawable.ic_launcher_background, "Song 3", "Singer 3"));
        modelClassList.add(new ModelClass(4, R.drawable.ic_launcher_background, "Song 4", "Singer 4"));
        modelClassList.add(new ModelClass(5, R.drawable.ic_launcher_background, "Song 5", "Singer 5"));

        Adapter queueAdapter = new Adapter(modelClassList);
        recyclerView.setAdapter(queueAdapter);
        queueAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.setUid(this);
        Intent in = getIntent();
        Uri data = in.getData();
        System.out.println("deeplinkingcallback   :- " + data);
        if (data != null) {
            isHost = false;
            String[] temp = data.toString().split("=");
            hostId = temp[1];
        }
    }

    public void enQueue(String contentId) {
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }

    public void removeFromQueue(String contentId) {
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }

    public void setupFirebase() {
        List<String> queue = new ArrayList<String>();
        queue.add("song 1");
        queue.add("song 2");
        queue.add("song 3");
        queue.add("song 4");
        queue.add("song 5");

        database.getReference().child("users").child(userId).child("queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> queue = (List<String>) dataSnapshot.getValue();
                String q = "";
                for (String s : queue) {
                    q += s + " # ";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                mTextMessage.setText("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void getContentFromApi() {
        NetworkService.getInstance()
                .getJSONApi()
                .getSearchResults("dilbar")
                .enqueue(new Callback<SearchResponse>() {

                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();
                        Item songItem = null;
                        for (Item item : searchResponse.getItems()) {
                            if ("SONG".equals(item.getId())) {
                                songItem = item;
                                break;
                            }
                        }
                        songItem.getItems();
                    }
                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                    }
                });
    }

    public String getDeeplLink(String uid) {
        return String.format("http://discoewynk.com/uid=%s", uid);
    }

    public void copyToClipboard(String text) {
        ClipData clipData = ClipData.newPlainText("Source Text", text);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void setDeepLinkState(){
        copyButton = findViewById(R.id.copyButton);
        copyLinkTextView = findViewById(R.id.copyLinkText);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(copyLinkTextView.getText().toString());
            }
        });

        if (isHost) {
            copyLinkTextView.setText(getDeeplLink(userId));
        } else {
            copyLinkTextView.setVisibility(View.GONE);
            copyButton.setVisibility(View.GONE);
        }
    }
}

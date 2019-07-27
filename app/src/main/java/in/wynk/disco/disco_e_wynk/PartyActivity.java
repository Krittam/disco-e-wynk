package in.wynk.disco.disco_e_wynk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    private static final String TAG = "Party_Activity";
    FirebaseDatabase database;
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> searchAdapter;
    Context context;
    PlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String url = "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3";
    String url2 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    private Boolean isHost;
    private String userId;
    private List<String> queue;
    private String hostId;
    private RecyclerView recyclerView;
    private EditText copyLinkTextView;
    private List<ModelClass> modelClassList;
    private Button copyButton;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);


        exoPlayerView = findViewById(R.id.exo_player_view);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // final ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            // TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri uri1 = Uri.parse(url);
            // Uri uri2 = Uri.parse(url2);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource Fsource = new ExtractorMediaSource(uri1, dataSourceFactory, extractorsFactory, null, null);
            // MediaSource Ssource = new ExtractorMediaSource(uri2, dataSourceFactory, extractorsFactory, null, null);
            //ConcatenatingMediaSource concatenatedSource =
            //   new ConcatenatingMediaSource(Fsource, Ssource);
            // exoPlayer= ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector(trackSelectionFactory));
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(Fsource);
            //exoPlayer.prepare(Ssource);
            //exoPlayer.prepare(concatenatedSource);
            exoPlayer.setPlayWhenReady(false);
            exoPlayerView.setControllerHideOnTouch(false);
            exoPlayerView.setControllerShowTimeoutMs(0);
            //exoPlayerView.setUseController(true);


            //    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            //    final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            //    TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            //   // DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory();
            //    DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exo-player");
            //    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://play.wynk.in/srch_unisysinfo/music/128/1528715656/srch_unisysinfo_M09050867.mp3?token=1564146430_f03aa48f3e5a76ef2275d371ab23c2e5"), dataSourceFactory, extractorsFactory,null,null);    // replace Uri with your song url
            //    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
            //    exoPlayerView.setPlayer(exoPlayer);
            //    exoPlayer.prepare(mediaSource);
            //    exoPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            Log.e("Exoplayer error:", e.toString());
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        isHost = getIntent().getExtras().getBoolean("isHost", false);
        MainActivity.setUid(this);
        userId = MainActivity.getUid(this);
        hostId = userId;
        database = FirebaseDatabase.getInstance();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ;
        recyclerView = findViewById(R.id.recycler_view);
        copyLinkTextView = findViewById(R.id.copyLinkText);

        context = this;
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);
        list = new ArrayList<>();
        searchAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
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

        if (isHost) {
            copyLinkTextView.setText(getDeeplLink(userId));
        } else {
            copyLinkTextView.setVisibility(View.GONE);
        }

        copyButton = findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(copyLinkTextView.getText().toString());
            }
        });


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


}

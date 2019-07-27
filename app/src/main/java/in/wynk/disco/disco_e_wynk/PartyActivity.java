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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartyActivity extends AppCompatActivity implements Adapter.OnPlayClickedListener{
        private static final String TAG = "Party_Activity";
        FirebaseDatabase database;
        SearchView searchView;
        ListView listView;
        Context context;
        PlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;
        //String url = "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3";
       // String url2 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        String url="";
        private Boolean isHost;
        private String userId;
        private List<String> queue;
        private String hostId;
        ArrayList<Song> list;
        ArrayAdapter< Song > searchAdapter;

        private RecyclerView recyclerView;
        private EditText copyLinkTextView;
        private List<ModelClass> modelClassList;
        private Button copyButton;
        private ClipboardManager clipboardManager;
        private List<String> queueSongIds = new ArrayList<String>();

        private Map<String, ModelClass> songIdToMetaMap;
        Adapter queueAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_party);

//        exoPlayerView = findViewById(R.id.exo_player_view);
//        try {
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            // final ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            // TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
//
//            Uri uri1 = Uri.parse(url);
//            // Uri uri2 = Uri.parse(url2);
//            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//            MediaSource Fsource = new ExtractorMediaSource(uri1, dataSourceFactory, extractorsFactory, null, null);
//            // MediaSource Ssource = new ExtractorMediaSource(uri2, dataSourceFactory, extractorsFactory, null, null);
//            //ConcatenatingMediaSource concatenatedSource =
//            //   new ConcatenatingMediaSource(Fsource, Ssource);
//            // exoPlayer= ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector(trackSelectionFactory));
//            exoPlayerView.setPlayer(exoPlayer);
//            exoPlayer.prepare(Fsource);
//            //exoPlayer.prepare(Ssource);
//            //exoPlayer.prepare(concatenatedSource);
//            if(url==""){
//                exoPlayer.setPlayWhenReady(false);
//            }
//            else{
//                exoPlayer.setPlayWhenReady(true);
//            }
//
//            exoPlayerView.setControllerHideOnTouch(false);
//            exoPlayerView.setControllerShowTimeoutMs(0);
//            //exoPlayerView.setUseController(true);
//
//
//            //    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            //    final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            //    TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//            //   // DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory();
//            //    DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exo-player");
//            //    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://play.wynk.in/srch_unisysinfo/music/128/1528715656/srch_unisysinfo_M09050867.mp3?token=1564146430_f03aa48f3e5a76ef2275d371ab23c2e5"), dataSourceFactory, extractorsFactory,null,null);    // replace Uri with your song url
//            //    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
//            //    exoPlayerView.setPlayer(exoPlayer);
//            //    exoPlayer.prepare(mediaSource);
//            //    exoPlayer.setPlayWhenReady(false);
//        } catch (Exception e) {
//            Log.e("Exoplayer error:", e.toString());
//        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        isHost = getIntent().getExtras().getBoolean("isHost", false);
        MainActivity.setUid(this);
        userId = MainActivity.getUid(this);
        hostId = userId;
        database = FirebaseDatabase.getInstance();
        context=this;
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        songIdToMetaMap = new HashMap<String, ModelClass>();

        handleSearch();

        setDeepLinkState();

        handleQueue();
        setupFirebase();
        exoPlayerfunc();
    }


    public void exoPlayerfunc(){
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
            if(url==""){
                exoPlayer.setPlayWhenReady(false);
            }
            else{
                exoPlayer.setPlayWhenReady(true);
            }

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


    }
    public void handleSearch(){
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);
        list = new ArrayList<>();
        searchAdapter = new ArrayAdapter<Song>(context, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchApi(query);
                list.clear();
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
                enQueue(list.get(position).getId());
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
        recyclerView.setLayoutManager(layoutManager);


        modelClassList = new ArrayList<>();
        queueAdapter = new Adapter(modelClassList);
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

        setupFirebase();
        this.queueAdapter.setOnPlayClickedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.queueAdapter.setOnPlayClickedListener(null);
    }

    @Override
    public void onPlayClicked(String songId) {
        getContentPlaybackApi(songId);
    }

    public void enQueue(String contentId) {
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }

    public void removeFromQueue(String contentId) {
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }

    public void setupFirebase() {

        database.getReference().child("users").child(hostId).child("queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelClassList.clear();
                Map<Integer, String> snapshotMap = (Map<Integer, String>) dataSnapshot.getValue();
                if (snapshotMap!=null){
                    Collection<String> queueSongObjects =  snapshotMap.values();
                    for (String songId : queueSongObjects){
                        ModelClass object;
                        ModelClass songMeta = songIdToMetaMap.get(songId);
                        if(null==songMeta){
                            object = new ModelClass(songId, "", songId, "");
                        }
                        else{
                            object = songIdToMetaMap.get(songId);
                        }

                        modelClassList.add(object);
                        queueAdapter.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                mTextMessage.setText("The read failed: " + databaseError.getCode());
            }
        });

    }


    public void SearchApi() {
        NetworkService.getInstance()
                .getSearchApi()
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

                        list.addAll(songItem.getItems());
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


    public void searchApi(String query) {
        NetworkService.getInstance()
                .getSearchApi()
                .getSearchResults(query)
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
                        list.addAll(songItem.getItems());
                        searchAdapter.notifyDataSetChanged();

//                        Log.e(TAG, songItem.getItems().toString());
                        searchApiHandler(songItem.getItems());
                    }


                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                    }
                });
    }

    void searchApiHandler(List<Song> searchResult){

//        getContentPlaybackApi(searchResult.get(1).getId());
    }

    public void getContentPlaybackApi(String songId) {
        NetworkService.getInstance()
                .getContentApi()
                .getPlaybackResult(songId, "daTr6PpO1NmAesWNG67VK8rE95s2:f9eETGydcRMJ998KWd0ErjnTdIw=", "0203ac820c37ce23/Android/28/188/2.0.7.1")
                .enqueue(new Callback<ContentPlaybackPojo>() {

                    @Override
                    public void onResponse(Call<ContentPlaybackPojo> call, Response<ContentPlaybackPojo> response) {
                        ContentPlaybackPojo contentPlaybackResponse = response.body();
                        playSong(contentPlaybackResponse.getUrl());
                    }


                    @Override
                    public void onFailure(Call<ContentPlaybackPojo> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                    }
                });
    }


    void playSong(String contentPlaybackUrl) {
        Log.e(TAG, contentPlaybackUrl);
        url=contentPlaybackUrl;
        exoPlayerfunc();

    }
}

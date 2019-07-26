package in.wynk.disco.disco_e_wynk;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerActivity extends AppCompatActivity {
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String url="https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3";
    String url2="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        exoPlayerView=(SimpleExoPlayerView) findViewById(R.id.exo_player_view);
    try{
            BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
            // final ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
            TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            // TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);

            Uri uri1= Uri.parse(url);
            Uri uri2= Uri.parse(url2);
            DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exoplayer");
            ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();

            MediaSource Fsource=new ExtractorMediaSource(uri1,dataSourceFactory,extractorsFactory,null,null);
            MediaSource Ssource=new ExtractorMediaSource(uri2,dataSourceFactory,extractorsFactory,null,null);
            ConcatenatingMediaSource concatenatedSource =
                   new ConcatenatingMediaSource(Fsource, Ssource);
            // exoPlayer= ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector(trackSelectionFactory));
            exoPlayerView.setPlayer(exoPlayer);
            // exoPlayer.prepare(Fsource);
             // exoPlayer.prepare(Ssource);
            exoPlayer.prepare(concatenatedSource);
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
        }catch(Exception e){
            Log.e("Exoplayer error:",e.toString());
        }


    }

}

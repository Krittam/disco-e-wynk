package in.wynk.disco.disco_e_wynk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    private TextView mTextMessage;
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;

    private Button.OnClickListener mOnHostClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(MainActivity.this, PartyActivity.class);
            Bundle extras = new Bundle();
            extras.putBoolean("isHost", true);
            myIntent.putExtras(extras);
            MainActivity.this.startActivity(myIntent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button = findViewById(R.id.host_button);
        button.setOnClickListener(mOnHostClickListener);

        setUid(this);

//        getContentFromApi();
//        getContentPlaybackApi();
        SearchApi("dilbar");


    }

    void searchApiHandler(List<Song> searchResult){

        for(Song song : searchResult){
            Log.e(TAG, song.getId());
        }

//        Log.e(TAG, searchResult.get(0).getId());

        getContentPlaybackApi(searchResult.get(1).getId());
    }

//    void contentHandler(String oStreamingUrl){
//        Log.e(TAG, oStreamingUrl);
//        getContentPlaybackApi(oStreamingUrl);
//    }

    void contentPlaybackHandler(String contentPlaybackUrl) {
        Log.e(TAG, contentPlaybackUrl);
    }

    public static void setUid(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mEditor = mPreferences.edit();
        String value = getUid(ctx);
        Log.d(TAG, "setSharedPreferences: " + "value: " + value);
        if (value == null) {
            String randomString = UUID.randomUUID().toString();
            mEditor.putString("uid", randomString);
            mEditor.commit();
        }
    }

    public static String getUid(Context ctx){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("uid", null);
    }

    public void SearchApi(String query) {
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
                        songItem.getItems();

//                        Log.e(TAG, songItem.getItems().toString());
                        searchApiHandler(songItem.getItems());
                    }


                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                    }
                });
    }


//    public void getContentFromApi(final String songId) {
//        NetworkService.getInstance()
//                .getContentApi()
//                .getContentResult(songId, "song", "en")
//                .enqueue(new Callback<ContentPojo>() {
//
//                    @Override
//                    public void onResponse(Call<ContentPojo> call, Response<ContentPojo> response) {
//                        ContentPojo contentResponse = response.body();
//
//
////                        Log.e(TAG, contentResponse.getOstreamingUrl().toString());
//                        contentHandler(songId, contentResponse.getOstreamingUrl());
//                    }
//
//
//                    @Override
//                    public void onFailure(Call<ContentPojo> call, Throwable t) {
//                        Log.d(TAG, t.getLocalizedMessage());
//                    }
//                });
//    }


    public void getContentPlaybackApi(String songId) {
        NetworkService.getInstance()
                .getContentApi()
                .getPlaybackResult(songId, "daTr6PpO1NmAesWNG67VK8rE95s2:f9eETGydcRMJ998KWd0ErjnTdIw=", "0203ac820c37ce23/Android/28/188/2.0.7.1")
                .enqueue(new Callback<ContentPlaybackPojo>() {

                    @Override
                    public void onResponse(Call<ContentPlaybackPojo> call, Response<ContentPlaybackPojo> response) {
                        ContentPlaybackPojo contentPlaybackResponse = response.body();
                        contentPlaybackHandler(contentPlaybackResponse.getUrl());
                    }


                    @Override
                    public void onFailure(Call<ContentPlaybackPojo> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                    }
                });
    }
}
package in.wynk.disco.disco_e_wynk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    private TextView mTextMessage;

    private Button.OnClickListener mOnHostClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(MainActivity.this, PartyActivity.class);
            Bundle extras = new Bundle();
            extras.putBoolean("isHost",true);
            myIntent.putExtras(extras);
            MainActivity.this.startActivity(myIntent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContentFromApi();

        String userId = "krittam_kothari";

        View button = findViewById(R.id.host_button);
        button.setOnClickListener(mOnHostClickListener);


        mTextMessage = (TextView) findViewById(R.id.message);
    }

    public void getContentFromApi(){
        NetworkService.getInstance()
                .getJSONApi()
                .getSearchResults("dilbar")
                .enqueue(new Callback<SearchResponse>() {

                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();
                        Item songItem = null;
                        for (Item item : searchResponse.getItems()){
                            if ("SONG".equals(item.getId())){
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





}

package in.wynk.disco.disco_e_wynk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    private TextView mTextMessage;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

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

        setSharedPreferences("uid");

    }

    public void setSharedPreferences(String uidKey) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String value = mPreferences.getString(uidKey, null);

        Log.d(TAG, "setSharedPreferences: key: " + uidKey + "value: " + value);

        if (value == null) {
            String randomString = UUID.randomUUID().toString();
            mEditor.putString(uidKey, randomString);
            mEditor.commit();
        }

    }


}
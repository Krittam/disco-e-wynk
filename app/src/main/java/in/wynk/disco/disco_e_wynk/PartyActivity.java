package in.wynk.disco.disco_e_wynk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PartyActivity extends AppCompatActivity {
    private Boolean isHost;
    private String userId;
    private List<String> queue;
    FirebaseDatabase database;
    private String hostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isHost = getIntent().getExtras().getBoolean("isHost", false);
        userId = "default_userId";
        hostId = "default_hostId";
        database = FirebaseDatabase.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent in = getIntent();
        Uri data = in.getData();
        System.out.println("deeplinkingcallback   :- "+data);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
    public void enQueue(String contentId){
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }

    public void removeFromQueue(String contentId){
        database.getReference().child("users").child(hostId).child("queue").push().setValue(contentId);
    }


    public void setupFirebase(){
        List<String> queue = new ArrayList<String>();
        queue.add("song 1");
        queue.add("song 2");
        queue.add("song 3");
        queue.add("song 4");
        queue.add("song 5");


        database.getReference().child("users").child(userId).child("queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> queue =(List<String>) dataSnapshot.getValue();
                String q = "";
                for (String s : queue){
                    q+= s + " # ";
                }
//                mTextMessage.setText(q);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                mTextMessage.setText("The read failed: " + databaseError.getCode());
            }
        });

    }

}

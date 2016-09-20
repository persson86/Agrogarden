package com.mob.schneiderpersson.agrohortav1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyService extends IntentService {
    ArrayList<String> catalogoList = new ArrayList<>();

    public MyService() {
        super(MyService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //retrieving data from the received intent
        int id = intent.getIntExtra("id", 0);
        String message = intent.getStringExtra("msg");

        Log.i("Data  ", "id : " + id + " message : " + message);
        //-----------------------------------------------


        //Do your long running task here
        Intent myIntent = new Intent("MyServiceStatus");
        switch (id) {
            case 1:
/*                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                getCatalogo();
                /*myIntent.putExtra("serviceMessage", "2");
                LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);*/
                break;
            case 2:
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                myIntent.putExtra("serviceMessage", "DONE");
                LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);
                break;
        }



        //------------------------------------------------

/*        //Broadcasting some data
        Intent myIntent = new Intent("MyServiceStatus");
        myIntent.putExtra("serviceMessage", "Task done");

        // Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);*/

    }

    private void getCatalogo() {
        DatabaseReference mDatabase;
        //Firebase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        catalogoList.clear();

        Log.i("LFSP", "getCatalogo - before BD");
        mDatabase.child("catalogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("LFSP", "getCatalogo - Data Change");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    catalogoList.add(data.getKey());
                }
                Intent myIntent = new Intent("MyServiceStatus");
                myIntent.putStringArrayListExtra("serviceMessage", catalogoList);
                //myIntent.putExtra("serviceMessage", "2");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
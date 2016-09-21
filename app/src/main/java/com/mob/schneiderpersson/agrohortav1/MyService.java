package com.mob.schneiderpersson.agrohortav1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.concurrent.atomic.AtomicInteger;

public class MyService extends IntentService {
    ArrayList<String> catalogoList = new ArrayList<>();
    ArrayList<String> compKeyList = new ArrayList<String>();
    ArrayList<String> antaKeyList = new ArrayList<String>();
    ArrayList<String> genericList = new ArrayList<String>();

    DatabaseReference mDatabase;

    Intent myIntent = new Intent("MyServiceStatus");

    public MyService() {
        super(MyService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //retrieving data from the received intent
        int id = intent.getIntExtra("id", 0);
        String message = intent.getStringExtra("msg");

        Log.i("Data  ", "id : " + id + " message : " + message);
        //-----------------------------------------------


        //Do your long running task here
        Intent myIntent = new Intent("MyServiceStatus");
        switch (id) {
            case 1:
                getCatalogoKey();
                break;
            case 2:
                compKeyList = intent.getStringArrayListExtra("compKeyList");
                getCompanheiras();
                break;
            case 3:
                antaKeyList = intent.getStringArrayListExtra("antaKeyList");
                getAntagonicas();
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

        //Firebase Reference

        catalogoList.clear();

        Log.i("LFSP", "getCatalogo - before BD");
        mDatabase.child("catalogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("LFSP", "getCatalogo - Data Change");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    catalogoList.add(data.getKey());
                }

                //myIntent.putExtra("serviceMessage", "2");
                //myIntent.putStringArrayListExtra("serviceMessage", catalogoList);

                Bundle extras = new Bundle();
                extras.putString("Message", "2");
                extras.putStringArrayList("serviceMessage", catalogoList);
                myIntent.putExtras(extras);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getCatalogoKey() {
        String key = "alface";
        mDatabase.child("catalogo").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Catalogo c = Snapshot.getValue(Catalogo.class);

                    String key = c.getKey();
                    String tipo = c.getTipo();

                    if (tipo.equals("C"))
                        compKeyList.add(key);

                    else
                        antaKeyList.add(key);
                }

                Bundle extras = new Bundle();
                extras.putString("step", "2");
                extras.putStringArrayList("compKeyList", compKeyList);
                extras.putStringArrayList("antaKeyList", antaKeyList);
                myIntent.putExtras(extras);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                int ii = 10;
            }
        });
    }

    private void getCompanheiras(){
        final AtomicInteger index = new AtomicInteger();
        index.set(0);

        for (String key : compKeyList) {
            if (key.equals(""))
                continue;

            mDatabase.child("relCompanheiras").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = index.get();
                    count = count + 1;
                    index.lazySet(count);

                    Relacionamento r = dataSnapshot.getValue(Relacionamento.class);
                    String item1 = r.getItem1();
                    String item2 = r.getItem2();

                    genericList.add(item1);
                    genericList.add(item2);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        while (compKeyList.size() != index.get()) {
            continue;
        }

        Bundle extras = new Bundle();
        extras.putString("step", "3");
        extras.putStringArrayList("compList", genericList);
        myIntent.putExtras(extras);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
    }

    private void getAntagonicas(){
        final AtomicInteger index = new AtomicInteger();
        index.set(0);

        for (String key : antaKeyList) {
            if (key.equals(""))
                continue;

            mDatabase.child("relAntagonicas").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = index.get();
                    count = count + 1;
                    index.lazySet(count);

                    Relacionamento r = dataSnapshot.getValue(Relacionamento.class);
                    String item1 = r.getItem1();
                    String item2 = r.getItem2();

                    genericList.add(item1);
                    genericList.add(item2);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        while (antaKeyList.size() != index.get()) {
            continue;
        }

        Bundle extras = new Bundle();
        extras.putString("step", "DONE");
        extras.putStringArrayList("antaList", genericList);
        myIntent.putExtras(extras);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
    }
}
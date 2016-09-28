package com.mob.schneiderpersson.agrohortav1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Main2Activity extends AppCompatActivity {

    EditText etInput;
    ListView lvList, lvCatalogo;
    ProgressBar progressBar;
    Button btAddPlanta;

    String plantaInput;

    ArrayList<String> listInput = new ArrayList<>();
    ArrayList<String> listCatalogo = new ArrayList<>();

    ArrayList<String> compList = new ArrayList<String>();
    ArrayList<String> antaList = new ArrayList<String>();

    ArrayList<String> compKeyList = new ArrayList<String>();
    ArrayList<String> antaKeyList = new ArrayList<String>();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toast.makeText(this, "ok!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //register broadcast receiver for the intent MyTaskStatus
        LocalBroadcastManager.getInstance(this).registerReceiver(MyReceiver, new IntentFilter("MyServiceStatus"));
    }

    //Defining broadcast receiver
    private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> compListAux = new ArrayList<String>();
            ArrayList<String> antaListAux = new ArrayList<String>();
            ArrayList<String> compKeyListAux = new ArrayList<String>();
            ArrayList<String> antaKeyListAux = new ArrayList<String>();

            String step = intent.getStringExtra("step");

            switch (step)
            {
                case "2":
                    compKeyListAux = intent.getStringArrayListExtra("compKeyList");
                    antaKeyListAux = intent.getStringArrayListExtra("antaKeyList");

                    for (String line : compKeyListAux)
                    {
                        compKeyList.add(line);
                    }
                    for (String line : antaKeyListAux)
                    {
                        antaKeyList.add(line);
                    }

                    Intent intent2 = new Intent(getApplicationContext(), MyService.class);
                    intent2.putExtra("id", 2);
                    intent2.putStringArrayListExtra("compKeyList", compKeyList);
                    startService(intent2);
                    break;
                case "3":
                    compListAux = intent.getStringArrayListExtra("compList");

                    for (String line : compListAux)
                    {
                        compList.add(line);
                    }

                    Intent intent3 = new Intent(getApplicationContext(), MyService.class);
                    intent3.putExtra("id", 3);
                    intent3.putStringArrayListExtra("antaKeyList", antaKeyList);
                    startService(intent3);
                    break;
                case "DONE":
                    antaListAux = intent.getStringArrayListExtra("antaList");

                    for (String line : antaListAux)
                    {
                        antaList.add(line);
                    }

                    mergeLists();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initialize();
        loadAsyncTaskForGetDataInput();
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(MyReceiver);
    }

    public void initialize(){
        etInput = (EditText) findViewById(R.id.etInput);
        lvList = (ListView) findViewById(R.id.lvList);
        lvCatalogo = (ListView) findViewById(R.id.lvCatalogo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btAddPlanta = (Button) findViewById(R.id.btAddPlanta);

        plantaInput = etInput.getText().toString().toLowerCase();
    }

    public void btAddPlanta_onCLick(View view) {
        initialize();
        if (plantaInput.length() > 0) {
            listInput.add(plantaInput);

            loadListInput();

            btAddPlanta.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Intent intent = new Intent(getApplicationContext(), MyService.class);
            intent.putExtra("id", 1);
            intent.putExtra("msg", "hi");
            intent.putExtra("plantaInput", plantaInput);
            startService(intent);

            etInput.setText("");
            plantaInput = "";
        }
    }

    private void loadListInput() {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listInput);

        lvList.setAdapter(itemsAdapter);
    }

    private void loadAsyncTaskForGetDataInput() {
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                getCatalogo();
                return 1;
            }

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Integer integer) {
            }

        }.execute();
    }

    private void getCatalogo() {
        listCatalogo.clear();

        mDatabase.child("catalogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    listCatalogo.add(data.getKey());
                }
                loadListCatalogo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadListCatalogo() {
        progressBar.setVisibility(View.GONE);
        btAddPlanta.setVisibility(View.VISIBLE);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCatalogo);
        lvCatalogo.setAdapter(itemsAdapter);
    }

    private void mergeLists() {
        ArrayList<String> listCatalogoAux = new ArrayList<>();

        //Remove itens duplicados
        compList = new ArrayList<String>(new LinkedHashSet<String>(compList));
        antaList = new ArrayList<String>(new LinkedHashSet<String>(antaList));

        //Remove itens do input da lista
        compList.removeAll(listInput);
        antaList.removeAll(listInput);

        listCatalogoAux.clear();
        listCatalogoAux.add("*******Companheiras******");

        Collections.sort(compList);

        for (String line : compList)
        {
            listCatalogoAux.add(line);
        }

        listCatalogoAux.add("*******Neutras******");

        Collections.sort(listCatalogo);

        for (String line : listCatalogo)
        {
            listCatalogoAux.add(line);
        }

        listCatalogoAux = new ArrayList<String>(new LinkedHashSet<String>(listCatalogoAux));
        listCatalogo.clear();
        listCatalogo = listCatalogoAux;

        //Remove itens do input da lista e de antagonicas
        listCatalogo.removeAll(listInput);
        listCatalogo.removeAll(antaList);

        //Collections.sort(listCatalogo);

        loadListCatalogo();
    }
}

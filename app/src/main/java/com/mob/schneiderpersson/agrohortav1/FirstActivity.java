package com.mob.schneiderpersson.agrohortav1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    ArrayList<Plant> listCatalogo = new ArrayList<>();
    ArrayList<Plant> listBox = new ArrayList<>();
    ArrayList<Plant> listAux = new ArrayList<>();

    GridView gridView;
    GridView gvBox;

    PlantAdapter plantAdapter;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAsyncTaskForGetDataInput();
    }

    private void loadGridView() {

        for (Plant p:listCatalogo)
        {
            listAux.add(p);
        }

        gridView = (GridView) findViewById(R.id.gridview);
        plantAdapter = new PlantAdapter(this, listAux);
        gridView.setAdapter(plantAdapter);

        gvBox = (GridView) findViewById(R.id.gvBox);
        plantAdapter = new PlantAdapter(this, listBox);
        gvBox.setAdapter(plantAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAux.get(position);
                plantAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();

                listAux.remove(position);

                Plant p = new Plant();
                p = listCatalogo.get(position);
                listBox.add(p);

            }
        });

        gvBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listBox.get(position);
                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
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

                //progressBar.setVisibility(View.VISIBLE);
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
                    Plant p = new Plant();
                    p.setName(data.getKey());
                    listCatalogo.add(p);
                }
                loadGridView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
/*
    private Plant[] plants = {
            new Plant(R.string.p0, R.drawable.img_hortela),
            new Plant(R.string.p1, R.drawable.img_hortela),
            new Plant(R.string.p2, R.drawable.img_hortela),
            new Plant(R.string.p3, R.drawable.img_hortela),
            new Plant(R.string.p4, R.drawable.img_hortela),
            new Plant(R.string.p5, R.drawable.img_hortela),
            new Plant(R.string.p6, R.drawable.img_hortela),
            new Plant(R.string.p7, R.drawable.img_hortela),
            new Plant(R.string.p8, R.drawable.img_hortela),
            new Plant(R.string.p9, R.drawable.img_hortela),
            new Plant(R.string.p10, R.drawable.img_hortela),
            new Plant(R.string.p11, R.drawable.img_hortela),
            new Plant(R.string.p12, R.drawable.img_hortela),
            new Plant(R.string.p13, R.drawable.img_hortela),
            new Plant(R.string.p14, R.drawable.img_hortela),
            new Plant(R.string.p15, R.drawable.img_hortela),
            new Plant(R.string.p16, R.drawable.img_hortela),
            new Plant(R.string.p17, R.drawable.img_hortela),
            new Plant(R.string.p18, R.drawable.img_hortela),
            new Plant(R.string.p19, R.drawable.img_hortela),
            new Plant(R.string.p20, R.drawable.img_hortela),
            new Plant(R.string.p21, R.drawable.img_hortela),
            new Plant(R.string.p22, R.drawable.img_hortela),
            new Plant(R.string.p23, R.drawable.img_hortela),
            new Plant(R.string.p24, R.drawable.img_hortela),
            new Plant(R.string.p25, R.drawable.img_hortela),
            new Plant(R.string.p26, R.drawable.img_hortela),
            new Plant(R.string.p27, R.drawable.img_hortela),
            new Plant(R.string.p28, R.drawable.img_hortela),
            new Plant(R.string.p29, R.drawable.img_hortela),
            new Plant(R.string.p30, R.drawable.img_hortela),
            new Plant(R.string.p31, R.drawable.img_hortela),
            new Plant(R.string.p32, R.drawable.img_hortela),
            new Plant(R.string.p33, R.drawable.img_hortela),
            new Plant(R.string.p34, R.drawable.img_hortela),
            new Plant(R.string.p35, R.drawable.img_hortela),
            new Plant(R.string.p36, R.drawable.img_hortela),
            new Plant(R.string.p37, R.drawable.img_hortela),
            new Plant(R.string.p38, R.drawable.img_hortela),
            new Plant(R.string.p39, R.drawable.img_hortela),
            new Plant(R.string.p40, R.drawable.img_hortela),
            new Plant(R.string.p41, R.drawable.img_hortela),
            new Plant(R.string.p42, R.drawable.img_hortela),
            new Plant(R.string.p43, R.drawable.img_hortela),
            new Plant(R.string.p44, R.drawable.img_hortela)};*/

}


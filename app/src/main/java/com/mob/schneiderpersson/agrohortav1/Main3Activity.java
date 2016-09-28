package com.mob.schneiderpersson.agrohortav1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mob.schneiderpersson.agrohortav1.R.id.btAddPlanta;
import static com.mob.schneiderpersson.agrohortav1.R.id.lvCatalogo;

public class Main3Activity extends AppCompatActivity {

    GridView gridview;
    GridView grid;

    String text[] = {};

    int image[] = {R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela,R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela};

    ProgressBar progressBar;
    ArrayList<String> listCatalogo = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<Integer> listImage = new ArrayList<>();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAsyncTaskForGetDataInput();
        //GridView gridview = (GridView) findViewById(R.id.gridview);
        //gridview.setAdapter(new ImageAdapter(this));

        grid = (GridView)findViewById(R.id.simpleGrid);
        /*grid.setAdapter(new ImageAdapter(this,image,text));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),text[position],Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void loadCatalogo(){

        for (String line:listCatalogo)
        {

            listName.add(line);
            listImage.add(R.drawable.img_hortela);
        }


        grid.setAdapter(new ImageAdapter(this, listCatalogo));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();
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
                    listCatalogo.add(data.getKey());
                }
                loadCatalogo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}


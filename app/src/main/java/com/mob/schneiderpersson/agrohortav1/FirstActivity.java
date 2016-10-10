package com.mob.schneiderpersson.agrohortav1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    ArrayList<Plant> listCatalogoDB = new ArrayList<>();
    ArrayList<String> listCatalogoSaved = new ArrayList<>();
    ArrayList<Plant> listBBox = new ArrayList<>();
    ArrayList<Plant> listAux = new ArrayList<>();
    ArrayList<String> listCatalogo = new ArrayList<>();
    ArrayList<String> listBox = new ArrayList<>();
    HashMap<String, String> boxMap = new HashMap<>();
    ArrayList<String> listInput = new ArrayList<>();
    ArrayList<String> listColor = new ArrayList<>();

    GridView gvList;
    GridView gvBox;

    GridView gvComp, gvNeutras, gvAnta;

    PlantAdapter plantCatalogoAdapter, plantBoxAdapter, compAdapter, antaAdapter, neutrasAdapter;

    TextView tvTitle;
    LinearLayout llTitle;
    ProgressBar progressBarCatalogo;
    ProgressDialog progressDialog;

    ArrayList<String> compList = new ArrayList<String>();
    ArrayList<String> compListSaved = new ArrayList<String>();
    ArrayList<String> antaList = new ArrayList<String>();
    ArrayList<String> antaListSaved = new ArrayList<String>();

    ArrayList<String> compKeyList = new ArrayList<String>();
    ArrayList<String> antaKeyList = new ArrayList<String>();

    String inputPlant;

    RelativeLayout rlOld;
    LinearLayout llNew;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAsyncTaskForGetDataInput();
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(MyReceiver, new IntentFilter("MyServiceStatus"));
    }

    //region BroadCastReceiver
    private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> compListAux = new ArrayList<String>();
            ArrayList<String> antaListAux = new ArrayList<String>();
            ArrayList<String> compKeyListAux = new ArrayList<String>();
            ArrayList<String> antaKeyListAux = new ArrayList<String>();

            String step = intent.getStringExtra("step");

            switch (step) {
                case "2":
                    compKeyListAux = intent.getStringArrayListExtra("compKeyList");
                    antaKeyListAux = intent.getStringArrayListExtra("antaKeyList");

                    for (String line : compKeyListAux) {
                        compKeyList.add(line);
                    }
                    for (String line : antaKeyListAux) {
                        antaKeyList.add(line);
                    }

                    Intent intent2 = new Intent(getApplicationContext(), MyService.class);
                    intent2.putExtra("id", 2);
                    intent2.putStringArrayListExtra("compKeyList", compKeyList);
                    startService(intent2);
                    break;
                case "3":
                    compListAux = intent.getStringArrayListExtra("compList");

                    for (String line : compListAux) {
                        compList.add(line);
                    }

                    Intent intent3 = new Intent(getApplicationContext(), MyService.class);
                    intent3.putExtra("id", 3);
                    intent3.putStringArrayListExtra("antaKeyList", antaKeyList);
                    startService(intent3);
                    break;
                case "DONE":
                    antaListAux = intent.getStringArrayListExtra("antaList");

                    for (String line : antaListAux) {
                        antaList.add(line);
                    }
                    progressDialog.dismiss();
                    mergeLists();
                    break;
            }
        }
    };
    //endregion

    private void initialize() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        progressBarCatalogo = (ProgressBar) findViewById(R.id.progressBarCatalogo);
        progressBarCatalogo.setVisibility(View.VISIBLE);

        gvList = (GridView) findViewById(R.id.gvList);
        gvBox = (GridView) findViewById(R.id.gvBox);
/*        gvComp = (GridView)findViewById(R.id.gvComp);
        gvAnta = (GridView)findViewById(R.id.gvAnta);
        gvNeutras = (GridView)findViewById(R.id.gvNeutra);*/

        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDialog();

                inputPlant = "";
                inputPlant = listCatalogo.get(position);
                addFilter(inputPlant);
                catalogoManager(position, inputPlant);
            }
        });

/*        gvComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDialog();

                inputPlant = "";
                inputPlant = compListSaved.get(position);
                addFilter(inputPlant);
                catalogoManager(position, inputPlant);
            }
        });*/
    }

    private void clearLists() {
        compList.clear();
        antaList.clear();
        compKeyList.clear();
        antaKeyList.clear();
    }

    private void addFilter(String filter) {
        clearLists();
        listInput.add(filter);

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        intent.putExtra("id", 1);
        intent.putExtra("plantaInput", filter);
        startService(intent);
    }

    private void catalogoManager(int position, String plant) {
        listColor.clear();
        listBox.add(plant);
        listColor.add("b");

        plantBoxAdapter.notifyDataSetChanged();
        gvBox.setAdapter(plantBoxAdapter);

        if (listBox.size() > 0) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    private void mergeLists() {
        ArrayList<String> listCatalogoAux = new ArrayList<>();
        HashMap<String, String> listMap = new HashMap<>();

        //Remove itens duplicados
        compList = new ArrayList<String>(new LinkedHashSet<String>(compList));
        antaList = new ArrayList<String>(new LinkedHashSet<String>(antaList));

        compList.remove(listInput);
        antaList.remove(listInput);

        //region Antagônicas
        for (String line : antaList)
            antaListSaved.add(line);

        antaListSaved = new ArrayList<String>(new LinkedHashSet<String>(antaListSaved));
        antaListSaved.removeAll(listInput);
        Collections.sort(antaListSaved);
        //endregion

        //region Companheiras
        for (String line : compList)
            compListSaved.add(line);

        compListSaved = new ArrayList<String>(new LinkedHashSet<String>(compListSaved));
        compListSaved.removeAll(listInput);
        compListSaved.removeAll(antaListSaved);
        Collections.sort(compListSaved);
        //endregion

        listColor.clear();
        listCatalogo.removeAll(listInput);
        listCatalogo.removeAll(compListSaved);
        listCatalogo.removeAll(antaListSaved);

        for (String p : compListSaved) {
            listCatalogoAux.add(p);
            listColor.add("c");
        }

        for (String p : listCatalogo) {
            listCatalogoAux.add(p);
            listColor.add("n");
        }

        for (String p : antaListSaved) {
            listCatalogoAux.add(p);
            listColor.add("a");
        }

        listCatalogo = listCatalogoAux;

        plantCatalogoAdapter = new PlantAdapter(this, listCatalogo, listColor);
        plantCatalogoAdapter.notifyDataSetChanged();
        gvList.setAdapter(plantCatalogoAdapter);
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(FirstActivity.this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setTitle("Buscando Plantas");
        progressDialog.show();
    }

    private void loadGridView() {
        for (Plant p : listCatalogoDB) {
            listCatalogo.add(p.getName());
        }

        listColor.clear();

        plantCatalogoAdapter = new PlantAdapter(this, listCatalogo, listColor);
        plantCatalogoAdapter.notifyDataSetChanged();
        gvList.setAdapter(plantCatalogoAdapter);

        plantBoxAdapter = new PlantAdapter(this, listBox, listColor);
        plantBoxAdapter.notifyDataSetChanged();
        gvBox.setAdapter(plantBoxAdapter);

        progressBarCatalogo.setVisibility(View.GONE);

        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputPlant = "";
                inputPlant = listCatalogo.get(position);

                if (antaListSaved.contains(inputPlant)) {
                    Toast.makeText(getApplicationContext(), "Planta Antagônica não pode ser selecionada", Toast.LENGTH_SHORT).show();
                    return;
                }

                startDialog();

                addFilter(inputPlant);
                catalogoManager(position, inputPlant);
            }
        });

        gvBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p = listBox.get(position);

                listInput.remove(p);
                listBox.remove(position);

                plantBoxAdapter.notifyDataSetChanged();
                gvBox.invalidateViews();

                listCatalogo.add(p);

                plantBoxAdapter.notifyDataSetChanged();
                gvList.invalidateViews();

                Collections.sort(listCatalogo);

                if (listBox.size() > 0) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                }

                if (listInput.size() == 0) {
                    clearLists();
                    listCatalogo.clear();
                    listCatalogoDB.clear();
                    compListSaved.clear();
                    antaListSaved.clear();

                    loadAsyncTaskForGetDataInput();
                }
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
        listCatalogoDB.clear();

        mDatabase.child("catalogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Plant p = new Plant();
                    p.setName(data.getKey());
                    listCatalogoDB.add(p);
                }
                loadGridView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //region dead code

    /*private Plant[] plants = {
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

    //endregion

}


package com.mob.schneiderpersson.agrohorta;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

@EActivity(R.layout.activity_first)
public class FirstActivity extends AppCompatActivity {

    private ArrayList<Plant> listCatalogoDB = new ArrayList<>();
    private ArrayList<String> listCatalogo = new ArrayList<>();
    private ArrayList<String> listBox = new ArrayList<>();
    private ArrayList<String> listInput = new ArrayList<>();
    private ArrayList<String> listColor = new ArrayList<>();
    private ArrayList<String> compList = new ArrayList<String>();
    private ArrayList<String> compListSaved = new ArrayList<String>();
    private ArrayList<String> antaList = new ArrayList<String>();
    private ArrayList<String> antaListSaved = new ArrayList<String>();
    private ArrayList<String> compKeyList = new ArrayList<String>();
    private ArrayList<String> antaKeyList = new ArrayList<String>();
    private HashMap<String, String> relAntaHist = new HashMap<>();
    private HashMap<String, String> relCompHist = new HashMap<>();
    private PlantAdapter plantCatalogoAdapter, plantBoxAdapter;
    private ProgressDialog progressDialog;
    private String inputPlant;

    private DatabaseReference mDatabase;

    @ViewById
    GridView gvList;

    @ViewById
    GridView gvBox;

    @ViewById
    TextView tvTitle;

    @ViewById
    ProgressBar progressBarCatalogo;

    @AfterViews
    void init() {
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
                    mergeLists(null);
                    break;
            }
        }
    };
    //endregion

    private void initialize() {
        progressBarCatalogo.setVisibility(View.VISIBLE);

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

    private void mergeLists(@Nullable String plantaRemovida) {
        ArrayList<String> listCatalogoAux = new ArrayList<>();

        //Remove itens duplicados
        compList = new ArrayList<String>(new LinkedHashSet<String>(compList));
        antaList = new ArrayList<String>(new LinkedHashSet<String>(antaList));

        compList.remove(listInput);
        antaList.remove(listInput);

        String planta;
        if (plantaRemovida == null) {
            int size = listBox.size() - 1;
            planta = listBox.get(size);
        } else {
            planta = plantaRemovida;

            Iterator<Map.Entry<String, String>> iter = relAntaHist.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String key = planta;
                String value = entry.getValue();
                key += value;
                if (key.equalsIgnoreCase(entry.getKey())) {
                    iter.remove();
                    antaListSaved.remove(value);
                    antaList.remove(value);
                    listCatalogo.add(value);
                }
            }

            for (String value : relAntaHist.values()) {
                if (!antaListSaved.contains(value))
                    antaListSaved.add(value);
            }

            Iterator<Map.Entry<String, String>> iterComp = relCompHist.entrySet().iterator();
            while (iterComp.hasNext()) {
                Map.Entry<String, String> entry = iterComp.next();
                String key = planta;
                String value = entry.getValue();
                key += value;
                if (key.equalsIgnoreCase(entry.getKey())) {
                    iterComp.remove();
                    compListSaved.remove(value);
                    compList.remove(value);
                    listCatalogo.add(value);
                }
            }

            for (String value : relCompHist.values()) {
                if (!compListSaved.contains(value))
                    compListSaved.add(value);
            }
        }

        //region Antagônicas
        for (String line : antaList) {
            antaListSaved.add(line);
            String plantaKey = planta;
            plantaKey += line;
            relAntaHist.put(plantaKey, line);
        }

        antaListSaved = new ArrayList<String>(new LinkedHashSet<String>(antaListSaved));
        antaListSaved.removeAll(listInput);
        Collections.sort(antaListSaved);
        //endregion

        //region Companheiras
        for (String line : compList) {
            compListSaved.add(line);
            String plantaKey = planta;
            plantaKey += line;
            relCompHist.put(plantaKey, line);
        }

        compListSaved = new ArrayList<String>(new LinkedHashSet<String>(compListSaved));
        compListSaved.removeAll(listInput);
        compListSaved.removeAll(antaListSaved);
        Collections.sort(compListSaved);
        //endregion

        listColor.clear();
        listCatalogo.removeAll(listInput);
        listCatalogo.removeAll(compListSaved);
        listCatalogo.removeAll(antaListSaved);
        Collections.sort(listCatalogo);

        for (String p : compListSaved) {
            listCatalogoAux.add(p);
            listColor.add("c");
            Log.i("compListSaved", p);
        }

        for (String p : listCatalogo) {
            listCatalogoAux.add(p);
            listColor.add("n");
            Log.i("listCatalogo", p);
        }

        for (String p : antaListSaved) {
            listCatalogoAux.add(p);
            listColor.add("a");
            Log.i("antaListSaved", p);
        }

        listCatalogo.clear();
        listCatalogo = listCatalogoAux;

        listCatalogo = new ArrayList<String>(new LinkedHashSet<String>(listCatalogo));

        plantCatalogoAdapter = new PlantAdapter(this, listCatalogo, listColor);
        plantCatalogoAdapter.notifyDataSetChanged();
        gvList.setAdapter(plantCatalogoAdapter);
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(FirstActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.buscando_plantas));
        progressDialog.setTitle(getResources().getString(R.string.aguarde));
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
                    relAntaHist.clear();
                    relCompHist.clear();

                    loadAsyncTaskForGetDataInput();
                } else
                    mergeLists(p);
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
        listCatalogo.clear();

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
}


package com.mob.schneiderpersson.agrohortav1;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    EditText etPlanta1, etPlanta2;
    Button btAddRelCompanheiras, btAddRelAntagonicas, btGetData;
    String input1, input2;
    ProgressBar progressBar;
    ListView lvList;

    HashMap<String, String> jsonMap = new HashMap<String, String>();
    HashMap<String, String> listMap = new HashMap<String, String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> compList = new ArrayList<String>();
    ArrayList<String> antaList = new ArrayList<String>();
    HashMap<String, String> testMap = new HashMap<String, String>();

    ArrayList<String> inputList = new ArrayList<String>();
    ArrayList<String> inputListAux = new ArrayList<String>();
    ArrayList<String> compKeyList = new ArrayList<String>();
    ArrayList<String> antaKeyList = new ArrayList<String>();
    public boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Firebase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
        etPlanta1 = (EditText) findViewById(R.id.etPlanta1);
        etPlanta2 = (EditText) findViewById(R.id.etPlanta2);
        btAddRelCompanheiras = (Button) findViewById(R.id.btAddRelCompanheiras);
        btAddRelAntagonicas = (Button) findViewById(R.id.btAddRelAntagonicas);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lvList = (ListView) findViewById(R.id.lvList);
        btGetData = (Button) findViewById(R.id.btGetData);
    }

    private void convertInputText() {
        input1 = etPlanta1.getText().toString().toLowerCase();
        input2 = etPlanta2.getText().toString().toLowerCase();
    }

    public void btGetData_onCLick(View view) {
        convertInputText();
        setInputList();
        getDataInput();


        //loadAsyncTaskForGetData();
    }

    private void setInputList() {
        inputList.clear();
        inputList.add(input1);
        inputList.add(input2);

        inputListAux.add(input1);
        inputListAux.add(input2);
    }

    private void getDataInput() {
        loadAsyncTaskForGetDataInput();
    }

    private void loadAsyncTaskForGetDataInput() {
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                getCatalogo();
                //getInputCompanheiras();
                //getInputAntagonicas();
                //mergeLists();
                return 1;
            }

            @Override
            protected void onPreExecute() {
                btGetData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Integer integer) {
                int i = 1;
            }

        }.execute();
    }

    
    private void getCatalogo() {
        compKeyList.clear();
        antaKeyList.clear();
        final AtomicInteger index = new AtomicInteger();
        index.set(0);

        mDatabase.child("catalogo").addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCatalogo2() {
        compKeyList.clear();
        antaKeyList.clear();
        final AtomicInteger index = new AtomicInteger();
        index.set(0);

        for (String planta : inputList) {
            if (planta.equals(""))
                continue;
            mDatabase.child("catalogo").child(planta).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = index.get();
                    count = count + 1;
                    index.lazySet(count);
                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        Catalogo c = Snapshot.getValue(Catalogo.class);

                        String key = c.getKey();
                        String tipo = c.getTipo();

                        if (tipo.equals("C"))
                            compKeyList.add(key);
                        else
                            antaKeyList.add(key);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        while (inputList.size() != index.get()) {
            continue;
        }
        getRelCompanheiras();
    }

    private void getRelCompanheiras() {
        compList.clear();
        mDatabase.child("relCompanheiras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (compKeyList.contains(data.getKey()) == false) {
                        continue;
                    }

                    Relacionamento relacionamento = data.getValue(Relacionamento.class);
                    String item1 = relacionamento.getItem1();
                    String item2 = relacionamento.getItem2();

                    compList.add(item1);
                    compList.add(item2);
                }
                getRelAntagonicas();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getRelAntagonicas() {
        antaList.clear();
        mDatabase.child("relAntagonicas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (antaKeyList.contains(data.getKey()) == false) {
                        continue;
                    }

                    Relacionamento relacionamento = data.getValue(Relacionamento.class);
                    String item1 = relacionamento.getItem1();
                    String item2 = relacionamento.getItem2();

                    antaList.add(item1);
                    antaList.add(item2);
                }
                mergeLists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mergeLists() {
        if (firstTime) {
            //Remove itens duplicados
            compList = new ArrayList<String>(new LinkedHashSet<String>(compList));
            antaList = new ArrayList<String>(new LinkedHashSet<String>(antaList));

            //Remove itens do input da lista
            compList.removeAll(inputList);
            antaList.removeAll(inputList);

            //Remove itens da lista companheiros que existam na lista de antagonicos
            compList.removeAll(antaList);

            antaList.clear();
            inputList.clear();

            if (inputListAux.size() == 1) {
                loadList();
                input1 = "";
                input2 = "";
                return;
            }


            firstTime = false;
            for (String line : compList) {
                inputList.add(line);
            }
            getDataInput();
        } else {
            //Remove itens duplicados
            compList = new ArrayList<String>(new LinkedHashSet<String>(compList));
            antaList = new ArrayList<String>(new LinkedHashSet<String>(antaList));

            //Remove itens da lista companheiros que existam na lista de antagonicos
            compList.removeAll(antaList);
            compList.removeAll(inputListAux);
            Toast.makeText(this, "testeeeee", Toast.LENGTH_SHORT).show();
            loadList();
            input1 = "";
            input2 = "";
        }
    }

    //-------------------------------old but gold
    public void btAddRelCompanheiras_onCLick(View view) {
        convertInputText();

        if (input1.isEmpty() && input2.isEmpty()) {
            Toast.makeText(this, "Campo vazio!", Toast.LENGTH_SHORT).show();
            return;
        }

        loadAsyncTaskForCompanheiras();
    }

    public void loadAsyncTaskForCompanheiras() {
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                saveDBRelCompanheiras();
                return 1;
            }

            @Override
            protected void onPreExecute() {
                btAddRelCompanheiras.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Integer integer) {
                Toast.makeText(getApplicationContext(), "Post Execute!", Toast.LENGTH_SHORT).show();
            }

        }.execute();
    }

    private void saveDBRelCompanheiras() {
        jsonMap.clear();

        jsonMap.put("item1", input1);
        jsonMap.put("item2", input2);

        mDatabase.child("relCompanheiras").push().setValue(jsonMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String strKey = databaseReference.getKey();
                Toast.makeText(getApplicationContext(), "Relacionamento Salvo!", Toast.LENGTH_SHORT).show();
                saveDBCatalogo(strKey, "C");
                btAddRelCompanheiras.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void btAddRelAntagonicas_onCLick(View view) {
        convertInputText();

        if (input1.isEmpty() && input2.isEmpty()) {
            Toast.makeText(this, "Campo vazio!", Toast.LENGTH_SHORT).show();
            return;
        }

        loadAsyncTaskForAntagonicas();
    }

    public void loadAsyncTaskForAntagonicas() {
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                saveDBRelAntagonicas();
                return 1;
            }

            @Override
            protected void onPreExecute() {
                btAddRelAntagonicas.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Integer integer) {
                Toast.makeText(getApplicationContext(), "Post Execute!", Toast.LENGTH_SHORT).show();
            }

        }.execute();
    }

    private void saveDBRelAntagonicas() {
        jsonMap.clear();

        jsonMap.put("item1", input1);
        jsonMap.put("item2", input2);

        mDatabase.child("relAntagonicas").push().setValue(jsonMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String strKey = databaseReference.getKey();
                Toast.makeText(getApplicationContext(), "Relacionamento Salvo!", Toast.LENGTH_SHORT).show();
                saveDBCatalogo(strKey, "A");
                btAddRelAntagonicas.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void saveDBCatalogo(String key, String type) {
        jsonMap.clear();
        jsonMap.put("key", key);
        if (type.equals("C"))
            jsonMap.put("tipo", "C");
        else
            jsonMap.put("tipo", "A");

        mDatabase.child("catalogo").child(input1).push().setValue(jsonMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(), "Catalogo planta 1 Salvo!", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.child("catalogo").child(input2).push().setValue(jsonMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(), "Catalogo planta 2 Salvo!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadAsyncTaskForGetData() {
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                getData();
                return 1;
            }

            @Override
            protected void onPreExecute() {
                btGetData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Integer integer) {
                //Toast.makeText(getApplicationContext(), "Post Execute!", Toast.LENGTH_SHORT).show();
            }

        }.execute();
    }

    private void getData() {
        mDatabase.child("catalogo").child(input1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMap.clear();
                list.clear();
                compList.clear();
                antaList.clear();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Catalogo c = Snapshot.getValue(Catalogo.class);

                    String key = c.getKey();
                    String tipo = c.getTipo();

                    if (tipo.equals("C"))
                        compList.add(key);
                    else
                        antaList.add(key);

                    listMap.put(key, tipo);
                    //list.add(key);
                }
                getItens();
                //loadList();
                //progressBar.setVisibility(View.GONE);
                //btGetData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadList() {
        progressBar.setVisibility(View.GONE);
        btGetData.setVisibility(View.VISIBLE);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, compList);

        lvList = (ListView) findViewById(R.id.lvList);
        lvList.setAdapter(itemsAdapter);
    }

    private void getItens() {
        mDatabase.child("relCompanheiras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Relacionamento relacionamento = data.getValue(Relacionamento.class);
                    String item1 = relacionamento.getItem1();
                    String item2 = relacionamento.getItem2();
                    String key = data.getKey();

                    if (Objects.equals(input1, item1)) {
                        //ok
                    } else {
                        if (Objects.equals(input1, item2)) {
                            //ok
                        } else
                            continue;
                    }

                    if (Objects.equals(input1, item1)) {
                        //testMap.put(key, item2);
                        list.add(item2);
                    } else {
                        //testMap.put(key, item1);
                        list.add(item1);
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


















/*    public void loadAntaList() {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, antaList);

        //lv_antagonicas = (ListView) findViewById(R.id.lv_antagonicas);
        lv_antagonicas.setAdapter(itemsAdapter);
    }*/

    /*public void getData(){
        String plantaMestre = et_planta.getText().toString().toLowerCase();

        if(plantaMestre.isEmpty())
        {
            finalList.clear();
            loadFInalList();
            return;
        }

        mDatabase.child("tabelao").child(plantaMestre).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Catalogo p = Snapshot.getValue(Catalogo.class);

                    String planta = p.getPlanta();
                    String relacao = p.getRelacao();

                    planta = planta.concat(" - " + relacao);
                    finalList.add(planta);
                }
                loadFInalList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadFInalList() {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, finalList);

        lv_final = (ListView) findViewById(R.id.lv_final);
        lv_final.setAdapter(itemsAdapter);
    }*/


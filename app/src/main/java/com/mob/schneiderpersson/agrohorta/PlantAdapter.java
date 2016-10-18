package com.mob.schneiderpersson.agrohorta;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlantAdapter extends BaseAdapter {

    private final Context mContext;
    //ArrayList<Plant>  plants = new ArrayList<>();
    ArrayList<String> plants = new ArrayList<>();
    ArrayList<String> listColor = new ArrayList<>();

    public PlantAdapter(Context context, ArrayList<String> plants, ArrayList<String> color) {
        this.mContext = context;
        this.plants = plants;
        this.listColor = color;
    }

    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //final Plant plant = plants.get(position);
        String p = plants.get(position);

        String c;

        if (!listColor.contains("b")) {
            if (listColor.size() > 0)
                c = listColor.get(position);
            else
                c = "n";
        } else
            c = "b";

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.custom_item, null);
        }

        int color = 0;
        int catalogo = Color.parseColor("#795548");
        int companheiras = Color.parseColor("#4CAF50");
        int antagonicas = Color.parseColor("#f44336");
        int box = Color.parseColor("#388E3C");

        if (c.equals("c"))
            color = companheiras;
        else if (c.equals("a"))
            color = antagonicas;
        else if (c.equals("b"))
            color = companheiras;
        else
            color = catalogo;

        convertView.setBackgroundColor(color);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.tvName);

        //imageView.setImageResource(plant.getImageResource());

        String planta = p;
        if (planta.contains(" ")) {
            planta = planta.replace(" ", "_");
        } else if (planta.contains("-")) {
            planta = planta.replace("-", "_");
        }

        switch (planta) {
            case "abóbora":
                imageView.setImageResource(R.drawable.abobora);
                nameTextView.setText(R.string.abóbora);
                break;
            case "abobrinha":
                imageView.setImageResource(R.drawable.abobrinha);
                nameTextView.setText(R.string.abobrinha);
                break;
            case "acelga":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.acelga);
                break;
            case "agrião":
                imageView.setImageResource(R.drawable.agriao);
                nameTextView.setText(R.string.agrião);
                break;
            case "alecrim":
                imageView.setImageResource(R.drawable.alecrim);
                nameTextView.setText(R.string.alecrim);
                break;
            case "alface":
                imageView.setImageResource(R.drawable.alface);
                nameTextView.setText(R.string.alface);
                break;
            case "alho_poró":
                imageView.setImageResource(R.drawable.alho_poro);
                nameTextView.setText(R.string.alho_poró);
                break;
            case "berinjela":
                imageView.setImageResource(R.drawable.berinjela);
                nameTextView.setText(R.string.berinjela);
                break;
            case "batata_inglesa":
                imageView.setImageResource(R.drawable.batata);
                nameTextView.setText(R.string.batata_inglesa);
                break;
            case "beterraba":
                imageView.setImageResource(R.drawable.beterraba);
                nameTextView.setText(R.string.beterraba);
                break;
            case "camomila":
                imageView.setImageResource(R.drawable.camomila);
                nameTextView.setText(R.string.camomila);
                break;
            case "cebola":
                imageView.setImageResource(R.drawable.cebola);
                nameTextView.setText(R.string.cebola);
                break;
            case "cenoura":
                imageView.setImageResource(R.drawable.cenoura);
                nameTextView.setText(R.string.cenoura);
                break;
            case "chicória":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.chicória);
                break;
            case "cebolinha":
                imageView.setImageResource(R.drawable.cebolinha);
                nameTextView.setText(R.string.cebolinha);
                break;
            case "coentro":
                imageView.setImageResource(R.drawable.coentro);
                nameTextView.setText(R.string.coentro);
                break;
            case "couve":
                imageView.setImageResource(R.drawable.couve);
                nameTextView.setText(R.string.couve);
                break;
            case "couve_flor":
                imageView.setImageResource(R.drawable.couve_flor);
                nameTextView.setText(R.string.couve_flor);
                break;
            case "ervilha":
                imageView.setImageResource(R.drawable.ervilha);
                nameTextView.setText(R.string.ervilha);
                break;
            case "espinafre":
                imageView.setImageResource(R.drawable.espinafre);
                nameTextView.setText(R.string.espinafre);
                break;
            case "feijão":
                imageView.setImageResource(R.drawable.feijao);
                nameTextView.setText(R.string.feijão);
                break;
            case "feijão_vagem":
                imageView.setImageResource(R.drawable.feijao_vagem);
                nameTextView.setText(R.string.feijão_vagem);
                break;
            case "framboesa":
                imageView.setImageResource(R.drawable.framboesa);
                nameTextView.setText(R.string.framboesa);
                break;
            case "girassol":
                imageView.setImageResource(R.drawable.girassol);
                nameTextView.setText(R.string.girassol);
                break;
            case "hortelã":
                imageView.setImageResource(R.drawable.coentro);
                nameTextView.setText(R.string.hortelã);
                break;
            case "manjerona":
                imageView.setImageResource(R.drawable.manjerona);
                nameTextView.setText(R.string.manjerona);
                break;
            case "maçã":
                imageView.setImageResource(R.drawable.maca);
                nameTextView.setText(R.string.maça);
                break;
            case "milho":
                imageView.setImageResource(R.drawable.milho);
                nameTextView.setText(R.string.milho);
                break;
            case "moranga":
                imageView.setImageResource(R.drawable.moranga);
                nameTextView.setText(R.string.moranga);
                break;
            case "morango":
                imageView.setImageResource(R.drawable.morango);
                nameTextView.setText(R.string.morango);
                break;
            case "mostarda":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.mostarda);
                break;
            case "nabo":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.nabo);
                break;
            case "pepino":
                imageView.setImageResource(R.drawable.pepino);
                nameTextView.setText(R.string.pepino);
                break;
            case "quiabo":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.quiabo);
                break;
            case "rabanete":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.rabanete);
                break;
            case "radite":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.radite);
                break;
            case "repolho":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.repolho);
                break;
            case "rúcula":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.rúcula);
                break;
            case "salsa":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.salsa);
                break;
            case "salsão":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.salsão);
                break;
            case "sálvia":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.sálvia);
                break;
            case "tomate":
                imageView.setImageResource(R.drawable.tomate);
                nameTextView.setText(R.string.tomate);
                break;
            case "tomilho":
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(R.string.tomilho);
                break;
            case "vagem":
                imageView.setImageResource(R.drawable.leaf2);
                nameTextView.setText(R.string.vagem);
                break;
            default:
                imageView.setImageResource(R.drawable.leaf1);
                nameTextView.setText(planta);
                break;
        }

        return convertView;
    }

}
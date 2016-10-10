package com.mob.schneiderpersson.agrohortav1;

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
        }
        else
            c = "b";

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.custom_item, null);
        }

        int color = 0;

        if (c.equals("c"))
            color = 0xff00ff00;
        else if (c.equals("a"))
            color = 0xffff0000;
        else if (c.equals("b"))
            color = 0xffcccccc;
        else
            color = 0xff888888;

        convertView.setBackgroundColor(color);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.tvName);

        //imageView.setImageResource(plant.getImageResource());
        imageView.setImageResource(R.drawable.img_hortela);
        nameTextView.setText(p);
        //nameTextView.setText(mContext.getString(plant.getName(position)));

        return convertView;
    }

}
package com.mob.schneiderpersson.agrohortav1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlantAdapter extends BaseAdapter {

    private final Context mContext;
    ArrayList<Plant>  plants = new ArrayList<>();

    public PlantAdapter(Context context, ArrayList<Plant> plants) {
        this.mContext = context;
        this.plants = plants;
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

        final Plant plant = plants.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.custom_item, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.ivImage);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.tvName);

        //imageView.setImageResource(plant.getImageResource());
        imageView.setImageResource(R.drawable.img_hortela);
        nameTextView.setText(plant.getName());
        //nameTextView.setText(mContext.getString(plant.getName(position)));

        return convertView;
    }

}
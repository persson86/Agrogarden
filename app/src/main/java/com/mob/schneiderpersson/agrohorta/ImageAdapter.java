package com.mob.schneiderpersson.agrohorta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    //private final int item_image[];
    //private final String item_text[];

    private final ArrayList<Integer> img = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        //this.img = img;
        nameList = name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);

            // get layout from custom_gridview.xml
            gridView = inflater.inflate(R.layout.custom_gridview, null);

/*            // set value into imageview
            ImageView image = (ImageView) gridView.findViewById(R.id.item_image);
            image.setImageResource(img.get(position));*/

            // set value into textview
            TextView text = (TextView) gridView.findViewById(R.id.item_text);
            text.setText(nameList.get(position));
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

/*
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(340, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 5, 0, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela,
            R.drawable.img_hortela, R.drawable.img_hortela
    };
}
*/

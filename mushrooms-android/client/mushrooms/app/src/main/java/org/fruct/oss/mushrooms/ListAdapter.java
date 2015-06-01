package org.fruct.oss.mushrooms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private final List<Bitmap> images;
    LayoutInflater lInflater;
    DisplayMetrics metrics;
    boolean small;


    public ListAdapter(Context context, List<String> values , List<Bitmap> images, DisplayMetrics metrics, boolean small) {
        super(context, R.layout.catalog_list_item, values);
        this.context = context;
        this.values = values;
        this.images = images;
        this.metrics = metrics;
        this.small = small;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.catalog_list_item, parent, false);
            holder = new ViewHolder();
            holder.textView = ((TextView) view.findViewById(R.id.catalogLabel));
            holder.imageView = ((ImageView) view.findViewById(R.id.catalogImages));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        String item = getItemName(position);

        Bitmap photo = getItemImage(position);

        if (small){

            holder.imageView.setLayoutParams(new LinearLayout.LayoutParams((metrics.widthPixels / 3) / 2, (metrics.widthPixels / 3) / 2));
            holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (metrics.widthPixels / 10) / 2);
            holder.imageView.setPadding(metrics.widthPixels / 100, 0, metrics.widthPixels / 100, 0);
        }else {

            holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels / 3, metrics.widthPixels / 3));
            holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.widthPixels / 15);
            holder.imageView.setPadding(metrics.widthPixels / 50, 0, metrics.widthPixels / 50, 0);
        }

        holder.textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));

        holder.textView.setText(item);

        holder.imageView.setImageBitmap(photo);

        return view;
    }

    public String getItemName(int position){

        return values.get(position);
    }

    public Bitmap getItemImage(int position){

        return images.get(position);
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}

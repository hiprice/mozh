package ir.sadeghzadeh.mozhdegani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import ir.sadeghzadeh.mozhdegani.ApplicationController;
import ir.sadeghzadeh.mozhdegani.Const;
import ir.sadeghzadeh.mozhdegani.R;
import ir.sadeghzadeh.mozhdegani.entity.Item;

/**
 * Created by reza on 11/3/16.
 */
public class ItemsAdapter extends ArrayAdapter<Item>{
    Item[] items;
    private static LayoutInflater inflater = null;

    public ItemsAdapter(Context context, int resource, Item[] items) {
        super(context, resource, items);
        this.items  =  items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if (items == null) {
            return 0;
        }
        return items.length;
    }

    public Item getItem(int index) {
        return items[index];
    }

    public View getView(int position, View paramView, ViewGroup paramViewGroup) {
        Holder holder = new Holder();
        Item item = items[position];
        View rowView=null;
        if(paramView == null){
            rowView = inflater.inflate(R.layout.item_row_item, null);
        }else {
            rowView = paramView;
        }
        holder.title = (TextView) rowView.findViewById(R.id.item_title);
        //holder.mobile= (TextView) rowView.findViewById(R.id.mobile);
        //holder.category = (TextView) rowView.findViewById(R.id.category);
        //holder.description = (TextView) rowView.findViewById(R.id.description);
        holder.date = (TextView) rowView.findViewById(R.id.date);
        holder.city = (TextView) rowView.findViewById(R.id.city);
        holder.thumbnail = (NetworkImageView) rowView.findViewById(R.id.thumbnail);

        //set values
        holder.title.setText(item.Title);
        //holder.mobile.setText(item.Mobile);
        //holder.category.setText(String.valueOf(item.CategoryId));
        //holder.description.setText(item.Description);
        holder.date.setText(item.Date + "");
        holder.city.setText(String.valueOf(item.CityTitle));
        if(item.ImageExt != null && !item.ImageExt.isEmpty()){
            String uri = Const.SERVER_URL + Const.THUMBNAIL_URL + "/" + item.id + item.ImageExt;
            holder.thumbnail.setImageUrl(uri, ApplicationController.getInstance().getImageLoaderInstance());
        }else {
            holder.thumbnail.setImageUrl(null, null);
            holder.thumbnail.setDefaultImageResId(R.drawable.ic_no_photo);
        }
        rowView.setTag(item.id);
        return rowView;
    }

    public class Holder {
        TextView title;
        TextView category;
        TextView description;
        TextView date;
        TextView city;
        NetworkImageView thumbnail;

        public Holder() {
        }
    }





}
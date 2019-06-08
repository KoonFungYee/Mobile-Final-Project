package com.example.findfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomFoodAdapter extends ArrayAdapter<Food> {
    ArrayList<Food> results, tempResult, suggestions;
    private ImageView image;
    private TextView name,price;

    public CustomFoodAdapter(Context context, ArrayList<Food> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.results = objects;
        this.tempResult = new ArrayList<>(objects);
        this.suggestions = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food food = getItem(position);
        View vi=convertView;
        try{
            if(convertView==null){
                vi = LayoutInflater.from(getContext()).inflate(R.layout.auto_textview, parent, false);
                image=vi.findViewById(R.id.imageView18);
                name=vi.findViewById(R.id.textView33);
                price=vi.findViewById(R.id.textView34);
                name.setText(food.getFoodname());
                price.setText(food.getPrice());
                String image_url ="http://www.socstudents.net/findfood/food/"+food.getFoodname()+"_"+food.getRestname()+"_"+food.getState()+".jpg";
                Picasso.get().load(image_url).fit().error(R.mipmap.food).into(image);
            }
            if (position % 2 == 0)
                vi.setBackgroundColor(getContext().getColor(R.color.odd));
            else
                vi.setBackgroundColor(getContext().getColor(R.color.even));

        }catch (IndexOutOfBoundsException e){

        }
        return vi;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Food food = (Food) resultValue;
            return food.getFoodname();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Food rest : tempResult) {
                    if (rest.getFoodname().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(rest);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Food> c = (ArrayList<Food>) results.values;
            if (results != null && results.count > 0) {
                clear();
                notifyDataSetInvalidated();
                for (Food rest : c) {
                    add(rest);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

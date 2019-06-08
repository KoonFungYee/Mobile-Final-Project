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

public class CustomAdapter extends ArrayAdapter<Result> {
    ArrayList<Result> results, tempResult, suggestions;
    private ImageView image,rating1,rating2,rating3,rating4,rating5;
    private TextView name,state,distance;

    public CustomAdapter(Context context, ArrayList<Result> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.results = objects;
        this.tempResult = new ArrayList<>(objects);
        this.suggestions = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result result = getItem(position);
        View vi=convertView;
        try{
            if(convertView==null){
                vi = LayoutInflater.from(getContext()).inflate(R.layout.auto_textview, parent, false);
                image=vi.findViewById(R.id.imageView18);
                name=vi.findViewById(R.id.textView33);
                state=vi.findViewById(R.id.textView34);
                name.setText(result.getName()+"   @"+result.getBuilding());
                state.setText(result.getState());
                String image_url ="http://www.socstudents.net/findfood/restaurant/"+result.getName()+"_"+result.getState()+".jpg";
                Picasso.get().load(image_url).fit().error(R.mipmap.shop).into(image);
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
            Result restaurant = (Result) resultValue;
            return restaurant.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Result rest : tempResult) {
                    if (rest.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            ArrayList<Result> c = (ArrayList<Result>) results.values;
            if (results != null && results.count > 0) {
                clear();
                notifyDataSetInvalidated();
                for (Result rest : c) {
                    add(rest);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

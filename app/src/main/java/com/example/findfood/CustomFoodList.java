package com.example.findfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFoodList extends SimpleAdapter {
    TextView foodname,price,restname,state;

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomFoodList(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.custom_food_list, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            ImageView imageView=vi.findViewById(R.id.imageView52);
            foodname=vi.findViewById(R.id.textView81);
            price=vi.findViewById(R.id.textView84);
            restname=vi.findViewById(R.id.textView82);
            state=vi.findViewById(R.id.textView83);

            String dfoodname=(String) data.get("foodname");
            String dprice=(String) data.get("price");
            String dname=(String) data.get("restname");
            String dbuilding = (String) data.get("building");
            String dstate=(String) data.get("state");

            foodname.setText(dfoodname);
            price.setText("RM"+dprice);
            restname.setText(dname);
            state.setText(dstate);
            String url="http://www.socstudents.net/findfood/food/"+dfoodname+"_"+dname+"_"+dstate+".jpg";
            Picasso.get().load(url).fit().error(R.mipmap.food).into(imageView);


        }catch (IndexOutOfBoundsException e){

        }
        return vi;
    }
}

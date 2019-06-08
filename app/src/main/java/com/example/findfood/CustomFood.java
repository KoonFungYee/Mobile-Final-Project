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

public class CustomFood  extends SimpleAdapter {
    private ImageView star1,star2,star3,star4,star5;

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomFood(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.food_list, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            ImageView picture=vi.findViewById(R.id.imageView46);
            TextView foodname = vi.findViewById(R.id.textView78);
            TextView price=vi.findViewById(R.id.textView80);
            TextView rating=vi.findViewById(R.id.textView87);
            star1=vi.findViewById(R.id.imageView47);
            star2=vi.findViewById(R.id.imageView48);
            star3=vi.findViewById(R.id.imageView49);
            star4=vi.findViewById(R.id.imageView50);
            star5=vi.findViewById(R.id.imageView51);

            String dfoodname=(String) data.get("foodname");
            String drating = (String) data.get("rating");
            String dprice="RM"+ data.get("price");
            String dname=(String) data.get("name");
            String dstate=(String) data.get("state");

            foodname.setText(dfoodname);
            rating.setText(drating);
            price.setText(dprice);
            String url="http://www.socstudents.net/findfood/food/"+dfoodname+"_"+dname+"_"+dstate+".jpg";
            Picasso.get().load(url).fit().error(R.mipmap.food).into(picture);

            Double rating1 = Double.parseDouble(drating);
            if (rating1>=4.75){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.full);
                star4.setImageResource(R.mipmap.full);
                star5.setImageResource(R.mipmap.full);
            }else if (rating1>=4.25){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.full);
                star4.setImageResource(R.mipmap.full);
                star5.setImageResource(R.mipmap.half);
            }else if (rating1>=3.75){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.full);
                star4.setImageResource(R.mipmap.full);
            }else if (rating1>=3.25){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.full);
                star4.setImageResource(R.mipmap.half);
            }else if (rating1>=2.75){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.full);
            }else if (rating1>=2.25){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
                star3.setImageResource(R.mipmap.half);
            }else if (rating1>=1.75){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.full);
            }else if (rating1>=1.25){
                star1.setImageResource(R.mipmap.full);
                star2.setImageResource(R.mipmap.half);
            }else if (rating1>=0.75){
                star1.setImageResource(R.mipmap.full);
            }else if (rating1>=0.25){
                star1.setImageResource(R.mipmap.half);
            }

        }catch (IndexOutOfBoundsException e){

        }
        return vi;
    }
}

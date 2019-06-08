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

public class CustomResult  extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomResult(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.restaurant_list, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView name = vi.findViewById(R.id.textView28);
            TextView state = vi.findViewById(R.id.textView30);
            TextView distance = vi.findViewById(R.id.textView32);
            TextView rating=vi.findViewById(R.id.textView35);
            ImageView star1=vi.findViewById(R.id.imageView13);
            ImageView star2=vi.findViewById(R.id.imageView14);
            ImageView star3=vi.findViewById(R.id.imageView15);
            ImageView star4=vi.findViewById(R.id.imageView16);
            ImageView star5=vi.findViewById(R.id.imageView17);
            ImageView image=vi.findViewById(R.id.imageView12);

            String dname = (String) data.get("name");
            String dbuilding=(String) data.get("building");
            String dstate = (String) data.get("state");
            String ddistance = (String) data.get("distance");
            String drating = (String) data.get("rating");
            name.setText(dname+" @"+dbuilding);
            state.setText(dstate);
            distance.setText(ddistance);
            rating.setText(drating);
            String url="http://www.socstudents.net/findfood/restaurant/"+dname+"_"+dstate+".jpg";
            Picasso.get().load(url).fit().error(R.mipmap.shop).into(image);

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

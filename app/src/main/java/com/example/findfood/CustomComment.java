package com.example.findfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomComment extends SimpleAdapter {
    private ImageView star1,star2,star3,star4,star5;
    private TextView name,rating,date,comment;

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomComment(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.comment_list, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            name=vi.findViewById(R.id.textView92);
            rating=vi.findViewById(R.id.textView94);
            date=vi.findViewById(R.id.textView93);
            comment=vi.findViewById(R.id.textView95);
            star1=vi.findViewById(R.id.imageView54);
            star2=vi.findViewById(R.id.imageView55);
            star3=vi.findViewById(R.id.imageView56);
            star4=vi.findViewById(R.id.imageView57);
            star5=vi.findViewById(R.id.imageView58);

            String dname=(String) data.get("name");
            String drating=(String) data.get("rating");
            String ddate=(String) data.get("date");
            String dcomment=(String) data.get("comment");

            name.setText(dname);
            rating.setText(drating);
            date.setText(ddate);
            comment.setText(dcomment);
            comment.setEnabled(false);
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

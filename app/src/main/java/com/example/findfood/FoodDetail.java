package com.example.findfood;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FoodDetail extends AppCompatActivity {
    private Dialog myDialogWindow;
    private Toolbar toolbar;
    private String foodname,price,period,remarks,rating,rating1,restname,state,drating,commentDate,person;
    private ImageView image,star1,star2,star3,star4,star5,comment;
    private TextView name,foodprice,time,remark,title,tvrating,noComment;
    private ListView list;
    private ArrayList<HashMap<String, String>> resultlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        toolbar = findViewById(R.id.foodbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        foodname = bundle.getString("foodname");
        price = bundle.getString("price");
        period = bundle.getString("period");
        remarks = bundle.getString("remarks");
        rating = bundle.getString("rating");
        restname = bundle.getString("restname");
        state = bundle.getString("state");
        initial();
        setting();
        checkTitle();
        checkName(SharedPreferenceConfig.getEmail(this));
        checkComment();

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogWindow = new Dialog(FoodDetail.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                myDialogWindow.setContentView(R.layout.new_comment);
                final EditText comment=myDialogWindow.findViewById(R.id.editText19);
                final ImageView star1,star2,star3,star4,star5;
                star1 = myDialogWindow.findViewById(R.id.imageView41);
                star2 = myDialogWindow.findViewById(R.id.imageView42);
                star3 = myDialogWindow.findViewById(R.id.imageView43);
                star4 = myDialogWindow.findViewById(R.id.imageView44);
                star5 = myDialogWindow.findViewById(R.id.imageView45);
                final Button submit = myDialogWindow.findViewById(R.id.button7);
                star1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drating="1.00";
                        star1.setImageResource(R.mipmap.full);
                        star2.setImageResource(R.mipmap.no);
                        star3.setImageResource(R.mipmap.no);
                        star4.setImageResource(R.mipmap.no);
                        star5.setImageResource(R.mipmap.no);
                    }
                });
                star2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drating="2.00";
                        star1.setImageResource(R.mipmap.full);
                        star2.setImageResource(R.mipmap.full);
                        star3.setImageResource(R.mipmap.no);
                        star4.setImageResource(R.mipmap.no);
                        star5.setImageResource(R.mipmap.no);
                    }
                });
                star3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drating="3.00";
                        star1.setImageResource(R.mipmap.full);
                        star2.setImageResource(R.mipmap.full);
                        star3.setImageResource(R.mipmap.full);
                        star4.setImageResource(R.mipmap.no);
                        star5.setImageResource(R.mipmap.no);
                    }
                });
                star4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drating="4.00";
                        star1.setImageResource(R.mipmap.full);
                        star2.setImageResource(R.mipmap.full);
                        star3.setImageResource(R.mipmap.full);
                        star4.setImageResource(R.mipmap.full);
                        star5.setImageResource(R.mipmap.no);
                    }
                });
                star5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drating="5.00";
                        star1.setImageResource(R.mipmap.full);
                        star2.setImageResource(R.mipmap.full);
                        star3.setImageResource(R.mipmap.full);
                        star4.setImageResource(R.mipmap.full);
                        star5.setImageResource(R.mipmap.full);
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment1 = comment.getText().toString();
                        try{
                            if (comment1.equals("")||drating.equals("0")){
                                Toast.makeText(FoodDetail.this, "Please write comment and give rating star", Toast.LENGTH_SHORT).show();
                            }else {
                                saveComment(person,drating,comment1,foodname,restname,state);
                                noComment.setVisibility(View.GONE);
                            }
                        }catch (Exception ex){
                            Toast.makeText(FoodDetail.this, "Please give rating star", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myDialogWindow.show();
            }
        });
    }

    private void checkComment() {
        class CheckComment extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                resultlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_comment.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("0")){
                    noComment.setVisibility(View.VISIBLE);
                }else {
                    noComment.setVisibility(View.GONE);
                    loadList();
                }
            }
        }
        CheckComment checkComment = new CheckComment();
        checkComment.execute();
    }

    private void saveComment(final String person, final String drating, final String comment1, final String foodname,
                             final String restname, final String state) {
        class SaveComment extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                commentDate=dateFormat.format(date);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",person);
                hashMap.put("rating",drating);
                hashMap.put("comment",comment1);
                hashMap.put("date",commentDate);
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/comment.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loadList();
                myDialogWindow.dismiss();
            }
        }
        SaveComment saveComment = new SaveComment();
        saveComment.execute();
    }

    private void checkName(final String email) {
        class CheckName extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_name.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                person=s;
            }
        }
        CheckName checkName = new CheckName();
        checkName.execute();
    }

    private void checkTitle() {
        class CheckTitle extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_food_title.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("available")){
                    title.setText("Food Details");
                    title.setTextColor(getResources().getColor(R.color.normal));
                }else if (s.equals("unavailable")){
                    title.setText("This Food Is Not Available");
                    title.setTextColor(getResources().getColor(R.color.warning));
                }
            }
        }
        CheckTitle checkTitle = new CheckTitle();
        checkTitle.execute();
    }

    private void loadList() {
        class LoadResultList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                resultlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/comment_list.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                resultlist.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        String name = c.getString("name");
                        String rating = c.getString("rating");
                        String comment = c.getString("comment");
                        String date = c.getString("date");

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("name",name);
                        resultlisthash.put("rating",rating);
                        resultlisthash.put("comment",comment);
                        resultlisthash.put("date",date);
                        resultlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                ListAdapter adapter = new CustomComment(
                        FoodDetail.this, resultlist,
                        R.layout.comment_list, new String[]
                        {"name","rating","comment","date"}, new int[]
                        {R.id.textView92,R.id.textView94,R.id.textView95,R.id.textView93});
                list.setAdapter(adapter);
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    private void setting() {
        String url="http://www.socstudents.net/findfood/food/"+foodname+"_"+restname+"_"+state+".jpg";
        Picasso.get().load(url).fit().error(R.mipmap.food).into(image);
        name.setText(foodname);
        foodprice.setText("RM"+price);
        time.setText(period);
        tvrating.setText(rating);
        if (remarks.equals("")){
            remark.setText("-");
        }else {
            remark.setText(remarks);
        }
        Double rating1 = Double.parseDouble(rating);
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
    }

    private void initial() {
        image=findViewById(R.id.imageView34);
        star1=findViewById(R.id.imageView36);
        star2=findViewById(R.id.imageView37);
        star3=findViewById(R.id.imageView38);
        star4=findViewById(R.id.imageView39);
        star5=findViewById(R.id.imageView40);
        name=findViewById(R.id.textView69);
        foodprice=findViewById(R.id.textView70);
        time=findViewById(R.id.textView74);
        remark=findViewById(R.id.textView73);
        comment=findViewById(R.id.imageView35);
        list=findViewById(R.id.commentlist);
        title=findViewById(R.id.textView62);
        tvrating=findViewById(R.id.textView91);
        noComment=findViewById(R.id.textView97);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Bundle bundle;
        switch (item.getItemId()) {
            case R.id.editfood:
                intent=new Intent(this,EditFood.class);
                bundle = new Bundle();
                bundle.putString("foodname",foodname);
                bundle.putString("price",price);
                bundle.putString("period",period);
                bundle.putString("remarks",remarks);
                bundle.putString("rating",rating);
                bundle.putString("restname",restname);
                bundle.putString("state",state);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.foodunavailable:
                title.setText("This Food Is Not Available");
                title.setTextColor(getResources().getColor(R.color.warning));
                changeFoodStatus(foodname,restname,state,"unavailable");
                return true;

            case R.id.foodavailable:
                title.setText("Food Details");
                title.setTextColor(getResources().getColor(R.color.normal));
                changeFoodStatus(foodname,restname,state,"available");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeFoodStatus(final String foodname, final String restname, final String state, final String unavailable) {
        class ChangeStatus extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                hashMap.put("status",unavailable);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/change_food_status.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {

                }else{
                    Toast.makeText(FoodDetail.this, "Error Occur", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ChangeStatus changeStatus = new ChangeStatus();
        changeStatus.execute();
    }
}

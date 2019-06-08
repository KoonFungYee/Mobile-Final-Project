package com.example.findfood;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantDetail extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private TextView title, restname, time, distance, rating, telno, fbname,noFood;
    private ImageView image, star1, star2, star3, star4, star5, go;
    private String name,state1,fbpage,rating1,distance1,building,latitude1,longtitude1,starttime,endtime,locationPrefer;
    private Double longtitude, latitude;
    private MapView mapView;
    private ListView foodlist;
    private View fb, tel;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ArrayList<HashMap<String, String>> resultlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        toolbar = findViewById(R.id.restaurantbar);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.bottomNavigationView5);
        navigation.setOnNavigationItemSelectedListener(navListener);
        initial();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        locationPrefer= bundle.getString("locationPrefer");
        building = bundle.getString("building");
        state1 = bundle.getString("state");
        rating1 = bundle.getString("rating");
        distance1 = bundle.getString("distance");
        final String telnumber= bundle.getString("telnumber");
        fbpage= bundle.getString("fbpage");
        starttime=bundle.getString("starttime");
        endtime=bundle.getString("endtime");
        latitude1 = bundle.getString("restlat");
        longtitude1 = bundle.getString("restlong");
        latitude = Double.parseDouble(latitude1);
        longtitude = Double.parseDouble(longtitude1);
        checkTitle(name, state1);
        setView(name, building, starttime, endtime, rating1, distance1, telnumber, fbpage);
        checkFood();
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude1 + "," + longtitude1);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(RestaurantDetail.this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+6"+telnumber, null));
                startActivity(intent);
            }
        });
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        foodlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantDetail.this,FoodDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("foodname",resultlist.get(position).get("foodname"));
                bundle.putString("price",resultlist.get(position).get("price"));
                bundle.putString("period",resultlist.get(position).get("period"));
                bundle.putString("remarks",resultlist.get(position).get("remarks"));
                bundle.putString("rating",resultlist.get(position).get("rating"));
                bundle.putString("restname",resultlist.get(position).get("name"));
                bundle.putString("state",state1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void checkFood() {
        class CheckFood extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("state",state1);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("0")){
                    noFood.setVisibility(View.VISIBLE);
                }else {
                    noFood.setVisibility(View.GONE);
                    loadResultList();
                }
            }
        }
        CheckFood checkFood = new CheckFood();
        checkFood.execute();
    }

    private void loadResultList() {
        class LoadResultList extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                resultlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("state",state1);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_food_result.php",hashMap);
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
                        String foodname = c.getString("foodname");
                        String price = c.getString("price");
                        String period = c.getString("period");
                        String remarks = c.getString("remarks");
                        String rating = c.getString("rating");

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("foodname",foodname);
                        resultlisthash.put("price",price);
                        resultlisthash.put("period",period);
                        resultlisthash.put("remarks",remarks);
                        resultlisthash.put("rating",rating);
                        resultlisthash.put("name",name);
                        resultlisthash.put("state",state1);
                        resultlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                ListAdapter adapter = new CustomFood(
                        RestaurantDetail.this, resultlist,
                        R.layout.food_list, new String[]
                        {"foodname","price"}, new int[]
                        {R.id.textView78,R.id.textView80});
                foodlist.setAdapter(adapter);
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=https://www.facebook.com/"+fbpage;
            } else { //older versions of fb app
                return "fb://page/" + fbpage;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "https://www.facebook.com/"+fbpage; //normal web url
        }
    }

    private void checkTitle(final String name, final String state1) {
        class CheckTitle extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("state",state1);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_title.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("available")){
                    title.setText("Restaurant Profile");
                    title.setTextColor(getResources().getColor(R.color.normal));
                }else if (s.equals("unavailable")){
                    title.setText("This Restaurant Is Unavailable");
                    title.setTextColor(getResources().getColor(R.color.warning));
                }
            }
        }
        CheckTitle checkTitle = new CheckTitle();
        checkTitle.execute();
    }

    private void setView(String name, String building, String starttime, String endtime, String rating2, String distance1,
                         String telnumber, String fbpage) {
        String dname=name+" @"+building;
        time.setText(starttime+" - "+endtime);
        restname.setText(dname);
        distance.setText(distance1);
        rating.setText(rating2);
        telno.setText(telnumber);
        fbname.setText(fbpage);
        String url="http://www.socstudents.net/findfood/restaurant/"+name+"_"+state1+".jpg";
        Picasso.get().load(url).fit().error(R.mipmap.shop).into(image);

        Double rating1 = Double.parseDouble(rating2);
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
        title=findViewById(R.id.textView36);
        image=findViewById(R.id.imageView19);
        restname=findViewById(R.id.textView39);
        time=findViewById(R.id.textView61);
        distance=findViewById(R.id.textView41);
        rating=findViewById(R.id.textView42);
        star1=findViewById(R.id.imageView20);
        star2=findViewById(R.id.imageView21);
        star3=findViewById(R.id.imageView22);
        star4=findViewById(R.id.imageView23);
        star5=findViewById(R.id.imageView24);
        mapView=findViewById(R.id.mapViewRest);
        foodlist=findViewById(R.id.foodList1);
        go=findViewById(R.id.imageView29);
        fb=findViewById(R.id.fbview);
        tel=findViewById(R.id.telview);
        telno=findViewById(R.id.textView59);
        fbname=findViewById(R.id.textView60);
        noFood=findViewById(R.id.textView96);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        final LatLng restaurantposition = new LatLng(latitude,longtitude);
        map.addMarker(new MarkerOptions().position(restaurantposition).title(name));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantposition,14));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                new RestaurantLocation(name,String.valueOf(latitude),String.valueOf(longtitude));
                startActivity(new Intent(RestaurantDetail.this,MapsActivity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Bundle bundle;
        switch (item.getItemId()) {
            case (R.id.newfood):
                intent=new Intent(this,NewFood.class);
                bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("state",state1);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.editrestaurant:
                intent=new Intent(this,EditRestaurant.class);
                bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("state",state1);
                bundle.putString("rating",rating1);
                bundle.putString("distance",distance1);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.unavailable:
                title.setText("This Restaurant Not Available");
                title.setTextColor(getResources().getColor(R.color.warning));
                changeStatus(name,state1,"unavailable");
                return true;

            case R.id.available:
                title.setText("Restaurant Profile");
                title.setTextColor(getResources().getColor(R.color.normal));
                changeStatus(name,state1,"available");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeStatus(final String name, final String state, final String status) {
        class ChangeStatus extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("state",state);
                hashMap.put("status",status);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/change_status.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {

                }else{
                    Toast.makeText(RestaurantDetail.this, "Error Occur", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ChangeStatus changeStatus = new ChangeStatus();
        changeStatus.execute();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(RestaurantDetail.this, MainActivity.class));
                    break;

                case R.id.aroundme:
                    Intent intent=new Intent(RestaurantDetail.this,AroundMe.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("locationPrefer",locationPrefer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.profile:
                    startActivity(new Intent(RestaurantDetail.this, profile.class));
                    break;
            }
            return true;
        }
    };
}

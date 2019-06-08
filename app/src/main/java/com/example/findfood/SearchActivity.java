package com.example.findfood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private Toolbar toolbarSearch;
    private CustomAdapter adapter = null;
    private CustomFoodAdapter adapter1 = null;
    private AutoCompleteTextView autoCompleteTextView;
    private RadioButton food, restaurant,temporary;
    private RadioGroup radioGroup;
    private ImageView seachresult,refresh;
    private ArrayList<HashMap<String, String>> resultlist;
    private ArrayList<Result> results;
    private ArrayList<Food> result;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String longtitude,latitude, location5, dlatitude,dlongtitude;
    private ListView listView;
    private Double latitude1,longtitude1;
    private TextView noResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        location5=bundle.getString("location");
        dlocation(SharedPreferenceConfig.getEmail(this));
        navigation = findViewById(R.id.bottomNavigationView2);
        navigation.setOnNavigationItemSelectedListener(navListener);
        toolbarSearch = findViewById(R.id.searchresult);
        setSupportActionBar(toolbarSearch);
        seachresult = findViewById(R.id.search);
        refresh=findViewById(R.id.imageView25);
        radioGroup = findViewById(R.id.radioGroup3);
        noResult=findViewById(R.id.textView105);
        noResult.setVisibility(View.GONE);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setEnabled(false);
        food = findViewById(R.id.radioButton4);
        restaurant = findViewById(R.id.radioButton3);
        listView=findViewById(R.id.resultList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (resultlist.get(0).get("category").toLowerCase().equals("restaurant")){
                    Intent intent = new Intent(SearchActivity.this,RestaurantDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name",resultlist.get(position).get("name"));
                    bundle.putString("building",resultlist.get(position).get("building"));
                    bundle.putString("state",resultlist.get(position).get("state"));
                    bundle.putString("rating",resultlist.get(position).get("rating"));
                    bundle.putString("category",resultlist.get(position).get("category"));
                    bundle.putString("distance",resultlist.get(position).get("distance"));
                    bundle.putString("restlat",String.valueOf(latitude1));
                    bundle.putString("restlong",String.valueOf(longtitude1));
                    bundle.putString("telnumber",resultlist.get(position).get("telnumber"));
                    bundle.putString("fbpage",resultlist.get(position).get("fbpage"));
                    bundle.putString("starttime",resultlist.get(position).get("starttime"));
                    bundle.putString("endtime",resultlist.get(position).get("endtime"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if (resultlist.get(0).get("category").toLowerCase().equals("food")){
                    Intent intent = new Intent(SearchActivity.this,FoodDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("foodname",resultlist.get(position).get("foodname"));
                    bundle.putString("price",resultlist.get(position).get("price"));
                    bundle.putString("period",resultlist.get(position).get("period"));
                    bundle.putString("remarks",resultlist.get(position).get("remarks"));
                    bundle.putString("rating",resultlist.get(position).get("rating"));
                    bundle.putString("restname",resultlist.get(position).get("restname"));
                    bundle.putString("state",resultlist.get(position).get("state"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            latitude=String.valueOf(location.getLatitude());
                            longtitude=String.valueOf(location.getLongitude());
                            currentLocation(longtitude,latitude,SharedPreferenceConfig.getEmail(SearchActivity.this));
                        } else {
                            Toast.makeText(SearchActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                    }
                };
                if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    SearchActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET}, 1);
                    return;
                } else {
                    getLocation();
                }
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setHint("You can search now");
                autoCompleteTextView.setEnabled(true);
                loadFoodResult("food");
            }
        });
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setHint("You can search now");
                autoCompleteTextView.setEnabled(true);
                loadResult("restaurant");
            }
        });
        seachresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    noResult.setVisibility(View.GONE);
                    String result = autoCompleteTextView.getText().toString();
                    int selectedID=radioGroup.getCheckedRadioButtonId();
                    temporary=findViewById(selectedID);
                    String category=temporary.getText().toString();
                    if (result.equals("")){
                        Toast.makeText(SearchActivity.this, "Please key in anything", Toast.LENGTH_SHORT).show();
                    }else if (category.equals("Restaurant")){
                        loadResultList(result);
                    }else {
                        loadFoodList(result);
                    }

                }catch (Exception e){
                    Toast.makeText(SearchActivity.this, "Please choose the category", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadFoodResult(final String search) {
        class LoadFoodResult extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                result=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("search",search);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_all_food.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                result.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        String foodname = c.getString("foodname");
                        String price = c.getString("price");
                        String restname = c.getString("restname");
                        String state = c.getString("state");
                        result.add(new Food(foodname,price,restname,state));
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                adapter1 = new CustomFoodAdapter(SearchActivity.this, result);
                autoCompleteTextView.setAdapter(adapter1);
            }
        }
        LoadFoodResult loadFoodResult = new LoadFoodResult();
        loadFoodResult.execute();
    }

    private void loadFoodList(final String result) {
        class LoadFoodList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                resultlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",result);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_food_list.php",hashMap);
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
                        String price = c.getString("price");
                        String period = c.getString("period");
                        String remarks = c.getString("remarks");
                        String rating = c.getString("rating");
                        String restname=c.getString("restname");
                        String state=c.getString("state");

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("foodname",result);
                        resultlisthash.put("price",price);
                        resultlisthash.put("period",period);
                        resultlisthash.put("remarks",remarks);
                        resultlisthash.put("rating",rating);
                        resultlisthash.put("restname",restname);
                        resultlisthash.put("state",state);
                        resultlisthash.put("category","food");
                        resultlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    noResult.setVisibility(View.VISIBLE);
                }
                ListAdapter adapter = new CustomFoodList(
                        SearchActivity.this, resultlist,
                        R.layout.custom_food_list, new String[]
                        {"foodname","price"}, new int[]
                        {R.id.textView81,R.id.textView84});
                listView.setAdapter(adapter);
            }
        }
        LoadFoodList loadFoodList = new LoadFoodList();
        loadFoodList.execute();
    }

    private void dlocation(final String email) {
        class LoadResultList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/current_position.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("position");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        dlatitude = c.getString("latitude");
                        dlongtitude = c.getString("longtitude");
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    private void loadResultList(final String result) {
        class LoadResultList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                resultlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",result);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_result.php",hashMap);
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
                        String building = c.getString("building");
                        String state = c.getString("state");
                        String rating = c.getString("rating");
                        String category=c.getString("category");
                        String telnumber=c.getString("telnumber");
                        String fbpage=c.getString("fbpage");
                        String starttime=c.getString("starttime");
                        String endtime=c.getString("endtime");
                        longtitude1 = Double.parseDouble(c.getString("longtitude"));
                        latitude1 = Double.parseDouble(c.getString("latitude"));
                        Double longtitude2=Double.parseDouble(dlongtitude);
                        Double latitude2=Double.parseDouble(dlatitude);
                        String distance=distance(latitude1,longtitude1,latitude2,longtitude2)+"KM";

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("name",name);
                        resultlisthash.put("building",building);
                        resultlisthash.put("state",state);
                        resultlisthash.put("rating",rating);
                        resultlisthash.put("distance",distance);
                        resultlisthash.put("category",category);
                        resultlisthash.put("telnumber",telnumber);
                        resultlisthash.put("fbpage",fbpage);
                        resultlisthash.put("starttime",starttime);
                        resultlisthash.put("endtime",endtime);
                        resultlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    noResult.setVisibility(View.VISIBLE);
                }
                ListAdapter adapter = new CustomResult(
                        SearchActivity.this, resultlist,
                        R.layout.restaurant_list, new String[]
                        {"name","state","distance","rating"}, new int[]
                        {R.id.textView28,R.id.textView30,R.id.textView32,R.id.textView35});
                listView.setAdapter(adapter);
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    private String distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        double dist1 = Math.acos(dist);
        double dist2 = Math.toDegrees(dist1);
        double dist3 = dist2 * 60 * 1.1515;
        double dist4 = dist3 * 1.609344;
        int distance=(int)Math.round(dist4);
        return ""+distance;
    }

    private void currentLocation(final String longtitude, final String latitude, final String email) {
        class CurrentLocation extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("longtitude",longtitude);
                hashMap.put("latitude",latitude);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/save_current_location.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {

                }else{
                    Toast.makeText(SearchActivity.this, "GPS open failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        CurrentLocation currentLocation1 = new CurrentLocation();
        currentLocation1.execute();

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        Intent intent=new Intent(SearchActivity.this,SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("location",location5);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                    return;
                }else {
                    Toast.makeText(this, "You must open gps", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SearchActivity.this,SearchActivity.class));
                    finish();
                }
                break;
        }
    }

    private void loadResult(final String search) {
        class LoadResult extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                results=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("search",search);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_all.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                results.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        String name = c.getString("name");
                        String building = c.getString("building");
                        String state = c.getString("state");
                        String rating = c.getString("rating");
                        String longtitude = c.getString("longtitude");
                        String latitude = c.getString("latitude");
                        results.add(new Result(name,building,state,rating,longtitude,latitude));
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                adapter = new CustomAdapter(SearchActivity.this, results);
                autoCompleteTextView.setAdapter(adapter);
            }
        }
        LoadResult loadResult = new LoadResult();
        loadResult.execute();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(SearchActivity.this, MainActivity.class));
                    break;

                case R.id.aroundme:
                    Intent intent=new Intent(SearchActivity.this,AroundMe.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location5);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.profile:
                    startActivity(new Intent(SearchActivity.this, profile.class));
                    break;
            }
            return true;
        }
    };
}

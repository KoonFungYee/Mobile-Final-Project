package com.example.findfood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spinner;
    private BottomNavigationView navigation;
    private ImageView search, trending, buffet, western, bbq, halal, chinese, india, japanese;
    private TextView tvtrending, tvbuffet, tvwestern, tvbbq, tvhalal, tvchinese, tvindia,tvjapanese,restNo,allNo;
    private String trendingNo, buffetNo, westernNo, bbqNo, halalNo, chineseNo, indiaNo, japaneseNo, location="",locationPrefer;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Double longtitude,latitude,longtitude1,latitude1;
    private ListView listTop10,listAll;
    private ArrayList<HashMap<String, String>> restaurantlist, alllist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.searchBar);
        setSupportActionBar(toolbar);
        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("location",location);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(navListener);
        location=checkLoc(SharedPreferenceConfig.getEmail(MainActivity.this));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getGPS();
        TabHost tabHost=findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("First");
        TabHost.TabSpec tab2=tabHost.newTabSpec("Second");
        TabHost.TabSpec tab3=tabHost.newTabSpec("Third");

        tab1.setIndicator("Categories");
        tab1.setContent(R.id.layout1);

        tab2.setIndicator("Top 10");
        tab2.setContent(R.id.layout2);

        tab3.setIndicator("All");
        tab3.setContent(R.id.layout3);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        initial();
        checkPreferLoc(SharedPreferenceConfig.getEmail(MainActivity.this));
        btnOnClick();
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                setRestaurant("Johor");
                                checkRestNumber("Johor");
                                checkRestaurant("Johor");
                                break;
                            case 1:
                                setRestaurant("Labuan");
                                checkRestNumber("Labuan");
                                checkRestaurant("Labuan");
                                break;
                            case 2:
                                setRestaurant("Kedah");
                                checkRestNumber("Kedah");
                                checkRestaurant("Kedah");
                                break;
                            case 3:
                                setRestaurant("Kelantan");
                                checkRestNumber("Kelantan");
                                checkRestaurant("Kelantan");
                                break;
                            case 4:
                                setRestaurant("Kuala Lumpur");
                                checkRestNumber("Kuala Lumpur");
                                checkRestaurant("Kuala Lumpur");
                                break;
                            case 5:
                                setRestaurant("Melaka");
                                checkRestNumber("Melaka");
                                checkRestaurant("Melaka");
                                break;
                            case 6:
                                setRestaurant("Pahang");
                                checkRestNumber("Pahang");
                                checkRestaurant("Pahang");
                                break;
                            case 7:
                                setRestaurant("Perak");
                                checkRestNumber("Perak");
                                checkRestaurant("Perak");
                                break;
                            case 8:
                                setRestaurant("Perlis");
                                checkRestNumber("Perlis");
                                checkRestaurant("Perlis");
                                break;
                            case 9:
                                setRestaurant("Pinang");
                                checkRestNumber("Pinang");
                                checkRestaurant("Pinang");
                                break;
                            case 10:
                                setRestaurant("Sabah");
                                checkRestNumber("Sabah");
                                checkRestaurant("Sabah");
                                break;
                            case 11:
                                setRestaurant("Sarawak");
                                checkRestNumber("Sarawak");
                                checkRestaurant("Sarawak");
                                break;
                            case 12:
                                setRestaurant("Selangor");
                                checkRestNumber("Selangor");
                                checkRestaurant("Selangor");
                                break;
                            case 13:
                                setRestaurant("Terengganu");
                                checkRestNumber("Terengganu");
                                checkRestaurant("Terengganu");
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        listTop10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,RestaurantDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",restaurantlist.get(position).get("name"));
                bundle.putString("building",restaurantlist.get(position).get("building"));
                bundle.putString("state",restaurantlist.get(position).get("state"));
                bundle.putString("starttime",restaurantlist.get(position).get("starttime"));
                bundle.putString("endtime",restaurantlist.get(position).get("endtime"));
                bundle.putString("rating",restaurantlist.get(position).get("rating"));
                bundle.putString("category",restaurantlist.get(position).get("category"));
                bundle.putString("distance",restaurantlist.get(position).get("distance"));
                bundle.putString("telnumber",restaurantlist.get(position).get("telnumber"));
                bundle.putString("fbpage",restaurantlist.get(position).get("fbpage"));
                bundle.putString("restlat",String.valueOf(latitude));
                bundle.putString("restlong",String.valueOf(longtitude));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,RestaurantDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",alllist.get(position).get("name"));
                bundle.putString("building",alllist.get(position).get("building"));
                bundle.putString("state",alllist.get(position).get("state"));
                bundle.putString("starttime",alllist.get(position).get("starttime"));
                bundle.putString("endtime",alllist.get(position).get("endtime"));
                bundle.putString("rating",alllist.get(position).get("rating"));
                bundle.putString("category",alllist.get(position).get("category"));
                bundle.putString("distance",alllist.get(position).get("distance"));
                bundle.putString("telnumber",alllist.get(position).get("telnumber"));
                bundle.putString("fbpage",alllist.get(position).get("fbpage"));
                bundle.putString("restlat",String.valueOf(latitude));
                bundle.putString("restlong",String.valueOf(longtitude));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadAll(final String state) {
        class LoadAll extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                alllist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/all_restaurant.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                alllist.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        String name = c.getString("name");
                        String building = c.getString("building");
                        String state = c.getString("state");
                        String rating = c.getString("rating");
                        Double longtitude3 = Double.parseDouble(c.getString("longtitude"));
                        Double latitude3 = Double.parseDouble(c.getString("latitude"));
//                        String distance=distance(latitude,longtitude,latitude3,longtitude3)+"KM";
                        String restaurant="restaurant";
                        String starttime = c.getString("starttime");
                        String endtime = c.getString("endtime");
                        String telnumber = c.getString("telnumber");
                        String fbpage = c.getString("fbpage");

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("name",name);
                        resultlisthash.put("building",building);
                        resultlisthash.put("state",state);
                        resultlisthash.put("rating",rating);
//                        resultlisthash.put("distance",distance);
                        resultlisthash.put("category",restaurant);
                        resultlisthash.put("restlong",""+longtitude3);
                        resultlisthash.put("restlat",""+latitude3);
                        resultlisthash.put("starttime",starttime);
                        resultlisthash.put("endtime",endtime);
                        resultlisthash.put("telnumber",telnumber);
                        resultlisthash.put("fbpage",fbpage);
                        alllist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                ListAdapter adapter = new CustomResult(
                        MainActivity.this, alllist,
                        R.layout.restaurant_list, new String[]
                        {"name","state","distance","rating"}, new int[]
                        {R.id.textView28,R.id.textView30,R.id.textView32,R.id.textView35});
                listAll.setAdapter(adapter);
            }
        }
        LoadAll loadAll = new LoadAll();
        loadAll.execute();
    }

    private void checkRestaurant(final String state) {
        class CheckRestaurant extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_restaurant.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("0")){
                    allNo.setVisibility(View.VISIBLE);
                    listAll.setVisibility(View.GONE);
                }else {
                    allNo.setVisibility(View.GONE);
                    listAll.setVisibility(View.VISIBLE);
                    loadAll(state);
                }
            }
        }
        CheckRestaurant checkRestaurant = new CheckRestaurant();
        checkRestaurant.execute();
    }

    private void checkRestNumber(final String state) {
        class CheckRestaurant extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_restaurant.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("0")){
                    restNo.setVisibility(View.VISIBLE);
                    listTop10.setVisibility(View.GONE);
                }else {
                    restNo.setVisibility(View.GONE);
                    listTop10.setVisibility(View.VISIBLE);
                    loadResultList(state);
                }
            }
        }
        CheckRestaurant checkRestaurant = new CheckRestaurant();
        checkRestaurant.execute();
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

    private void loadResultList(final String state) {
        class LoadResultList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                restaurantlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/top10.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                restaurantlist.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i=0;i<restarray.length();i++){
                        JSONObject c = restarray.getJSONObject(i);
                        String name = c.getString("name");
                        String building = c.getString("building");
                        String state = c.getString("state");
                        String rating = c.getString("rating");
                        longtitude1 = Double.parseDouble(c.getString("longtitude"));
                        latitude1 = Double.parseDouble(c.getString("latitude"));
//                        String distance=distance(latitude1,longtitude1,latitude,longtitude)+"KM";
                        String restaurant="restaurant";
                        String starttime = c.getString("starttime");
                        String endtime = c.getString("endtime");
                        String telnumber = c.getString("telnumber");
                        String fbpage = c.getString("fbpage");

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("name",name);
                        resultlisthash.put("building",building);
                        resultlisthash.put("state",state);
                        resultlisthash.put("rating",rating);
//                        resultlisthash.put("distance",distance);
                        resultlisthash.put("category",restaurant);
                        resultlisthash.put("restlong",""+longtitude1);
                        resultlisthash.put("restlat",""+latitude1);
                        resultlisthash.put("starttime",starttime);
                        resultlisthash.put("endtime",endtime);
                        resultlisthash.put("telnumber",telnumber);
                        resultlisthash.put("fbpage",fbpage);
                        restaurantlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                ListAdapter adapter = new CustomResult(
                        MainActivity.this, restaurantlist,
                        R.layout.restaurant_list, new String[]
                        {"name","state","distance","rating"}, new int[]
                        {R.id.textView28,R.id.textView30,R.id.textView32,R.id.textView35});
                listTop10.setAdapter(adapter);
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    private void getGPS() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude=location.getLatitude();
                    longtitude=location.getLongitude();
                    currentLocation(longtitude,latitude,SharedPreferenceConfig.getEmail(MainActivity.this));
                } else {
                    Toast.makeText(MainActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
            return;
        } else {
            getLocation();
        }
    }

    private void currentLocation(final Double longtitude, final Double latitude, final String email) {
        class CurrentLocation extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("longtitude",String.valueOf(longtitude));
                hashMap.put("latitude",String.valueOf(latitude));
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
                    Toast.makeText(MainActivity.this, "Update location failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        CurrentLocation currentLocation1 = new CurrentLocation();
        currentLocation1.execute();
    }

    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1,locationListener);

        }catch (Exception ex){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                    return;
                }else {
                    Toast.makeText(this, "You must open gps", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();
                }
                break;
        }
    }


    private void setRestaurant(String preferLocation) {
        trendingNumber("trending",preferLocation);
        buffetNumber("buffet",preferLocation);
        westernNumber("western",preferLocation);
        bbqNumber("bbq",preferLocation);
        halalNumber("halal",preferLocation);
        chineseNumber("chinese",preferLocation);
        indiaNumber("india",preferLocation);
        japaneseNumber("japanese",preferLocation);
    }

    private void japaneseNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvjapanese.setText(getRestaurantNumber(s));
                japaneseNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void trendingNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvtrending.setText(getRestaurantNumber(s));
                trendingNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void buffetNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvbuffet.setText(getRestaurantNumber(s));
                buffetNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void westernNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvwestern.setText(getRestaurantNumber(s));
                westernNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void bbqNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvbbq.setText(getRestaurantNumber(s));
                bbqNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void halalNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvhalal.setText(getRestaurantNumber(s));
                halalNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void chineseNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvchinese.setText(getRestaurantNumber(s));
                chineseNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private void indiaNumber(final String category, final String preferLocation) {
        class RestaurantNumber extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",preferLocation);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/restaurant_number.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tvindia.setText(getRestaurantNumber(s));
                indiaNo=s;
            }
        }
        RestaurantNumber restaurantNumber = new RestaurantNumber();
        restaurantNumber.execute();
    }

    private String getRestaurantNumber(String s) {
        int n=Integer.parseInt(s);
        String restaurant="";
        if (n>1){
            restaurant="Restaurants";
        }else {
            restaurant="Restaurant";
        }
        return n+" "+restaurant;
    }

    private void btnOnClick() {
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(trendingNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","trending");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        buffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(buffetNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","buffet");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(westernNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","western");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        bbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(bbqNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","bbq");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        halal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(halalNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","halal");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(chineseNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","chinese");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        india.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(indiaNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","india");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=Integer.parseInt(japaneseNo);
                if (n==0){
                    Toast.makeText(MainActivity.this, "No restaurant", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MainActivity.this,RestaurantCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    bundle.putString("category","japanese");
                    bundle.putString("locationPrefer",locationPrefer);
                    bundle.putString("state",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void initial() {
        trending=findViewById(R.id.imageView3);
        buffet=findViewById(R.id.imageView5);
        western=findViewById(R.id.imageView8);
        bbq=findViewById(R.id.imageView6);
        halal=findViewById(R.id.imageView7);
        chinese=findViewById(R.id.imageView9);
        india=findViewById(R.id.imageView63);
        tvtrending=findViewById(R.id.textView2);
        tvbuffet=findViewById(R.id.textView9);
        tvwestern=findViewById(R.id.textView12);
        tvbbq=findViewById(R.id.textView22);
        tvhalal=findViewById(R.id.textView100);
        tvchinese=findViewById(R.id.textView102);
        tvindia=findViewById(R.id.textView104);
        tvjapanese=findViewById(R.id.textView20);
        japanese=findViewById(R.id.imageView10);
        spinner=findViewById(R.id.spinner2);
        listTop10=findViewById(R.id.listTop10);
        restNo=findViewById(R.id.textView107);
        listAll=findViewById(R.id.listAll);
        allNo=findViewById(R.id.textView106);
    }

    private void checkPreferLoc(final String email) {
        class CheckPreferLoc extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/checkpreferloc.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("")){
                    popoutSetPreferLocation();
                }else {
                    locationPrefer=s;
                    setSpinner(s);
                    setRestaurant(s);
                }
            }
        }
        CheckPreferLoc checkPreferLoc = new CheckPreferLoc();
        checkPreferLoc.execute();
    }

    private void popoutSetPreferLocation() {
        LayoutInflater li=LayoutInflater.from(this);
        final View promptView=li.inflate(R.layout.choose_state,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Choose one prefer location");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            final RadioGroup group = promptView.findViewById(R.id.radioGroup);
                            int selectedId = group.getCheckedRadioButtonId();
                            final RadioButton radioButton = promptView.findViewById(selectedId);
                            String preferLocation=radioButton.getText().toString();
                            savePreferLocation(preferLocation);
                        }catch (Exception e){
                            popoutSetPreferLocation();
                            Toast.makeText(MainActivity.this, "Please choose one", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    private void savePreferLocation(final String preferLocation) {
        class SavePreferLoc extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",SharedPreferenceConfig.getEmail(MainActivity.this));
                hashMap.put("location",preferLocation);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/savepreferloc.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("Something Wrong")){
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }else {
                    setSpinner(s);
                    setRestaurant(s);
                }
            }
        }
        SavePreferLoc savePreferLoc = new SavePreferLoc();
        savePreferLoc.execute();
    }

    private void setSpinner(String s) {
        int location=0;
        switch (s){
            case "Johor":
                location=0;
                break;
            case "Labuan":
                location=1;
                break;
            case "Kedah":
                location=2;
                break;
            case "Kelantan":
                location=3;
                break;
            case "Kuala Lumpur":
                location=4;
                break;
            case "Melaka":
                location=5;
                break;
            case "Pahang":
                location=6;
                break;
            case "Perak":
                location=7;
                break;
            case "Perlis":
                location=8;
                break;
            case "Pinang":
                location=9;
                break;
            case "Sabah":
                location=10;
                break;
            case "Sarawak":
                location=11;
                break;
            case "Selangor":
                location=12;
                break;
            case "Terrenganu":
                location=13;
                break;
        }
        spinner.setSelection(location);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent;
            Bundle bundle;
            switch (menuItem.getItemId()) {
                case R.id.aroundme:
                    intent=new Intent(MainActivity.this,AroundMe.class);
                    bundle = new Bundle();
                    bundle.putString("locationPrefer",locationPrefer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.profile:
                    startActivity(new Intent(MainActivity.this, profile.class));
                    break;
            }
            return true;
        }
    };

    private String checkLoc(final String email) {
        class CheckLoc extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/checkpreferloc.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("")){
                    popoutSetPreferLocation();
                }else {
                    location=s;
                }
            }
        }
        CheckLoc checkLoca = new CheckLoc();
        checkLoca.execute();
        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.newrestaurant):
                startActivity(new Intent(this,NewRestaurant.class));
                return true;

            case R.id.logout:
                SharedPreferenceConfig.deleteEmail(this);
                startActivity(new Intent(this,Login.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
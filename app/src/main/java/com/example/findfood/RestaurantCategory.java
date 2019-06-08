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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantCategory extends AppCompatActivity {
    private BottomNavigationView navigation;
    private ListView listRestaurant;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String location,category,state,locationPrefer;
    private ArrayList<HashMap<String, String>> restaurantlist;
    private Double latitude1,longtitude1,longtitude,latitude,longtitude2, latitude2;
    private ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_category);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        location=bundle.getString("location");
        locationPrefer=bundle.getString("locationPrefer");
        category = bundle.getString("category");
        state=bundle.getString("state");
        getCurrentLocation(SharedPreferenceConfig.getEmail(this));
        navigation = findViewById(R.id.bottomNavigationView4);
        navigation.setOnNavigationItemSelectedListener(navListener);
        loadResultList(category,state);
        listRestaurant=findViewById(R.id.listCategory);
        listRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantCategory.this,RestaurantDetail.class);
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
                bundle.putString("restlat",String.valueOf(latitude1));
                bundle.putString("restlong",String.valueOf(longtitude1));
                bundle.putString("locationPrefer",locationPrefer);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getCurrentLocation(final String email) {
        class GetCurrentLocation extends AsyncTask<Void,Void,String> {

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
                        latitude2 = Double.parseDouble(c.getString("latitude"));
                        longtitude2 = Double.parseDouble(c.getString("longtitude"));
                    }
                }catch (final JSONException e){
                    Toast.makeText(RestaurantCategory.this, "Error Occur", Toast.LENGTH_SHORT).show();
                }
            }
        }
        GetCurrentLocation getCurrentLocation = new GetCurrentLocation();
        getCurrentLocation.execute();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(RestaurantCategory.this, MainActivity.class));
                    break;

                case R.id.aroundme:
                    Intent intent=new Intent(RestaurantCategory.this,AroundMe.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("locationPrefer",locationPrefer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.profile:
                    startActivity(new Intent(RestaurantCategory.this, profile.class));
                    break;
            }
            return true;
        }
    };



    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(RestaurantCategory.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(RestaurantCategory.this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                    loadResultList(category,state);
                }else{
                    Toast.makeText(RestaurantCategory.this, "GPS open failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        CurrentLocation currentLocation1 = new CurrentLocation();
        currentLocation1.execute();
    }

    private void loadResultList(final String category, final String state) {
        class LoadResultList extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                restaurantlist=new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/search_category.php",hashMap);
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
                        String starttime = c.getString("starttime");
                        String endtime = c.getString("endtime");
                        String rating = c.getString("rating");
                        String fbpage = c.getString("fbpage");
                        String telnumber=c.getString("telnumber");
                        longtitude1 = Double.parseDouble(c.getString("longtitude"));
                        latitude1 = Double.parseDouble(c.getString("latitude"));
                        String distance=distance(latitude1,longtitude1,latitude2,longtitude2)+"KM";
                        String restaurant="restaurant";

                        HashMap<String,String> resultlisthash = new HashMap<>();
                        resultlisthash.put("name",name);
                        resultlisthash.put("building",building);
                        resultlisthash.put("state",state);
                        resultlisthash.put("starttime",starttime);
                        resultlisthash.put("endtime",endtime);
                        resultlisthash.put("rating",rating);
                        resultlisthash.put("distance",distance);
                        resultlisthash.put("category",restaurant);
                        resultlisthash.put("telnumber",telnumber);
                        resultlisthash.put("fbpage",fbpage);
                        restaurantlist.add(resultlisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }
                ListAdapter adapter = new CustomResult(
                        RestaurantCategory.this, restaurantlist,
                        R.layout.restaurant_list, new String[]
                        {"name","state","distance","rating"}, new int[]
                        {R.id.textView28,R.id.textView30,R.id.textView32,R.id.textView35});
                listRestaurant.setAdapter(adapter);
            }
        }
        LoadResultList loadResultList = new LoadResultList();
        loadResultList.execute();
    }

    private String distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        int distance=(int)Math.round(dist);
        return ""+distance;
    }

    public void refresh(View view) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location1) {
                try {
                    if (location1 != null) {
                        latitude = location1.getLatitude();
                        longtitude = location1.getLongitude();
                        currentLocation(longtitude, latitude, SharedPreferenceConfig.getEmail(RestaurantCategory.this));
                        Intent intent = new Intent(RestaurantCategory.this,RestaurantCategory.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("location",location);
                        bundle.putString("locationPrefer",locationPrefer);
                        bundle.putString("category",category);
                        bundle.putString("state",state);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RestaurantCategory.this, "Error Occur", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(RestaurantCategory.this, "Error Occur...", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            RestaurantCategory.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
            return;
        } else {
            getLocation();
        }
    }
}

package com.example.findfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AroundMe extends AppCompatActivity  implements OnMapReadyCallback {
    private BottomNavigationView navigation;
    private MapView mapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ArrayList<HashMap<String, String>> list=null;
    private ArrayList<HashMap<String, String>> totalnumber=null;
    private String locationPrefer="";
    private Double longtitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        locationPrefer= bundle.getString("locationPrefer");
        navigation = findViewById(R.id.bottomNavigationView3);
        navigation.setOnNavigationItemSelectedListener(navListener);
        getUserLocation(SharedPreferenceConfig.getEmail(this));
        list=new ArrayList<>();
        totalnumber=new ArrayList<>();
        totalnumber=callNum(locationPrefer);
        list=setMap(locationPrefer);

        mapView=findViewById(R.id.maprest);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private String checkPreferLoc(final String email) {
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
                locationPrefer=s;
            }
        }
        CheckPreferLoc checkPreferLoc = new CheckPreferLoc();
        checkPreferLoc.execute();
        return locationPrefer;
    }

    private void getUserLocation(final String email) {
        class UserLocation extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/get_location.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray foodarray = jsonObject.getJSONArray("profile");
                    for (int i = 0; i < foodarray.length(); i++) {
                        JSONObject c = foodarray.getJSONObject(i);
                        longtitude= Double.parseDouble(c.getString("longtitude"));
                        latitude= Double.parseDouble(c.getString("latitude"));
                    }
                }catch(JSONException ex){
                    Toast.makeText(AroundMe.this, "Error Occur", Toast.LENGTH_SHORT).show();
                }
            }
        }
        UserLocation userLocation = new UserLocation();
        userLocation.execute();
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
    public void onMapReady(final GoogleMap map) {

        try{
            String location = locationPrefer;
            Double latit=0.0,longt=0.0;
            switch (location) {
                case "Johor":
                    latit=1.494000;
                    longt=103.739852;
                    break;
                case "Labuan":
                    latit=5.314028;
                    longt=115.223828;
                    break;
                case "Kedah":
                    latit=6.121847;
                    longt=100.366882;
                    break;
                case "Kelantan":
                    latit=5.606669;
                    longt=102.171161;
                    break;
                case "Kuala Lumpur":
                    latit=3.159056;
                    longt=101.702790;
                    break;
                case "Melaka":
                    latit=2.209930;
                    longt=102.242575;
                    break;
                case "Pahang":
                    latit=3.826657;
                    longt=102.513862;
                    break;
                case "Perak":
                    latit=4.772064;
                    longt=100.957794;
                    break;
                case "Perlis":
                    latit=6.480521;
                    longt=100.239582;
                    break;
                case "Pinang":
                    latit=5.371680;
                    longt=100.294309;
                    break;
                case "Sabah":
                    latit=5.697319;
                    longt=117.547284;
                    break;
                case "Sarawak":
                    latit=3.165263;
                    longt=113.017590;
                    break;
                case "Selangor":
                    latit=3.355401;
                    longt=101.265549;
                    break;
                case "Terrenganu":
                    latit=4.777469;
                    longt=103.381707;
                    break;
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latit,longt),10));

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    final String name[]=new String[list.size()];
                    Double latitude[]=new Double[list.size()];
                    Double longtitude[]=new Double[list.size()];
                    for (int i=0;i<list.size();i++){
                        name[i]=list.get(i).get("name");
                        longtitude[i]=Double.parseDouble(list.get(i).get("longtitude"));
                        latitude[i]=Double.parseDouble(list.get(i).get("latitude"));

                        LatLng restaurantposition = new LatLng(latitude[i], longtitude[i]);
                        map.addMarker(new MarkerOptions().position(restaurantposition).title(list.get(i).get("name")));
                    }
                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            int i=Integer.parseInt(marker.getId().substring(1));
                            Intent intent = new Intent(AroundMe.this,RestaurantDetail.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name",list.get(i).get("name"));
                            bundle.putString("building",list.get(i).get("building"));
                            bundle.putString("state",list.get(i).get("state"));
                            bundle.putString("starttime",list.get(i).get("starttime"));
                            bundle.putString("endtime",list.get(i).get("endtime"));
                            bundle.putString("rating",list.get(i).get("rating"));
                            bundle.putString("category",list.get(i).get("category"));
                            bundle.putString("distance",list.get(i).get("distance"));
                            bundle.putString("telnumber",list.get(i).get("telnumber"));
                            bundle.putString("fbpage",list.get(i).get("fbpage"));
                            bundle.putString("restlat",list.get(i).get("latitude"));
                            bundle.putString("restlong",list.get(i).get("longtitude"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            });
        }catch(Exception exeption){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }



    private ArrayList<HashMap<String, String>> setMap(final String s) {
        class CheckAllLoc extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("location",s);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/check_selected_location.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray foodarray = jsonObject.getJSONArray("gps");
                    for (int i = 0; i < foodarray.length(); i++) {
                        JSONObject c = foodarray.getJSONObject(i);
                        String name1 = c.getString("name");
                        String longtitude1 = c.getString("longtitude");
                        String latitude1 = c.getString("latitude");
                        String building = c.getString("building");
                        String state = c.getString("state");
                        String starttime = c.getString("starttime");
                        String endtime = c.getString("endtime");
                        String rating = c.getString("rating");
                        String telnumber = c.getString("telnumber");
                        String fbpage = c.getString("fbpage");
                        String distance=distance(Double.parseDouble(latitude1),Double.parseDouble(longtitude1),latitude,longtitude)+"KM";

                        HashMap<String,String> restlisthash = new HashMap<>();
                        restlisthash.put("name",name1);
                        restlisthash.put("longtitude",longtitude1);
                        restlisthash.put("latitude",latitude1);
                        restlisthash.put("building",building);
                        restlisthash.put("state",state);
                        restlisthash.put("starttime",starttime);
                        restlisthash.put("endtime",endtime);
                        restlisthash.put("rating",rating);
                        restlisthash.put("telnumber",telnumber);
                        restlisthash.put("fbpage",fbpage);
                        restlisthash.put("distance",distance);
                        list.add(restlisthash);
                    }
                }catch(JSONException ex){
                    Toast.makeText(AroundMe.this, "No Restaurant", Toast.LENGTH_SHORT).show();
                }
            }
        }
        CheckAllLoc checkAllLoc = new CheckAllLoc();
        checkAllLoc.execute();
        return list;
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


    private ArrayList<HashMap<String, String>> callNum(final String state) {

        class CheckNumberLoc extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("state", state);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/check_num_location.php", hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                totalnumber.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("num");
                    for (int i = 0; i < restarray.length(); i++) {
                        JSONObject c = restarray.getJSONObject(i);
                        String number1 = c.getString("number");

                        HashMap<String,String> restnolisthash = new HashMap<>();
                        restnolisthash.put("number",number1);
                        totalnumber.add(restnolisthash);
                    }
                }catch(JSONException ex){
                    Toast.makeText(AroundMe.this, "No Restaurant", Toast.LENGTH_SHORT).show();
                }
            }
        }
        CheckNumberLoc checkNumberLoc = new CheckNumberLoc();
        checkNumberLoc.execute();
        return totalnumber;
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Intent intent;
            Bundle bundle;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    intent=new Intent(AroundMe.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    intent=new Intent(AroundMe.this,profile.class);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };
}

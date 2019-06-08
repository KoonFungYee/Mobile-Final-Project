package com.example.findfood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class EditRestaurant extends AppCompatActivity {
    private ImageView restImage;
    private EditText restName, buildingName, street, start, end, telNo, fbPage;
    private Spinner reststate, category;
    private TextView longtitude, latitude;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String name, rating, building, distance, street1, state, starttime, endtime, telNumber, fbpage, category1, longti, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        state = bundle.getString("state");
        rating = bundle.getString("rating");
        distance= bundle.getString("distance");
        initialView();
        retrieve();
        restName.setText(name);
        restName.setEnabled(false);
        setSpinner(state);
        reststate.setEnabled(false);
        String url="http://www.socstudents.net/findfood/restaurant/"+name+"_"+state+".jpg";
        Picasso.get().load(url).fit().into(restImage);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude.setText("" + location.getLatitude());
                    longtitude.setText("" + location.getLongitude());
                } else {
                    Toast.makeText(EditRestaurant.this, "Location Error Occur", Toast.LENGTH_SHORT).show();
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
            EditRestaurant.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
            return;
        } else {
            getLocation();
        }
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
        reststate.setSelection(location);
    }

    private void retrieve() {
        class RetrieveRestaurant extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("state", state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/retrieve_restaurant.php", hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    String building2="",starttime2="",endtime2="",telNumber2="",fbpage2="",category2="";
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < restarray.length(); i++) {
                        JSONObject c = restarray.getJSONObject(i);
                        building2 = c.getString("building");
                        starttime2 = c.getString("starttime");
                        endtime2 = c.getString("endtime");
                        telNumber2 = c.getString("telNumber");
                        fbpage2 = c.getString("fbpage");
                        category2 = c.getString("category");
                    }
                    buildingName.setText(building2);
                    start.setText(starttime2);
                    end.setText(endtime2);
                    telNo.setText(telNumber2);
                    fbPage.setText(fbpage2);
                    category.setSelection(category(category2));

                }catch (Exception ex){
                    Toast.makeText(EditRestaurant.this, "Error Occur", Toast.LENGTH_SHORT).show();
                }
            }

            private int category(String category2) {
                int position=0;
                switch (category2){
                    case "bbq":
                        position=0;
                        break;
                    case "buffet":
                        position=1;
                        break;
                    case "chinese":
                        position=2;
                        break;
                    case "halal":
                        position=3;
                        break;
                    case "india":
                        position=4;
                        break;
                    case "trending":
                        position=5;
                        break;
                    case "western":
                        position=6;
                        break;
                }
                return position;
            }
        }
        RetrieveRestaurant retrieveRestaurant = new RetrieveRestaurant();
        retrieveRestaurant.execute();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(EditRestaurant.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(EditRestaurant.this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    return;
                } else {
                    Toast.makeText(this, "You must open gps", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditRestaurant.this, NewRestaurant.class));
                    finish();
                }
                break;
        }
    }

    private void initialView() {
        restImage=findViewById(R.id.imageView28);
        restName = findViewById(R.id.editText4);
        buildingName = findViewById(R.id.editText9);
        start = findViewById(R.id.editText13);
        end = findViewById(R.id.editText14);
        telNo = findViewById(R.id.editText15);
        fbPage = findViewById(R.id.editText16);
        reststate = findViewById(R.id.spinner8);
        category = findViewById(R.id.spinner9);
        longtitude = findViewById(R.id.textView56);
        latitude = findViewById(R.id.textView58);
    }

    public void takepictureedit(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Upload picture");
        alertDialogBuilder
                .setMessage("Let's take a picture")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 1);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, 400, 500);
            restImage.setImageBitmap(imageBitmap);
            restImage.buildDrawingCache();
            ContextWrapper cw = new ContextWrapper(this);
            File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdir();
            }
            Log.e("FILE NAME", "" + pictureFileDir.toString());
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                return;
            }
            FileOutputStream outStream = null;
            String photoFile = "profile.jpg";
            File outFile = new File(pictureFileDir, photoFile);
            try {
                outStream = new FileOutputStream(outFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getDir() {
        ContextWrapper cw = new ContextWrapper(this);
        File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdir();
        }
        Log.d("GETDIR", pictureFileDir.getAbsolutePath());
        return pictureFileDir.getAbsolutePath() + "/profile.jpg";
    }

    public void save(View view) {
        name = restName.getText().toString();
        building = buildingName.getText().toString();
        state = reststate.getSelectedItem().toString();
        starttime = start.getText().toString();
        endtime = end.getText().toString();
        telNumber = telNo.getText().toString();
        fbpage = fbPage.getText().toString();
        category1 = category.getSelectedItem().toString().toLowerCase();
        longti = longtitude.getText().toString();
        lat = latitude.getText().toString();
        if (name.equals("") || building.equals("") || state.equals("") || starttime.equals("")
                || endtime.equals("") || telNumber.equals("")) {
            Toast.makeText(this, "Please fill in all column", Toast.LENGTH_SHORT).show();
        } else {
            try {
                new Encode_image().execute(getDir(), name+"_"+state+".jpg");
            } catch (Exception ex) {
                Toast.makeText(EditRestaurant.this, "Please upload picture", Toast.LENGTH_SHORT).show();
            }
            updateRestaurant(name, building, state, starttime, endtime, telNumber, fbpage, category1, longti, lat);
        }
    }

    private void updateRestaurant(final String name, final String building, final String state,
                                 final String starttime, final String endtime, final String telNumber, final String fbpage,
                                 final String category, final String longti, final String lat) {
        class UpdateRestaurant extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("state", state);
                hashMap.put("building", building);
                hashMap.put("starttime", starttime);
                hashMap.put("endtime", endtime);
                hashMap.put("telnumber", telNumber);
                hashMap.put("fbpage", fbpage);
                hashMap.put("category", category);
                hashMap.put("longtitude", longti);
                hashMap.put("latitude", lat);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/edit_restaurant.php", hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(EditRestaurant.this,RestaurantDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name",name);
                    bundle.putString("state",state);
                    bundle.putString("starttime",starttime);
                    bundle.putString("endtime",endtime);
                    bundle.putString("rating",rating);
                    bundle.putString("building",building);
                    bundle.putString("distance",distance);
                    bundle.putString("category",category);
                    bundle.putString("telnumber",telNumber);
                    bundle.putString("fbpage",fbpage);
                    bundle.putString("restlat",lat);
                    bundle.putString("restlong",longti);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditRestaurant.this, "Creat Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        UpdateRestaurant updateRestaurant = new UpdateRestaurant();
        updateRestaurant.execute();
    }

    public class Encode_image extends AsyncTask<String, String, Void> {
        private String encoded_string, image_name;
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... args) {
            String filname = args[0];
            image_name = args[1];
            bitmap = BitmapFactory.decodeFile(filname);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            makeRequest(encoded_string, image_name);
        }

        private void makeRequest(final String encoded_string, final String image_name) {
            class UploadAll extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("encoded_string", encoded_string);
                    map.put("image_name", image_name);
                    RequestHandler rh = new RequestHandler();//request server connection
                    String s = rh.sendPostRequest("http://www.socstudents.net/findfood/php/new_restaurant_image.php", map);
                    return s;
                }
            }
            UploadAll uploadall = new UploadAll();
            uploadall.execute();
        }
    }
}

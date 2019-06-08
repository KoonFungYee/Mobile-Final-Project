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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NewRestaurant extends AppCompatActivity {
    private ImageView restImage;
    private EditText restName, buildingName, start, end, telNo, fbPage;
    private Spinner reststate, category;
    private TextView longtitude, latitude;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String name, building, state, starttime, endtime, telNumber, fbpage, category1, longti, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);
        initialView();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude.setText("" + location.getLatitude());
                    longtitude.setText("" + location.getLongitude());
                } else {
                    Toast.makeText(NewRestaurant.this, "Error Occur", Toast.LENGTH_SHORT).show();
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
            NewRestaurant.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
            return;
        } else {
            getLocation();
        }
    }

    private void initialView() {
        restImage = findViewById(R.id.imageView11);
        restName = findViewById(R.id.editText);
        buildingName = findViewById(R.id.editText2);
        start = findViewById(R.id.editText6);
        end = findViewById(R.id.editText7);
        telNo = findViewById(R.id.editText8);
        fbPage = findViewById(R.id.editText10);
        reststate = findViewById(R.id.spinner3);
        category = findViewById(R.id.spinner4);
        longtitude = findViewById(R.id.TVlongtitude);
        latitude = findViewById(R.id.TVlatitude);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(NewRestaurant.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(NewRestaurant.this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
                    startActivity(new Intent(NewRestaurant.this, NewRestaurant.class));
                    finish();
                }
                break;
        }
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
            checkDuplicate(name,state);
        }
    }

    private void checkDuplicate(final String name,final String state) {
        class CheckDuplicate extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("state", state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_duplicate_restaurant.php", hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("yes")) {
                    Toast.makeText(NewRestaurant.this, "Restaurant exist", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        new Encode_image().execute(getDir(), name+"_"+state+ ".jpg");
                    } catch (Exception ex) {
                        Toast.makeText(NewRestaurant.this, "Please upload picture", Toast.LENGTH_SHORT).show();
                    }
                    creatRestaurant(name, building, state, starttime, endtime, telNumber, fbpage, category1, longti, lat);
                }
            }
        }

        CheckDuplicate duplicate = new CheckDuplicate();
        duplicate.execute();
    }

    private void creatRestaurant(final String name, final String building, final String state,
                                 final String starttime, final String endtime, final String telNumber, final String fbpage,
                                 final String category, final String longti, final String lat) {
        class CreatRestaurant extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("building", building);
                hashMap.put("state", state);
                hashMap.put("starttime", starttime);
                hashMap.put("endtime", endtime);
                hashMap.put("telnumber", telNumber);
                hashMap.put("fbpage", fbpage);
                hashMap.put("category", category);
                hashMap.put("longtitude", longti);
                hashMap.put("latitude", lat);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/new_restaurant.php", hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    startActivity(new Intent(NewRestaurant.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(NewRestaurant.this, "Creat Failed", Toast.LENGTH_LONG).show();
                }
            }
        }

        CreatRestaurant creatRestaurant1 = new CreatRestaurant();
        creatRestaurant1.execute();

    }

    public void takepicture(View view) {
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

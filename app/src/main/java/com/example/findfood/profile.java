package com.example.findfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class profile extends AppCompatActivity {
    private BottomNavigationView navigation;
    private TextView tvname,tvemail,tvphone,tvgender;
    private String username, name, phone, gender, location1,locationPrefer="";
    private ImageView image;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navListener);
        username=SharedPreferenceConfig.getEmail(this);
        initialView();
        loadprofile(username);
        locationPrefer=checkLoc(SharedPreferenceConfig.getEmail(profile.this));
    }

    private void loadprofile(final String username1) {
        class LoadProfile extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",username1);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://www.socstudents.net/findfood/php/getprofile.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("profile");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        name = c.getString("name");
                        phone = c.getString("phone");
                        gender = c.getString("gender");
                        location1=c.getString("location");
                    }
                    int location = 0;
                    switch (location1) {
                        case "Johor":
                            location = 0;
                            break;
                        case "Labuan":
                            location = 1;
                            break;
                        case "Kedah":
                            location = 2;
                            break;
                        case "Kelantan":
                            location = 3;
                            break;
                        case "Kuala Lumpur":
                            location = 4;
                            break;
                        case "Melaka":
                            location = 5;
                            break;
                        case "Pahang":
                            location = 6;
                            break;
                        case "Perak":
                            location = 7;
                            break;
                        case "Perlis":
                            location = 8;
                            break;
                        case "Pinang":
                            location = 9;
                            break;
                        case "Sabah":
                            location = 10;
                            break;
                        case "Sarawak":
                            location = 11;
                            break;
                        case "Selangor":
                            location = 12;
                            break;
                        case "Terrenganu":
                            location = 13;
                            break;
                    }
                    tvname.setText(name);
                    tvemail.setText(username);
                    tvphone.setText(phone);
                    tvgender.setText(gender);
                    spinner.setSelection(location);
                    Picasso.get()
                            .load("http://www.socstudents.net/findfood/profileimages/"+username+".jpg")
                            .fit()
                            .error(R.mipmap.user)
                            .into(image);
                }catch(JSONException e){
                    Toast.makeText(profile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        LoadProfile loadProfile = new LoadProfile();
        loadProfile.execute();
    }

    private void initialView() {
        image=findViewById(R.id.imageprofile1);
        tvname=findViewById(R.id.TVdisplayname);
        tvemail=findViewById(R.id.TVemail);
        tvphone=findViewById(R.id.TVphone);
        tvgender=findViewById(R.id.TVgender);
        spinner=findViewById(R.id.spinner5);
    }

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
                locationPrefer=s;
            }
        }
        CheckLoc checkLoca = new CheckLoc();
        checkLoca.execute();
        return locationPrefer;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(profile.this, MainActivity.class));
                    break;
                case R.id.aroundme:
                    intent=new Intent(profile.this,AroundMe.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("locationPrefer",locationPrefer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };

    public void edit(View view) {
        LayoutInflater li=LayoutInflater.from(this);
        View promptView=li.inflate(R.layout.confirmpassword,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText userInput=promptView.findViewById(R.id.ETpasswordconfirm);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmPassword(userInput.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    private void confirmPassword(String password) {
        String realPassword=SharedPreferenceConfig.getPassword(this);
        if (password.equals(realPassword)){
            Intent intent=new Intent(profile.this,updateProfile.class);
            Bundle bundle=new Bundle();
            bundle.putString("username",username);
            bundle.putString("name",name);
            bundle.putString("phone",phone);
            bundle.putString("gender",gender);
            bundle.putString("password",password);
            bundle.putString("location",location1);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
        }
    }
}

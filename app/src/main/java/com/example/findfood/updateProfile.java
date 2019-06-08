package com.example.findfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class updateProfile extends AppCompatActivity {
    private ImageView image;
    private EditText  etname, etphone, etemail, etpassword;
    private Spinner spgender,preferlocation;
    private String username1, name, email, phone, gender, password, location1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initialView();
        setup();
        image=findViewById(R.id.profileImageUpdate);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTakePicture();
            }
        });
    }

    private void setup() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        username1=bundle.getString("username");
        name=bundle.getString("name");
        email=bundle.getString("username");
        phone=bundle.getString("phone");
        gender=bundle.getString("gender");
        password=bundle.getString("password");
        location1=bundle.getString("location");
        etname.setText(name);
        etemail.setText(email);
        etemail.setEnabled(false);
        etphone.setText(phone);
        etpassword.setText(password);
        int n=0;
        switch (gender){
            case "":
                n=0;
                break;
            case "Male":
                n=1;
                break;
            case "Female":
                n=2;
        }
        spgender.setSelection(n);
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
        preferlocation.setSelection(location);
        Picasso.get()
                .load("http://www.socstudents.net/findfood/profileimages/"+email+".jpg")
                .fit()
                .error(R.mipmap.user)
                .into(image);
    }


    private void dialogTakePicture() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Change Picture");
        alertDialogBuilder
                .setMessage("Let's take a picture")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 1);
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap,400,500);
            image.setImageBitmap(imageBitmap);
            image.buildDrawingCache();
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

    public void updateProfile(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure to update?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        check();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void check() {
        String name, phone, gender, newpass,preferloc;
        name=etname.getText().toString();
        phone=etphone.getText().toString();
        gender=spgender.getSelectedItem().toString();
        newpass=etpassword.getText().toString();
        preferloc=preferlocation.getSelectedItem().toString();
        if (name.equals("")||phone.equals("")||gender.equals("")||newpass.equals("")){
            Toast.makeText(this, "Please fill in all column", Toast.LENGTH_SHORT).show();
        }else {
            try{
                new Encode_image().execute(getDir(),username1+".jpg");
            }catch (Exception ex){
                Toast.makeText(this, "Please upload picture", Toast.LENGTH_SHORT).show();
            }
            updateUser(name,phone,gender,username1,newpass,preferloc);
        }

    }

    private void updateUser(final String name, final String phone, final String gender, final String username, final String password, final String location) {
        class UpdateUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("phone",phone);
                hashMap.put("gender",gender);
                hashMap.put("email",username);
                hashMap.put("password",password);
                hashMap.put("location",location);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/updateprofile.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(updateProfile.this, "Update Success", Toast.LENGTH_SHORT).show();
                    SharedPreferenceConfig.savePassword(password,updateProfile.this);
                    Intent intent = new Intent(updateProfile.this, profile.class);
                    updateProfile.this.finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(updateProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateUser updateUser = new UpdateUser();
        updateUser.execute();
    }

    public String getDir(){
        ContextWrapper cw = new ContextWrapper(this);
        File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdir();
        }
        Log.d("GETDIR",pictureFileDir.getAbsolutePath());
        return pictureFileDir.getAbsolutePath()+"/profile.jpg";
    }

    public class Encode_image extends AsyncTask<String,String,Void> {
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
                    String s = rh.sendPostRequest("http://www.socstudents.net/findfood/php/upload_image.php", map);
                    return s;
                }
            }
            UploadAll uploadall = new UploadAll();
            uploadall.execute();
        }
    }

    private void initialView() {
        image=findViewById(R.id.profileImageUpdate);
        etname=findViewById(R.id.ETnameUpdate);
        etemail=findViewById(R.id.ETemailUpdate);
        etphone=findViewById(R.id.ETphoneUpdate);
        spgender=findViewById(R.id.SpinnerGender);
        etpassword=findViewById(R.id.ETpassword);
        preferlocation=findViewById(R.id.spinner6);
    }
}

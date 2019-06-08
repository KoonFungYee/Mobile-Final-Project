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

public class EditFood extends AppCompatActivity {
    private ImageView image;
    private EditText edfoodname,edprice,edremark;
    private Spinner time;
    private String foodname,price,restname,remark,period,state,ID,rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        foodname = bundle.getString("foodname");
        price = bundle.getString("price");
        restname = bundle.getString("restname");
        remark = bundle.getString("remarks");
        period = bundle.getString("period");
        state = bundle.getString("state");
        rating=bundle.getString("rating");
        initial();
        setting();
        retrieveID();
        String url="http://www.socstudents.net/findfood/food/"+foodname+"_"+restname+"_"+state+".jpg";
        Picasso.get().load(url).fit().into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTakePicture();
            }
        });
    }

    private void retrieveID() {
        class GetID extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname);
                hashMap.put("restname",restname);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/get_ID.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ID=s;
            }
        }
        GetID getID = new GetID();
        getID.execute();
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

    private void check() {
        String foodname1,price1,remark1,period1;
        foodname1=edfoodname.getText().toString();
        price1=edprice.getText().toString();
        remark1=edremark.getText().toString();
        period1=time.getSelectedItem().toString();
        if (foodname1.equals("")||price1.equals("")||period1.equals("")){
            Toast.makeText(this, "Please fill in all column", Toast.LENGTH_SHORT).show();
        }else {
            try{
                new EditFood.Encode_image().execute(getDir(),foodname1+"_"+restname+"_"+state+".jpg");
            }catch (Exception ex){
                Toast.makeText(this, "Please upload picture", Toast.LENGTH_SHORT).show();
            }
            updateFood(foodname1,price1,remark1,period1);
        }
    }

    private void updateFood(final String foodname1, final String price1, final String remark1, final String period1) {
        class UpdateFood extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname1);
                hashMap.put("price",price1);
                hashMap.put("remark",remark1);
                hashMap.put("period",period1);
                hashMap.put("id",ID);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/update_food.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(EditFood.this, "Update Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditFood.this, FoodDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("foodname",foodname1);
                    bundle.putString("price",price1);
                    bundle.putString("period",period1);
                    bundle.putString("remarks",remark1);
                    bundle.putString("restname",restname);
                    bundle.putString("state",state);
                    bundle.putString("rating",rating);
                    intent.putExtras(bundle);
                    EditFood.this.finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(EditFood.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        UpdateFood updateFood = new UpdateFood();
        updateFood.execute();
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
                    String s = rh.sendPostRequest("http://www.socstudents.net/findfood/php/upload_food_image.php", map);
                    return s;
                }
            }
            UploadAll uploadall = new UploadAll();
            uploadall.execute();
        }
    }

    private void setting() {
        edfoodname.setText(foodname);
        edprice.setText(price);
        edremark.setText(remark);
        int n=0;
        switch (period){
            case "All Day":
                n=0;
                break;
            case "Breakfast":
                n=1;
                break;
            case "Lunch":
                n=2;
                break;
            case "Hi-Tea":
                n=3;
                break;
            case "Dinner":
                n=4;
                break;
            case "Supper":
                n=5;
                break;
        }
        time.setSelection(n);
    }

    private void initial() {
        image=findViewById(R.id.imageView53);
        edfoodname=findViewById(R.id.editText20);
        edprice=findViewById(R.id.editText21);
        time=findViewById(R.id.spinner11);
        edremark=findViewById(R.id.editText23);
    }

    public void update(View view) {
        check();
    }
}

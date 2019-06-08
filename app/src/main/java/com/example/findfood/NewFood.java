package com.example.findfood;

import android.content.Context;
import android.content.ContextWrapper;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NewFood extends AppCompatActivity {
    private String name, state;
    ImageView foodImage;
    EditText restname,foodname,price,remarks;
    Spinner time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        state = bundle.getString("state");
        initial();
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTakePicture();
            }
        });
        restname.setText(name);
        restname.setEnabled(false);
    }

    private void dialogTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap,400,500);
            foodImage.setImageBitmap(imageBitmap);
            foodImage.buildDrawingCache();
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


    private void initial() {
        foodImage=findViewById(R.id.imageView33);
        restname=findViewById(R.id.editText5);
        foodname=findViewById(R.id.editText12);
        price=findViewById(R.id.editText17);
        time=findViewById(R.id.spinner10);
        remarks=findViewById(R.id.editText18);
    }


    public void submit(View view) {
        String foodname1,foodprice,period,remark;
        foodname1=foodname.getText().toString();
        foodprice=price.getText().toString();
        period=time.getSelectedItem().toString();
        remark=remarks.getText().toString();
        if (foodname1.equals("")||foodprice.equals("")){
            Toast.makeText(this, "Please fill in the column of Food Name and Price", Toast.LENGTH_SHORT).show();
        }else {
            try{
                new Encode_image().execute(getDir(),foodname1+"_"+name+"_"+state+".jpg");
            }catch (Exception ex){
                Toast.makeText(this, "Please upload food picture", Toast.LENGTH_SHORT).show();
            }
            newFood(foodname1,foodprice,period,remark,name,state);
        }
    }

    private void newFood(final String foodname1, final String foodprice, final String period, final String remarks, final String name,
                         final String state) {
        class CreateFood extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("foodname",foodname1);
                hashMap.put("price",foodprice);
                hashMap.put("period",period);
                hashMap.put("remarks",remarks);
                hashMap.put("restname",name);
                hashMap.put("state",state);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/new_food.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(NewFood.this, "Created Success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(NewFood.this,FoodDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("foodname",foodname1);
                    bundle.putString("price",foodprice);
                    bundle.putString("period",period);
                    bundle.putString("remarks",remarks);
                    bundle.putString("restname",name);
                    bundle.putString("state",state);
                    bundle.putString("rating","0.00");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NewFood.this, "Create Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        CreateFood createFood = new CreateFood();
        createFood.execute();
    }
}

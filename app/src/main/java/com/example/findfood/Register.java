package com.example.findfood;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText username, password, name, phone;
    private Spinner gender;
    private String username1,password1,name1,phone1,gender1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void clickhere(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }

    public void register(View view) {
        username = findViewById(R.id.ETusername);
        password = findViewById(R.id.ETpassword);
        name = findViewById(R.id.ETname);
        phone = findViewById(R.id.ETphone);
        gender = findViewById(R.id.spinner);

        username1 = username.getText().toString();
        password1 = password.getText().toString();
        name1 = name.getText().toString();
        phone1 = phone.getText().toString();
        gender1 = gender.getSelectedItem().toString();

        if (username1.equals("") || password1.equals("") || name1.equals("") || phone1.equals("") || gender1.equals("")) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        } else {
            checkDuplicate(username1);
        }
    }

    private void checkDuplicate(final String name) {
        class CheckDuplicate extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",name);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/check_duplicate_user.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("yes")) {
                    Toast.makeText(Register.this, "User exist", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(username1,password1,name1,phone1,gender1);
                }
            }
        }

        CheckDuplicate duplicate = new CheckDuplicate();
        duplicate.execute();
    }

    private void registerUser(final String username, final String password, final String name, final String phone, final String gender) {
        class RegisterUser extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",username);
                hashMap.put("password",password);
                hashMap.put("name",name);
                hashMap.put("phone",phone);
                hashMap.put("gender",gender);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/register.php",hashMap);
                return s;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this,
                        "Registration","...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(Register.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    Register.this.finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        RegisterUser registerUser = new RegisterUser();
        registerUser.execute();
    }
}

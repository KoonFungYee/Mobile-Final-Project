package com.example.findfood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class Login extends AppCompatActivity {
    SharedPreferenceConfig utils;
    private EditText username,password;
    private Dialog dialogforgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utils=new SharedPreferenceConfig();
        if (utils.getEmail(this) != null ){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void clickhere(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }

    public void login(View view) {
        username=findViewById(R.id.ETusername);
        password=findViewById(R.id.ETpassword);
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        loginUser(username1,password1);
    }

    private void loginUser(final String username1, final String password1) {
        class LoginUser extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this,
                        "Login user","...",false,false);
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",username1);
                hashMap.put("password",password1);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://www.socstudents.net/findfood/php/login.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")){
                    verifyFromSQLite(username1,password1);
                }else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute();
    }

    private void verifyFromSQLite(String username1, String password1) {
        Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Login.this,MainActivity.class));
        SharedPreferenceConfig.saveEmail(username1, this);
        SharedPreferenceConfig.savePassword(password1, this);
        finish();
    }

    public void forgorPassword(View view) {
        dialogforgotpass = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialogforgotpass.setContentView(R.layout.forgot_password);
        final EditText edemail = dialogforgotpass.findViewById(R.id.editText3);
        Button btnsendemail = dialogforgotpass.findViewById(R.id.button8);
        btnsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotemail =  edemail.getText().toString();
                sendPassword(forgotemail);
            }
        });
        dialogforgotpass.show();
    }

    private void sendPassword(final String forgotemail) {
        class SendPassword extends AsyncTask<Void,String,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap();
                hashMap.put("email",forgotemail);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://www.socstudents.net/findfood/php/verify_email.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(Login.this, "Success. Check your email", Toast.LENGTH_LONG).show();
                    dialogforgotpass.dismiss();
                }else{
                    Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPassword sendPassword = new SendPassword();
        sendPassword.execute();
    }
}

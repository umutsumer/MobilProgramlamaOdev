package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText password2;
    EditText sendto;
    EditText phone;
    Button register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        password2 = findViewById(R.id.editPassword2);
        sendto = findViewById(R.id.editMail);
        phone = findViewById(R.id.editPhoneNo);
        register = (Button) findViewById(R.id.createaccButton);
        DB = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailbody = "Welcome!\nThese are your User Informations:\n" + "Username: " + username.getText().toString() + "\nPassword: " + password.getText().toString() + "\nPhone Number: " + phone.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = password2.getText().toString();
                String mail = sendto.getText().toString();
                String phoneNr = phone.getText().toString();
                if(user.equals("")||pass.equals("")||mail.equals("")||phoneNr.equals("")){
                    Toast.makeText(RegisterActivity.this,"Please fill all fields.",Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals(repass)){
                    Boolean checkuser = DB.checkusername(user);
                    if (checkuser==false){
                    Boolean insert = DB.insertData(user,pass,mail,phoneNr);
                    if(insert==true){
                        Toast.makeText(RegisterActivity.this,"Registered Succesfully.",Toast.LENGTH_SHORT).show();
                        sendEmail(emailbody);
                        finish();
                        }else{
                        Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                    }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Username Already Exists.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Passwords must match.",Toast.LENGTH_SHORT).show();
                }


        }

    });
}

private void sendEmail(String body){

        String mEmail = sendto.getText().toString();
        String mSubject = "Your User Informations.";
        String mMessage = body;

        JavaMailAPI  javaMailAPI = new JavaMailAPI(this,mEmail,mSubject,mMessage);
        javaMailAPI.execute();
    }
}
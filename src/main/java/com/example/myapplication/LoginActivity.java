package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;

public class LoginActivity extends AppCompatActivity {


    EditText username;
    EditText password;
    Button login;
    Button register;
    TextView attempts;
    DBHelper DB;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editUsernameLog);
        password = findViewById(R.id.editPasswordLog);
        attempts =(TextView)findViewById(R.id.remainingAttempts);
        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.registerButton);
        DB = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoregister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(gotoregister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")|| pass.equals("")){
                    Toast.makeText(LoginActivity.this,"Please Fill all the Fields.",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkuserpass = DB.checkusernamepassword(user,pass);
                    if(checkuserpass==true){
                        Toast.makeText(LoginActivity.this,"Sign in Succesful.",Toast.LENGTH_SHORT).show();
                        Intent gotoApp = new Intent(LoginActivity.this,ListActivity.class);
                        startActivity(gotoApp);
                    }else{
                        counter--;
                        attempts.setText("Remaining Attempts: "+counter);
                        if(counter==0){
                            Toast.makeText(LoginActivity.this,"Redirecting to Register Page.",Toast.LENGTH_SHORT).show();
                            Intent gotoReg = new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(gotoReg);
                            counter = 3;
                            attempts.setText("Remaining Attempts: "+counter);
                        }
                    }
                }
            }
        });
    }
}
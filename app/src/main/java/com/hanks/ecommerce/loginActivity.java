package com.hanks.ecommerce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.ecommerce.Model.Users;
import com.hanks.ecommerce.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    private TextView AdminLink , NotAdminLink ;
    private EditText InputPassword ,InputPhoneNumber ;
    private Button LoginButton ;
    private ProgressDialog loadingBar ;
    private String parentDbName = "Users" ;
    private CheckBox chkBoxRememberMe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = (Button)findViewById(R.id.login_btn);
        InputPassword= (EditText)findViewById(R.id.login_phone_password_input);
        InputPhoneNumber = (EditText)findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView)findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = (CheckBox)findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";

            }
        });
    }


    private void LoginUser() {

        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
         if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this , "Enter your number....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this , "Enter your password....", Toast.LENGTH_SHORT).show();
        }
        else
         {  loadingBar.setTitle("Login Account");
             loadingBar.setMessage("Please Wait ! Your credentials are being checked");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             AllowAccessToAccount(phone , password );

         }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey , phone );
            Paper.book().write(Prevalent.UserPasswordKey , password );
        }
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
               if(dataSnapshot.child(parentDbName).child(phone).exists())
               {
                   Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                   if(usersData.getPhone().equals(phone))
                   {
                       if(usersData.getPassword().equals(password))
                       {
                          if(parentDbName.equals("Admins"))
                          {
                              Toast.makeText(loginActivity.this, "Welcome Admin , You Logged in successfully ....", Toast.LENGTH_SHORT).show();
                              loadingBar.dismiss();
                              Intent intent = new Intent(loginActivity.this , AdminCategoryActivity.class);
                              startActivity(intent);
                          }
                          else if (parentDbName.equals("Users"))
                          {
                              Toast.makeText(loginActivity.this, "Logged in successfully ....", Toast.LENGTH_SHORT).show();
                              loadingBar.dismiss();
                              Intent intent = new Intent(loginActivity.this , HomeActivity.class);
                              startActivity(intent);
                              Prevalent.currentOnLineUser = usersData ;
                          }
                       }
                       else
                       {
                           Toast.makeText(loginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                       }


                   }

               }
               else
               {
                   Toast.makeText(loginActivity.this,"Please enter correct Phone Number ",Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

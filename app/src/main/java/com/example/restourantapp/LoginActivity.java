package com.example.restourantapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restourantapp.view.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class    LoginActivity extends AppCompatActivity {

    Button callSignUp;
    Button login_btn;
    Button login_google_btn;
    TextView forgotPassword;
    FirebaseAuth mFirebaseAuth;


     TextInputEditText username, password;


    @Override
protected  void onCreate (Bundle savedInsatanceState ){
    super.onCreate(savedInsatanceState);
    setContentView(R.layout.login_activity);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final  DatabaseReference table_user = database.getReference("Users");


    username=findViewById(R.id.username1);
    password=findViewById(R.id.password1);
    login_btn=findViewById(R.id.login1);
    login_btn.setOnClickListener(new View.OnClickListener() {



        @Override
        public void onClick(View v) {
            final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(username.getText().toString()).exists()) {

                        mDialog.dismiss();
                        User user = dataSnapshot.child(username.getText().toString()).getValue(User.class);
                        if (user.getPassword().equals(password.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "Logedin successfully", Toast.LENGTH_SHORT).show();
                            Intent intentToDashboard = new Intent(getApplicationContext(),SignUpActivity.class);
                            startActivity(intentToDashboard);

                        } else {
                            Toast.makeText(LoginActivity.this, "Logedin in failed!!!", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }});}





}

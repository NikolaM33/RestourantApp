package com.example.restourantapp;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restourantapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    Button login_btn, btnSignUp;

    TextView forgotPassword, verifyEmail;
    FirebaseAuth mAuth;


    TextInputEditText username, password;
    TextInputLayout emailLayout, passwordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.login_activity);


        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.singup1);
        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        verifyEmail = findViewById(R.id.verifyEmail);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        login_btn = findViewById(R.id.login1);
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                verifyEmail(email);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intentToSignUp);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (validateLogin()) {
                    String email = username.getText().toString();
                    String inputPassword = password.getText().toString();
                    loginUser(email, inputPassword);
                }


            }
        });
    }

    private void loginUser(String email, final String password) {
        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Logeed in", Toast.LENGTH_LONG).show();
                        Intent toHome = new Intent(getApplicationContext(), RestaurantsList.class);
                        startActivity(toHome);
                        mDialog.dismiss();
                        finish();
                    } else {
                        emailLayout.setError("Email not verified. Please verify your email!");
                        verifyEmail.setVisibility(View.VISIBLE);
                        mDialog.dismiss();
                    }

                } else {
                    String message = task.getException().toString();
                    mDialog.dismiss();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException invalidEmail) {
                        emailLayout.setError("Invalid email");
                    } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                        passwordLayout.setError("Wrong password");

                    } catch (Exception e) {
                        e.getMessage().toString();
                    }

                }
            }
        });

    }

    private boolean validateLogin() {
        if (username.getText().toString().isEmpty()) {
            emailLayout.setError("Enter your email");
            return false;

        } else if (password.getText().toString().isEmpty()) {
            passwordLayout.setError("Enter password!");
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
            passwordLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void verifyEmail(String enteredEmail) {
        Toast.makeText(LoginActivity.this, "Verifyacitveted", Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("VERIFY EMAIL!");
        alertDialog.setMessage("Verification link is sent to email:");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_verifyEmail = inflater.inflate(R.layout.verify_email_layout, null);


        TextInputEditText email = layout_verifyEmail.findViewById(R.id.edtVerifyEmail);
        TextInputLayout emailLayout = layout_verifyEmail.findViewById(R.id.verifyEmailInputLayout);
        email.setText(enteredEmail);
        alertDialog.setView(layout_verifyEmail);
        alertDialog.setPositiveButton("Resend link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.reload();
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginActivity.this, "Verification email has been sent", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.getMessage().toString();
                        // Log.d(TAG, "onFailure:Email not sent"+e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();


                    }
                });
            }
        });
        alertDialog.show();


    }


}

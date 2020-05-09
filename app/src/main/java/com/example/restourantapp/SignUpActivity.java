package com.example.restourantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restourantapp.Model.User;
import com.example.restourantapp.view.CountryAdapter;
import com.example.restourantapp.view.CountrySpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private ArrayList<CountrySpinner> mCountryList;
    private CountryAdapter mAdapter;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference table_user = database.getReference("Users");
    private TextInputEditText userName;
    private TextInputEditText userLastName;
    private TextInputEditText userEmail;
    private TextInputEditText userPhoneNumber;
    private TextInputEditText userPassword;
    private Button createAccount;
    private String selectedCountryCode;
    private TextInputLayout userNameLayout, lastNameLayout, phoneNumberLayout, emailLayout, passwordLayout;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initList();
        initializeFields();
        mAuth = FirebaseAuth.getInstance();
        Spinner spinnerCountries = findViewById(R.id.spinner);
        mAdapter = new CountryAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountrySpinner selectedItem = (CountrySpinner) parent.getItemAtPosition(position);
                selectedCountryCode = selectedItem.getCountryCode();
                Toast.makeText(SignUpActivity.this, selectedCountryCode, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() || !validateLastName() || !validateEmail() || !validatePhoneNo() || !validatePassword()) {
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();

                } else {
                    createNewAccount();
                }
            }
        });
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        progressDialog.setTitle("Waiting for creating account");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //verification email
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this, "Verification email has been sent", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.d(TAG, "onFailure:Email not sent"+e.getMessage());
                            Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();


                        }
                    });
                    final String uid = FirebaseAuth.getInstance().getUid();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = new User(userName.getText().toString(), userPassword.getText().toString(), userEmail.getText().toString(),
                                    fullPhoneNumber(), userLastName.getText().toString());
                            table_user.child(uid).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Added in base ", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(SignUpActivity.this, uid, Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        emailLayout.setError("This email is already used from another account");
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        String message = task.getException().toString();
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }


    private void initList() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountrySpinner("+386", R.drawable.flag_serbia));
        mCountryList.add(new CountrySpinner("+383", R.drawable.flag_kosovo));
    }

    private void initializeFields() {
        userName = findViewById(R.id.edtInputName);
        userLastName = findViewById(R.id.edtInputLastName);
        userEmail = findViewById(R.id.edtInputEmail);
        userPhoneNumber = findViewById(R.id.edtInputPhoneNumber);
        userPassword = findViewById(R.id.edtInputPassword);
        createAccount = findViewById(R.id.btnContinue);
        progressDialog = new ProgressDialog(this);
        userNameLayout = findViewById(R.id.txtInputName);
        lastNameLayout = findViewById(R.id.txtInputLastName);
        phoneNumberLayout = findViewById(R.id.txtInputPhone);
        emailLayout = findViewById(R.id.txtInputEmail);
        passwordLayout = findViewById(R.id.txtInputPassword);

    }

    private String fullPhoneNumber() {
        String phoneNumber = userPhoneNumber.getText().toString();
        Character ch1 = phoneNumber.charAt(0);
        Character ch2 = 0;
        if (ch1.equals(ch2)) {
            String phone = phoneNumber.substring(1);
            return selectedCountryCode + phone;

        } else {
            return selectedCountryCode + phoneNumber;
        }

    }

    private boolean validateName() {
        String val = userName.getText().toString();
        if (val.isEmpty()) {
            userNameLayout.setError("Field cannot be empty");
            return false;
        } else {
            userNameLayout.setError(null);
            userNameLayout.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validateEmail() {
        String val = userEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            emailLayout.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailLayout.setError("Invalid email address");
            return false;

        } else {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validatePhoneNo() {
        String val = userPhoneNumber.getText().toString();

        if (val.isEmpty()) {
            phoneNumberLayout.setError("Field cannot be empty");
            return false;
        } else {
            phoneNumberLayout.setError(null);
            phoneNumberLayout.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validatePassword() {
        String val = userPassword.getText().toString();
        String valPassword = "^(?=\\A\\w{6,20}\\z).{6,}$";
        if (val.isEmpty()) {
            passwordLayout.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(valPassword)) {
            passwordLayout.setError("Password is too weak");
            return false;
        } else {

            passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validateLastName() {
        String val = userLastName.getText().toString();
        if (val.isEmpty()) {
            lastNameLayout.setError("Field cannot be empty");
            return false;
        } else {
            lastNameLayout.setError(null);
            lastNameLayout.setErrorEnabled(false);
            return true;

        }
    }


}

package com.example.restourantapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.restourantapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    private TextInputEditText password;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText phoneNumber;
    private Button btnLogout;
    private TextView txtFullName, txtUsername;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private User currentUser;
    private ProgressDialog progressDialog;
    private CardView orderCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        password = findViewById(R.id.user_password);
        email = findViewById(R.id.user_email);

        name = findViewById(R.id.edtUser_name);
        phoneNumber = findViewById(R.id.user_phoneNumber);
        btnLogout = findViewById(R.id.btn_logout);
        txtFullName = findViewById(R.id.user_name);
        txtUsername = findViewById(R.id.username);
        progressDialog = new ProgressDialog(this);
        orderCard = findViewById(R.id.orderCard);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailChangeDialog();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeName();
            }
        });
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePhoneNumber();
            }
        });
        fillInformation();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(UserProfile.this, OrderStatus.class);
                startActivity(intent);

            }
        });
        orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, OrderStatus.class));
            }
        });

    }

    public void fillInformation() {
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                txtFullName.setText(currentUser.getName() + " " + currentUser.getLastName());
                txtUsername.setText(mAuth.getCurrentUser().getEmail());
                name.setText(currentUser.getName() + " " + currentUser.getLastName());
                email.setText(mAuth.getCurrentUser().getEmail());
                phoneNumber.setText(currentUser.getPhoneNumber());
                password.setText(currentUser.getPassword());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "Error. Connection with Database", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showEmailChangeDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
        alertDialog.setTitle("CHANGE EMAIL");
        alertDialog.setMessage("Please enter new email address!");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_changeEmail = inflater.inflate(R.layout.change_email_layout, null);


        final TextInputEditText new_email = layout_changeEmail.findViewById(R.id.edtEmailChange);
        alertDialog.setView(layout_changeEmail);
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!(new_email.getText().toString().isEmpty())) {
                    final String newEmail = new_email.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (newEmail.matches(emailPattern)) {
                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference.child(mAuth.getCurrentUser().getUid()).child("email").setValue(newEmail);
                                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UserProfile.this, "Verification email has been sent!!", Toast.LENGTH_SHORT).show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mAuth.signOut();
                                                    startActivity(new Intent(UserProfile.this, LoginActivity.class));
                                                }
                                            }, 4000);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(UserProfile.this, "Error connecting with database try again later!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(UserProfile.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserProfile.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please fill all information");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_changePassword = inflater.inflate(R.layout.change_password_layout, null);

        final TextInputLayout layoutOld_password = layout_changePassword.findViewById(R.id.layoutOldPassword);
        final TextInputLayout layoutNew_password = layout_changePassword.findViewById(R.id.layoutNewPassword);
        final TextInputLayout layoutConfirm_password = layout_changePassword.findViewById(R.id.layoutConfirmPassword);
        final TextInputEditText old_password = layout_changePassword.findViewById(R.id.old_password);
        final TextInputEditText new_password = layout_changePassword.findViewById(R.id.new_password);
        final TextInputEditText repeat_NewPassword = layout_changePassword.findViewById(R.id.repeat_password);
        alertDialog.setView(layout_changePassword);

        //Button
        old_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if ((old_password.getText().toString().trim()).equals(currentUser.getPassword())) {
                        layoutOld_password.setErrorEnabled(false);
                        new_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    final String newPassword = new_password.getText().toString().trim();
                                    if (!(newPassword.equals(currentUser.getPassword()))) {
                                        layoutNew_password.setErrorEnabled(false);
                                        if (newPassword.length() > 6) {

                                            layoutNew_password.setErrorEnabled(false);
                                            repeat_NewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View v, boolean hasFocus) {
                                                    if (!hasFocus) {
                                                        String confirmPassword = repeat_NewPassword.getText().toString().trim();
                                                        if (!(confirmPassword.equals(newPassword))) {
                                                            layoutConfirm_password.setError("Passwords doesn't match!");
                                                        } else {
                                                            layoutConfirm_password.setErrorEnabled(false);
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            layoutNew_password.setError("Password must have more then six characters");
                                        }
                                    } else {
                                        layoutNew_password.setError("Password cannot be same as old!!");
                                    }
                                }
                            }
                        });
                    } else {
                        layoutOld_password.setError("Password is incorrect!");
                    }
                }
            }
        });
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //change password here
                progressDialog.setTitle("Changing password....");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                if ((old_password.getText().toString().trim()).equals(currentUser.getPassword())) {
                    if (!(new_password.getText().toString().trim()).equals(currentUser.getPassword())) {
                        final String newPassword = new_password.getText().toString().trim();
                        if (newPassword.length() >= 6) {
                            if ((newPassword.equals(repeat_NewPassword.getText().toString().trim()))) {

                                final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            reference.child(mAuth.getCurrentUser().getUid()).child("password").setValue(newPassword);
                                            Toast.makeText(UserProfile.this, "Passwords is changed. Please Login with new password", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(UserProfile.this, LoginActivity.class));
                                            Toast.makeText(UserProfile.this, "Passwords is changed. Please Login with new password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(UserProfile.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "Passwords must have more the 6 characters", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    } else {
                        Toast.makeText(UserProfile.this, "New password cannot be same as old!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(UserProfile.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }




            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }


    private void showChangeName() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
        alertDialog.setTitle("CHANGE NAME");
        alertDialog.setMessage("Please fill all information");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_changeName = inflater.inflate(R.layout.change_name_layout, null);

        final TextInputEditText new_name = layout_changeName.findViewById(R.id.new_name);
        final TextInputEditText new_surname = layout_changeName.findViewById(R.id.new_surname);
        alertDialog.setView(layout_changeName);

        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!new_name.getText().toString().isEmpty()) {
                    String name = new_name.getText().toString();
                    reference.child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);


                }
                if (!new_surname.getText().toString().isEmpty()) {
                    String newLastName = new_surname.getText().toString();
                    reference.child(mAuth.getCurrentUser().getUid()).child("lastName").setValue(newLastName);

                }

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void showChangePhoneNumber() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
        alertDialog.setTitle("CHANGE PHONE NUMBER");
        alertDialog.setMessage("Please fill all information");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_changePhoneNumber = inflater.inflate(R.layout.change_phone_number_layout, null);

        final TextInputEditText new_phoneNumber = layout_changePhoneNumber.findViewById(R.id.new_phoneNumber);

        alertDialog.setView(layout_changePhoneNumber);

        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!new_phoneNumber.getText().toString().isEmpty()) {
                    String newPhoneNumber = new_phoneNumber.getText().toString().trim();
                    reference.child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(newPhoneNumber);

                }

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }


}

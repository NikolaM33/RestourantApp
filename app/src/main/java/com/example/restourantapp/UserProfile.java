package com.example.restourantapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {
    private TextInputEditText password;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        password = findViewById(R.id.user_password);
        email = findViewById(R.id.user_email);
        name = findViewById(R.id.edtUser_name);
        phoneNumber = findViewById(R.id.user_phoneNumber);


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

    }

    private void showEmailChangeDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
        alertDialog.setTitle("CHANGE EMAIL");
        alertDialog.setMessage("Please enter new email address!");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_changeEmail = inflater.inflate(R.layout.change_email_layout, null);


        TextInputEditText new_email = layout_changeEmail.findViewById(R.id.edtEmailChange);
        alertDialog.setView(layout_changeEmail);
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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


        TextInputEditText old_password = layout_changePassword.findViewById(R.id.old_password);
        TextInputEditText new_password = layout_changePassword.findViewById(R.id.new_password);
        TextInputEditText repeat_NewPassword = layout_changePassword.findViewById(R.id.repeat_password);
        alertDialog.setView(layout_changePassword);
        //Button
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //change password here

                // android.app.AlertDialog waitingDialog= new SpotsDialog (UserProfile.this);
                //waitingDialog.show();

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

        TextInputEditText new_name = layout_changeName.findViewById(R.id.new_name);
        TextInputEditText new_surname = layout_changeName.findViewById(R.id.new_surname);
        alertDialog.setView(layout_changeName);

        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

        TextInputEditText new_phoneNumber = layout_changePhoneNumber.findViewById(R.id.new_phoneNumber);

        alertDialog.setView(layout_changePhoneNumber);

        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

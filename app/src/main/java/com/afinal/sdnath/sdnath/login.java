package com.afinal.sdnath.sdnath;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    Button login, SignUp,Learn_More;
    String myphone, myemail, mypassword;
    EditText phone, password;
    ProgressDialog dialog;
    FirebaseAuth mauth;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mauth.getCurrentUser();
        if (currentUser!=null){
            Intent intent = new Intent(login.this, Home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.pass);

        login = findViewById(R.id.login);
        SignUp = findViewById(R.id.signUp);
        Learn_More = findViewById(R.id.learnmore);
        dialog = new ProgressDialog(login.this);

        mauth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Please Wait ...");
                dialog.show();


                myphone = phone.getText().toString().trim();
                myemail = myphone + "@gmail.com";
                mypassword = password.getText().toString().trim();


                if (!TextUtils.isEmpty(myemail) && !TextUtils.isEmpty(mypassword)) {

                    mauth.signInWithEmailAndPassword(myemail, mypassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Intent intent = new Intent(login.this, Home.class);
                                startActivity(intent);

                            } else if (!isNetworkAvailable()) {
                                Toast.makeText(login.this, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(login.this, "LogIn failed. Please input correct phone number & password", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                } else {
                    Toast.makeText(login.this, "Please fill up all the required fields", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, SignUp.class);
                startActivity(intent);
            }
        });

        Learn_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Learn_More.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Home.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);
                finish();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }

}

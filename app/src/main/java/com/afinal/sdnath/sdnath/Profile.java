package com.afinal.sdnath.sdnath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Profile extends AppCompatActivity {

    Button edit;
    TextView name,gender,blood, age, weight,lastDonate,phone, thana,district;
    String myname, mygender, myblood, mylastDonate, myage, myweight, myphone, mydistrict,mythana;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        uid=firebaseAuth.getUid();

        name=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        blood=findViewById(R.id.bloodGroup);
        age=findViewById(R.id.age);
        weight=findViewById(R.id.weight);
        lastDonate=findViewById(R.id.lastDonate);
        phone=findViewById(R.id.phone);
        district=findViewById(R.id.district);
        thana=findViewById(R.id.thana);
        edit = findViewById(R.id.edit);




        firebaseFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot!=null) {
                    name.setText(documentSnapshot.getString("name"));
                    gender.setText(documentSnapshot.getString("gender"));
                    blood.setText(documentSnapshot.getString("blood"));
                    age.setText(documentSnapshot.getString("age"));
                    weight.setText(documentSnapshot.getString("weight"));
                    lastDonate.setText(documentSnapshot.getString("lastDonate"));
                    phone.setText(documentSnapshot.getString("phone"));
                    district.setText(documentSnapshot.getString("district"));
                    thana.setText(documentSnapshot.getString("thana"));
                }
            }
        });




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Profile.this,EditProfile.class);
                startActivity(intent);
            }
        });
    }




//SETTING BACK ARROW
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

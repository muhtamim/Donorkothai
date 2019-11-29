package com.afinal.sdnath.sdnath;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {


    EditText name, age, weight, phone, email, password;
    String myname, mygender, myblood, mylastDonate, myage, myweight, myphone, myemail, mypassword, mydistrict;
    String mythana = "empty";
    String uid;
    RadioButton male, female;
    LinearLayout thanaLayout, emptyLayout;
    Spinner blood, district, thana;

    String canDonate;

    Button signUp;

    TextView lastDonate;
    Calendar lastDonateDay;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    int dayint;
    String dayArray[] = {"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    public String dayOfWeek;

    ProgressDialog dialog;

    FirebaseAuth auth;
    Firebase PrimaryKeyUid;
    FirebaseFirestore myFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");


        auth = FirebaseAuth.getInstance();
        myFirestore = FirebaseFirestore.getInstance();

        Firebase.setAndroidContext(SignUp.this);
        dialog = new ProgressDialog(SignUp.this);


        name = findViewById(R.id.name);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        blood = findViewById(R.id.bloodGroup);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        lastDonate = findViewById(R.id.lastDonate);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        district = findViewById(R.id.district);
        thanaLayout = findViewById(R.id.thanaLayout);
        thana = findViewById(R.id.thana);

        signUp = findViewById(R.id.signUp);


        lastDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lastDonateDay = Calendar.getInstance();


                if (mylastDonate!=null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(mylastDonate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    lastDonateDay.setTime(date);
                    year = lastDonateDay.get(Calendar.YEAR);
                    month = lastDonateDay.get(Calendar.MONTH);
                    dayOfMonth = lastDonateDay.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    year = lastDonateDay.get(Calendar.YEAR);
                    month = lastDonateDay.get(Calendar.MONTH);
                    dayOfMonth = lastDonateDay.get(Calendar.DAY_OF_MONTH);
                }


                datePickerDialog = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                lastDonateDay.set(year, month, day);


                                dayint = lastDonateDay.get(Calendar.DAY_OF_WEEK);
                                dayOfWeek = dayArray[dayint];

                                //lastDonate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(lastDonateDay.getTime()));
                                //mylastDonate=DateFormat.getDateInstance(DateFormat.DEFAULT).format(lastDonateDay.getTime());

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                mylastDonate = (simpleDateFormat.format(lastDonateDay.getTime()));
                                lastDonate.setText(mylastDonate);


                                //datePickerDialog.updateDate(lastDonateDay.get(Calendar.YEAR),5,lastDonateDay.get(Calendar.DAY_OF_MONTH));


                                //setting selected date to the calender instance
                                Date date = null;
                                try {
                                    date = simpleDateFormat.parse(mylastDonate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                lastDonateDay.setTime(date);


                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();


            }
        });


        blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myblood = (String) adapterView.getItemAtPosition(i);


                if ("--Select Blood Group--".equals(myblood)) {
                    myblood = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mydistrict = (String) adapterView.getItemAtPosition(i);


                if ("--Select District--".equals(mydistrict)) {
                    mydistrict = null;
                } else if ("DHAKA".equals(mydistrict)) {
                    thanaLayout.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUp.this, R.array.DHAKA, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                } else if ("CHITTAGONG".equals(mydistrict)) {
                    thanaLayout.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUp.this, R.array.CHITTAGONG, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                }

                else if ("SYLHET".equals(mydistrict)) {
                    thanaLayout.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUp.this, R.array.SYLHET, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        thana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mythana = (String) adapterView.getItemAtPosition(i);


                if ("--Select Thana--".equals(mythana)) {
                    mythana = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myname = name.getText().toString().trim();
                if (male.isChecked()) {
                    mygender = "Male";
                } else if (female.isChecked()) {
                    mygender = "Female";
                }
                myage = age.getText().toString().trim();
                myweight = weight.getText().toString().trim();
                myphone = phone.getText().toString().trim();
                myemail = myphone + "@gmail.com";
                mypassword = password.getText().toString().trim();


                dialog.setMessage("Please Wait ...");
                dialog.show();


                if (mylastDonate != null && myblood != null && mydistrict != null && ((thanaLayout.getVisibility() == View.VISIBLE && mythana != null) || (thanaLayout.getVisibility() == View.GONE))) {


                    if (!TextUtils.isEmpty(myname) && !TextUtils.isEmpty(mygender) && !TextUtils.isEmpty(myage) && !TextUtils.isEmpty(myweight) && !TextUtils.isEmpty(myphone) && !TextUtils.isEmpty(myemail) && !TextUtils.isEmpty(mypassword)) {


                        auth.createUserWithEmailAndPassword(myemail, mypassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Intent homeintent = new Intent(SignUp.this, Home.class);
                                    Toast.makeText(SignUp.this, "Registration successfully completed", Toast.LENGTH_SHORT).show();
                                    startActivity(homeintent);
                                    finish();


                                    // Write data to the database


                                    uid = auth.getUid();

                                    //writing into firebase database

                                    PrimaryKeyUid = new Firebase("https://sdnath.firebaseio.com/users/" + uid);
                                    Firebase userName = PrimaryKeyUid.child("name");
                                    userName.setValue(myname);
                                    Firebase userGender = PrimaryKeyUid.child("gender");
                                    userGender.setValue(mygender);
                                    Firebase userBlood = PrimaryKeyUid.child("blood");
                                    userBlood.setValue(myblood);
                                    Firebase userAge = PrimaryKeyUid.child("age");
                                    userAge.setValue(myage);
                                    Firebase userWeight = PrimaryKeyUid.child("weight");
                                    userWeight.setValue(myweight);
                                    Firebase userLastDonate = PrimaryKeyUid.child("lastDonate");
                                    userLastDonate.setValue(mylastDonate);
                                    Firebase userPhone = PrimaryKeyUid.child("phone");
                                    userPhone.setValue(myphone);
                                    Firebase userEmail = PrimaryKeyUid.child("email");
                                    userEmail.setValue(myemail);
                                    Firebase userPassword = PrimaryKeyUid.child("password");
                                    userPassword.setValue(mypassword);
                                    Firebase userDistrict = PrimaryKeyUid.child("district");
                                    userDistrict.setValue(mydistrict);
                                    Firebase userThana = PrimaryKeyUid.child("thana");
                                    userThana.setValue(mythana);


                                    //Writing into firestore

                                    Map<String, String> setvalue = new HashMap<>();
                                    setvalue.put("name", myname);
                                    setvalue.put("gender", mygender);
                                    setvalue.put("blood", myblood);
                                    setvalue.put("age", myage);
                                    setvalue.put("weight", myweight);
                                    setvalue.put("lastDonate", mylastDonate);
                                    setvalue.put("phone", myphone);
                                    setvalue.put("email", myemail);
                                    setvalue.put("password", mypassword);
                                    setvalue.put("district", mydistrict);
                                    setvalue.put("thana", mythana);

                                    myFirestore.collection("users").document(uid).set(setvalue);

                                    //myFirestore.collection("location").document(mydistrict).collection(mythana).document(myblood).set(setvalue);






                                    //checking blood donation date is 4 month before or not

                                    long differece = System.currentTimeMillis() - lastDonateDay.getTimeInMillis();
                                    long days = differece / (24 * 60 * 60 * 1000);

                                    if (days >= 120) {
                                        canDonate = "1";
                                    } else {
                                        canDonate = "0";
                                    }


                                    //saving blood info

                                    Map<String, String> setValueBlood = new HashMap<>();

                                    //setValueBlood.put("uid",uid);
                                    setValueBlood.put("canDonate", canDonate);

                                    myFirestore.collection("blood").document("bank").collection(mydistrict).document(mythana).collection(myblood).document(uid).set(setValueBlood);


                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignUp.this, "An account already exist with this phone number", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (e instanceof FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(SignUp.this, "Please use a strong password using both number & characters minimum 6 digit", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(SignUp.this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(SignUp.this, "Please fill up all the required fields", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


                } else {
                    Toast.makeText(SignUp.this, "Select your Blood Group, Last Donation, District,Thana", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


            }
        });
    }


    //SETTING BACK ARROW
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

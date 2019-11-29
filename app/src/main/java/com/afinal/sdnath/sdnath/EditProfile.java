package com.afinal.sdnath.sdnath;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class EditProfile extends AppCompatActivity {


    Button save;
    EditText name, age, weight;
    TextView lastDonate;
    TextView phone;
    RadioButton male, female;
    Spinner blood,district,thana;
    String canDonate;
    String myname, mygender, myblood, mylastDonate, myage, myweight, myphone, mydistrict, mythana;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String uid;

    Calendar lastDonateDay;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    int dayint;
    String dayArray[] = {"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    public String dayOfWeek;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getUid();

        name = findViewById(R.id.name);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        blood = findViewById(R.id.bloodGroup);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        lastDonate = findViewById(R.id.lastDonate);
        phone = findViewById(R.id.phone);
        district=findViewById(R.id.district);
        thana=findViewById(R.id.thana);

        save = findViewById(R.id.save);
        dialog = new ProgressDialog(EditProfile.this);


        firebaseFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    name.setText(documentSnapshot.getString("name"));

                    if (documentSnapshot.getString("gender").equals("Male")) {
                        male.setChecked(true);
                        female.setChecked(false);
                    } else if(documentSnapshot.getString("gender").equals("Female")) {
                        male.setChecked(false);
                        female.setChecked(true);
                    }
                    myblood = documentSnapshot.getString("blood");


                    age.setText(documentSnapshot.getString("age"));
                    weight.setText(documentSnapshot.getString("weight"));
                    lastDonate.setText(documentSnapshot.getString("lastDonate"));
                    phone.setText(documentSnapshot.getString("phone"));


                    mydistrict = documentSnapshot.getString("district");
                    mythana = documentSnapshot.getString("thana");
                }
            }
        });


        blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              String  mySelectedBlood = (String) adapterView.getItemAtPosition(i);



                if ("--Select Blood Group--".equals(mySelectedBlood)) {
                    //do nothing,keep the previous blood group into the string
                }
                else {
                    myblood = (String) adapterView.getItemAtPosition(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lastDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lastDonateDay = Calendar.getInstance();


                if (mylastDonate != null) {
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
                } else {
                    year = lastDonateDay.get(Calendar.YEAR);
                    month = lastDonateDay.get(Calendar.MONTH);
                    dayOfMonth = lastDonateDay.get(Calendar.DAY_OF_MONTH);
                }


                datePickerDialog = new DatePickerDialog(EditProfile.this,
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

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfile.this, "Phone number can not be edited", Toast.LENGTH_SHORT).show();
            }
        });


        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              String  mySelectedDistrict = (String) adapterView.getItemAtPosition(i);

                if ("--Select District--".equals(mySelectedDistrict)) {

                } else if ("DHAKA".equals(mySelectedDistrict)) {
                    mydistrict = (String) adapterView.getItemAtPosition(i);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfile.this, R.array.DHAKA, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(EditProfile.this, "Now select your thana", Toast.LENGTH_SHORT).show();
                } else if ("CHITTAGONG".equals(mySelectedDistrict)) {
                    mydistrict = (String) adapterView.getItemAtPosition(i);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfile.this, R.array.CHITTAGONG, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(EditProfile.this, "Now select your thana", Toast.LENGTH_SHORT).show();

                }

                else if ("SYLHET".equals(mySelectedDistrict)) {
                    mydistrict = (String) adapterView.getItemAtPosition(i);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfile.this, R.array.SYLHET, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(EditProfile.this, "Now select your thana", Toast.LENGTH_SHORT).show();
                }

                else {
                    mydistrict = (String) adapterView.getItemAtPosition(i);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfile.this, R.array.EMPTY, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    mythana = "empty";

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        thana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               String mySelectedthana = (String) adapterView.getItemAtPosition(i);

                if ("--Select Thana--".equals(mySelectedthana)) {
                    //do nothing
                } else {
                    mythana = (String) adapterView.getItemAtPosition(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditProfile.this);
                builder.setMessage("Do you want to save the changes?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveProfile();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


    }


    public void saveProfile() {

        dialog.setMessage("Saving...");
        dialog.show();

        if (isNetworkAvailable()) {
            myname = name.getText().toString().trim();
            if (male.isChecked()) {
                mygender = "Male";
            } else if (female.isChecked()) {
                mygender = "Female";
            }
            myage = age.getText().toString().trim();
            myweight = weight.getText().toString().trim();
            myphone = phone.getText().toString().trim();


            //Writing into firestore

            Map<String, Object> setvalue = new HashMap<>();
            setvalue.put("name", myname);
            setvalue.put("gender", mygender);
            setvalue.put("blood", myblood);
            setvalue.put("age", myage);
            setvalue.put("weight", myweight);
            setvalue.put("lastDonate", mylastDonate);
            setvalue.put("phone", myphone);
            setvalue.put("district", mydistrict);
            setvalue.put("thane", mythana);

            firebaseFirestore.collection("users").document(uid).update(setvalue);


            //checking blood donation date is 4 month before or not

            long differece = System.currentTimeMillis() - lastDonateDay.getTimeInMillis();
            long days = differece / (24 * 60 * 60 * 1000);

            if (days >= 120) {
                canDonate = "1";
            } else {
                canDonate = "0";
            }


            //saving blood info

            Map<String, Object> setValueBlood = new HashMap<>();

            //setValueBlood.put("uid",uid);
            setValueBlood.put("canDonate", canDonate);

            firebaseFirestore.collection("blood").document("bank").collection(mydistrict).document(mythana).collection(myblood).document(uid).update(setValueBlood);

            finish();
        } else {
            dialog.dismiss();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Internet connection not available");
            builder.setCancelable(true);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Home.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

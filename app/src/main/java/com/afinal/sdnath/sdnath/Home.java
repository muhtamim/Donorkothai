package com.afinal.sdnath.sdnath;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Spinner district, thana;
    String mydistrict="null";
    String mythana = "empty";
    NavigationView navigationView;
    Button sendAP, sendAN, sendBP, sendBN, sendABP, sendABN, sendOP, sendON;
    TextView aP, aN, bP, bN, abP, abN, oP, oN;
    String maP, maN, mbP, mbN, mabP, mabN, moP, moN;
    int countAP = 0;
    int countAN = 0;
    int countBP = 0;
    int countBN = 0;
    int countABP = 0;
    int countABN = 0;
    int countOP = 0;
    int countON = 0;

    TextView name, address;
    ProgressDialog dialog;

    FirebaseAuth auth;
    String uid;
    FirebaseFirestore myFirestore;

    Date internetDate;


    String myname, myphone, myaddress, myaddressD, myaddressT, myblood;
    String mybag, myhospital;
    String requestBlood;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        district = findViewById(R.id.district);
        thana = findViewById(R.id.thana);
        sendAP = findViewById(R.id.sendAP);
        sendAN = findViewById(R.id.sendAN);
        sendBP = findViewById(R.id.sendBP);
        sendBN = findViewById(R.id.sendBN);
        sendABP = findViewById(R.id.sendABP);
        sendABN = findViewById(R.id.sendABN);
        sendOP = findViewById(R.id.sendOP);
        sendON = findViewById(R.id.sendON);

        aP = findViewById(R.id.aP);
        aN = findViewById(R.id.aN);
        bP = findViewById(R.id.bP);
        bN = findViewById(R.id.bN);
        abP = findViewById(R.id.abP);
        abN = findViewById(R.id.abN);
        oP = findViewById(R.id.oP);
        oN = findViewById(R.id.oN);


        auth = FirebaseAuth.getInstance();
        myFirestore = FirebaseFirestore.getInstance();
        uid = auth.getUid();
        initFCM();

        dialog = new ProgressDialog(Home.this);


//nav Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);

        name = hView.findViewById(R.id.name);
        address = hView.findViewById(R.id.address);

        setNavContents();


        myFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    myname = documentSnapshot.getString("name");
                    myphone = documentSnapshot.getString("phone");
                    myaddressD = documentSnapshot.getString("district");
                    myblood = documentSnapshot.getString("blood");

                    if ("empty".equals(documentSnapshot.getString("thana"))) {
                        myaddress = myaddressD;
                    } else {
                        myaddressT = documentSnapshot.getString("thana");
                        myaddress = myaddressT + " , " + myaddressD;
                    }

                }
            }
        });


        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mydistrict = (String) adapterView.getItemAtPosition(i);

                clearContents();
                if ("--Select District--".equals(mydistrict)) {
                    mydistrict = "null";
                } else if ("DHAKA".equals(mydistrict)) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Home.this, R.array.DHAKA, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(Home.this, "Now select your thana", Toast.LENGTH_SHORT).show();
                } else if ("CHITTAGONG".equals(mydistrict)) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Home.this, R.array.CHITTAGONG, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(Home.this, "Now select your thana", Toast.LENGTH_SHORT).show();
                }


                else if ("SYLHET".equals(mydistrict)) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Home.this, R.array.SYLHET, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    thana.requestFocus();
                    Toast.makeText(Home.this, "Now select your thana", Toast.LENGTH_SHORT).show();
                }

                else {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Home.this, R.array.EMPTY, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    thana.setAdapter(adapter);
                    mythana = "empty";
                    setContentsForDistrict();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                mydistrict="null";
            }
        });


        thana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mythana = (String) adapterView.getItemAtPosition(i);

                clearContents();
                if ("--Select Thana--".equals(mythana)) {
                    mythana = "null";
                } else {
                    clearContents();
                    setContentsForThana();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                mythana="empty";
            }
        });




        sendAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Error",mydistrict+mythana+"hjvjhb");

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("A+", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict)) || "SYLHET".equals(mydistrict) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("A-", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict) || "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("B+", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict)|| "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("B-", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict) || "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendABP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("AB+", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict)|| "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendABN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("AB-", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict) || "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("O+", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict) || "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sendON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!("null".equals(mydistrict)) && !("null".equals(mythana))) {
                    sendRequest("O-", myname, myphone, myaddress);
                } else if (("DHAKA".equals(mydistrict) || "CHITTAGONG".equals(mydistrict)|| "SYLHET".equals(mydistrict)) && ("null".equals(mythana))) {
                    Toast.makeText(Home.this, "Select any thana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, "First select any district", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //setting notification chanel for android oreo and higher
/***
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("BD", "BloodDonor", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
****/
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + Home.this.getPackageName() + "/" + R.raw.blood_notification_sound);

        //Log.d("Error",Home.this.getPackageName());
        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel("BD", "DonorKothai", NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            // Configure the notification channel.
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(sound, attributes); // This is IMPORTANT

            NotificationManager mNotificationManager = getSystemService(NotificationManager.class);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }


        }



    }

    public void sendRequest(final String blood, final String name, final String phone, final String address) {

        //getting time in background
        //new TimeTask().execute((Void[]) null);


        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

        View view = LayoutInflater.from(Home.this).inflate(R.layout.custom_dialog_input, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();


        ImageView img = view.findViewById(R.id.img);
        Spinner bag = view.findViewById(R.id.bag);
        final EditText hospital = view.findViewById(R.id.hospital);
        Button request = view.findViewById(R.id.request);


        if ("A+".equals(blood)) {
            requestBlood = "A+";
            img.setImageResource(R.mipmap.ap);
        }
        if ("A-".equals(blood)) {
            requestBlood = "A-";
            img.setImageResource(R.mipmap.an);
        }
        if ("B+".equals(blood)) {
            requestBlood = "B+";
            img.setImageResource(R.mipmap.bp);
        }
        if ("B-".equals(blood)) {
            requestBlood = "B-";
            img.setImageResource(R.mipmap.bn);
        }
        if ("AB+".equals(blood)) {
            requestBlood = "AB+";
            img.setImageResource(R.mipmap.abp);
        }
        if ("AB-".equals(blood)) {
            requestBlood = "AB-";
            img.setImageResource(R.mipmap.abn);
        }
        if ("O+".equals(blood)) {
            requestBlood = "O+";
            img.setImageResource(R.mipmap.op);
        }
        if ("O-".equals(blood)) {
            requestBlood = "O-";
            img.setImageResource(R.mipmap.on);
        }


        bag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mybag = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mybag = "1 Bag";
            }
        });


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myhospital = hospital.getText().toString();

                dialog.setMessage("Sending Request...");
                dialog.show();


                if (isNetworkAvailable()) {


                    Map<String, String> setvalue = new HashMap<>();
                    setvalue.put("name", name);
                    setvalue.put("uid", uid);
                    setvalue.put("phone", phone);
                    setvalue.put("address", address);
                    setvalue.put("hospital", myhospital);
                    setvalue.put("bag", mybag);
                    //setvalue.put("time", internetDate.toString());
                    setvalue.put("time",String.valueOf(System.currentTimeMillis()));
                    myFirestore.collection("notification").document("donor").collection(mydistrict).document(mythana).collection(blood).document().set(setvalue);

                    sendNotification(requestBlood+" "+"blood needed",mybag+","+" "+address);

                    alertDialog.dismiss();
                    //waiting 2 sec
                    new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            dialog.dismiss();
                            Toast.makeText(Home.this, "Request send succesfully", Toast.LENGTH_SHORT).show();
                        }
                    }.start();
                } else {
                    Toast.makeText(Home.this, "Please check network availability and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });


        alertDialog.show();
    }


    public void setNavContents() {

        myFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    name.setText(documentSnapshot.getString("name"));
                    address.setText(documentSnapshot.getString("district"));
                }
            }
        });

    }


    public void setContentsForDistrict() {

        if (isNetworkAvailable()) {
            dialog.setMessage("Please wait...");
            dialog.show();

            setContents(mydistrict, mythana, "A+", aP);
            setContents(mydistrict, mythana, "A-", aN);
            setContents(mydistrict, mythana, "B+", bP);
            setContents(mydistrict, mythana, "B-", bN);
            setContents(mydistrict, mythana, "AB+", abP);
            setContents(mydistrict, mythana, "AB-", abN);
            setContents(mydistrict, mythana, "O+", oP);
            setContents(mydistrict, mythana, "O-", oN);
        } else {
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


    public void setContents(String mydistrict, String mythana, String myblood, final TextView text) {


        DocumentReference emptyThanaRef = myFirestore.collection("blood").document("bank").collection(mydistrict).document(mythana);
        emptyThanaRef.collection(myblood)
                .whereEqualTo("canDonate", "1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        count = count + 1;
                    }
                    text.setText(String.valueOf(count));
                    dialog.dismiss();

                }
            }
        });




    }


    public void setContentsForThana() {
        if (isNetworkAvailable()) {
            dialog.setMessage("Please wait...");
            dialog.show();

            setContents(mydistrict, mythana, "A+", aP);
            setContents(mydistrict, mythana, "A-", aN);
            setContents(mydistrict, mythana, "B+", bP);
            setContents(mydistrict, mythana, "B-", bN);
            setContents(mydistrict, mythana, "AB+", abP);
            setContents(mydistrict, mythana, "AB-", abN);
            setContents(mydistrict, mythana, "O+", oP);
            setContents(mydistrict, mythana, "O-", oN);
        } else {
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

    public void clearContents() {

        aP.setText("0");
        aN.setText("0");
        bP.setText("0");
        bN.setText("0");
        abP.setText("0");
        abN.setText("0");
        oP.setText("0");
        oN.setText("0");

        countAP = 0;
        countAN = 0;
        countBP = 0;
        countBN = 0;
        countABP = 0;
        countABN = 0;
        countOP = 0;
        countON = 0;

        maP = null;
        maN = null;
        mbP = null;
        mbN = null;
        mabP = null;
        mabN = null;
        moP = null;
        moN = null;
    }


    /*****************************************************/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("Do want to exit?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    moveTaskToBack(true);
                    finish();
                    // android.os.Process.killProcess(android.os.Process.myPid());
                    // System.exit(1);
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
            //super.onBackPressed();


        }
    }


    /*******************************************************/
    //dot menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rate) {
            String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/details?id=")));
            }
            return true;
        }
        if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share play store link via"));


            return true;
        }
        if (id == R.id.about) {
            Intent intent = new Intent(Home.this, about.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /********************************************************/

    //navigation panel listener
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);

        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(Home.this, Notification.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {
            dialog.setMessage("Logging out....");
            dialog.show();
            auth.signOut();
            finish();
            Intent intent = new Intent(Home.this, login.class);
            startActivity(intent);


        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(Home.this, about.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent emailIntent = new Intent();
            emailIntent.setAction(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("donorkothai@gmail.com"));
            startActivity(emailIntent);

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share play store link via"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.nav_home);
        super.onResume();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Home.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void sendRegistrationToServer(final String token) {

        myFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        myFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                   String userDistrict = documentSnapshot.getString("district");
                   String userThana = documentSnapshot.getString("thana");
                   String userBlood = documentSnapshot.getString("blood");

                    Map<String, Object> updatevalue = new HashMap<>();
                    updatevalue.put("token", token);
                    myFirestore.collection("users").document(uid).update(updatevalue);
                    myFirestore.collection("blood").document("bank").collection(userDistrict).document(userThana).collection(userBlood)
                            .document(uid).update(updatevalue);
                }
            }
        });


    }

    private void initFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Home.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                sendRegistrationToServer(token);
            }
        });
    }


    public void sendNotification(final String title, final String body) {
        myFirestore.collection("blood").document("bank").collection(mydistrict).document(mythana).collection(requestBlood)
                .whereEqualTo("canDonate", "1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tkn = document.getString("token");
                        String doc = document.getId();
                        Log.d("Error", tkn + doc);

                        new Notify().execute(tkn,title,body);
                    }

                }
            }
        });


    }


    public static class Notify extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            //String tkn = "ck_rNJuzN3M:APA91bGUBcgl5V3S26e_BwaCYgiXFT20YvrOre3r-BBQjT3GXJZlApTtg-O2dskSmvcLjQOR7bx5mLIbDuWxq-Wtnwx5TxAnA0Bfs5Q02D1bLQLay-OD43c-e9bbxAgIA4vIEsS-PN4C";
            String tkn= params[0];
            String title=params[1];
            String body=params[2];
            Log.d("Error", tkn);
            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=AIzaSyC4NOtjDrif-n7GRZgWWr2scMCYBtAsWAI");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();

                json.put("to", tkn);


                JSONObject info = new JSONObject();
                info.put("title", title);   // Notification title
                info.put("body", body);// Notification body

                info.put("type","donor");

                //json.put("notification", info);

                json.put("data", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();

            } catch (Exception e) {
                Log.d("Error", "" + e);
            }


            return null;
        }
    }


    private class TimeTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... param) {
            long nowAsPerDeviceTimeZone = 0;
            SntpClient sntpClient = new SntpClient();
            if (sntpClient.requestTime("0.africa.pool.ntp.org", 30000)) {
                nowAsPerDeviceTimeZone = sntpClient.getNtpTime();
            }
            internetDate = new Date(nowAsPerDeviceTimeZone);
            return null;
        }

        protected void onPostExecute(Void param) {

        }
    }

}

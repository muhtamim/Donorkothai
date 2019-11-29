package com.afinal.sdnath.sdnath;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DonarNotificationAdapter extends Adapter<DonarNotificationAdapter.myViewHolder> {

    Context context;
    List<Donor> donor;
    String blood;
    FirebaseAuth auth;
    String myUid;
    String uid;
    FirebaseFirestore myFirestore;
    Date internetDate;
    ProgressDialog dialog;
    String requestName, requestPhone, requestAddress, requestQuantity, requestHospital;
    String myname,myphone,myaddress;
    String tkn;
    String time;
    String myNotificationTime;



    public DonarNotificationAdapter(Context c, List<Donor> d, String myblood) {
        context = c;
        donor = d;
        blood = myblood;

        myFirestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        myUid=auth.getUid();
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.donar_notification_row, viewGroup, false);
        return new myViewHolder(view);
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {

        requestName = donor.get(i).getName();
        requestPhone = donor.get(i).getPhone();
        requestAddress = donor.get(i).getAddress();
        uid = donor.get(i).getUid();
        requestQuantity = donor.get(i).getBag();
        requestHospital = donor.get(i).getHospital();
/***
        myFirestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        myUid=auth.getUid();
*/
        String timeInMill=donor.get(i).getTime();
        myNotificationTime=DateFormat.format("dd/MM/yyyy hh:mm:ss", Long.parseLong(timeInMill)).toString();

        myViewHolder.name.setText("NAME: " + requestName);
        myViewHolder.phone.setText("PHONE: " + requestPhone);
        myViewHolder.address.setText("ADDRESS: " + requestAddress);
        myViewHolder.bag.setText("QUANTITY: " + requestQuantity);
        myViewHolder.hospital.setText("HOSPITAL: " + requestHospital);
        myViewHolder.textUid.setText(uid);
        myViewHolder.notificationTime.setText(myNotificationTime);


        if ("A+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.ap);
        }
        if ("A-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.an);
        }
        if ("B+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.bp);
        }
        if ("B-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.bn);
        }
        if ("AB+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.abp);
        }
        if ("AB-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.abn);
        }
        if ("O+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.op);
        }
        if ("O-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.on);
        }

        dialog = new ProgressDialog(context);


        myViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Accepting Request...");
                dialog.show();
                //new TimeTask().execute((Void[]) null);

                final String uidText=myViewHolder.textUid.getText().toString();
                if (isNetworkAvailable()) {


                    myFirestore.collection("users").document(myUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot!=null) {
                                myname= documentSnapshot.getString("name");
                                myphone=documentSnapshot.getString("phone");

                                String district=documentSnapshot.getString("district");

                                if ("empty".equals(documentSnapshot.getString("thana"))) {
                                    myaddress = district;
                                } else {
                                    String thana = documentSnapshot.getString("thana");
                                    myaddress = thana + " , " + district;
                                }



                                Map<String, String> setvalue = new HashMap<>();
                                setvalue.put("name", myname);
                                setvalue.put("phone", myphone);
                                setvalue.put("address", myaddress);
                                setvalue.put("blood", blood);
                                //setvalue.put("time", internetDate.toString());
                                setvalue.put("time", String.valueOf(System.currentTimeMillis()));
                                myFirestore.collection("notification").document("reciever").collection(uidText).document().set(setvalue);


                                myFirestore.collection("users").document(uidText).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        if (documentSnapshot != null) {
                                            tkn = documentSnapshot.getString("token");

                                            new Notify().execute(tkn, myname + " " + "has accepted your request", myphone + "," + " " + myaddress);
                                        }

                                    }
                                });


                                //waiting 2 sec
                                new CountDownTimer(2000, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        dialog.dismiss();
                                        Toast.makeText(context, "Your contact information send succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                }.start();

                            }
                        }
                    });


                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "Please check network availability and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return donor.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, phone, address, hospital, bag;
        Button accept;
        TextView notificationTime;
        TextView textUid;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            hospital = itemView.findViewById(R.id.hospital);
            accept = itemView.findViewById(R.id.accept);
            bag = itemView.findViewById(R.id.bag);
            textUid =itemView.findViewById(R.id.textuid);
            notificationTime=itemView.findViewById(R.id.notificationTime);


        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Notification.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class Notify extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            //String tkn = "ck_rNJuzN3M:APA91bGUBcgl5V3S26e_BwaCYgiXFT20YvrOre3r-BBQjT3GXJZlApTtg-O2dskSmvcLjQOR7bx5mLIbDuWxq-Wtnwx5TxAnA0Bfs5Q02D1bLQLay-OD43c-e9bbxAgIA4vIEsS-PN4C";
            String tkn = params[0];
            String title = params[1];
            String body = params[2];
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
                info.put("type","reciever");

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

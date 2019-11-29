package com.afinal.sdnath.sdnath;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class BloodDonarFragment extends Fragment {


    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    String uid;
    DonarNotificationAdapter donarNotificationAdapter;
    //ArrayList<Donor> donorlis;

    String myname, mygender, myblood, mylastDonate, myage, myweight, myphone, mydistrict,mythana;

    public BloodDonarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_blood_donar, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        uid=auth.getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    mythana=documentSnapshot.getString("thana");
                    mydistrict=documentSnapshot.getString("district");
                    myblood=documentSnapshot.getString("blood");



                    Query query = firebaseFirestore.collection("notification").document("donor").collection(mydistrict).document(mythana).collection(myblood).orderBy("time",Query.Direction.DESCENDING).limit(3);

                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }


                            List<Donor> donor = queryDocumentSnapshots.toObjects(Donor.class);


                            donarNotificationAdapter = new DonarNotificationAdapter(getActivity(), donor, myblood);
                            recyclerView.setAdapter(donarNotificationAdapter);
                        }
                    });
                }
            }
        });

        return view;
    }



}

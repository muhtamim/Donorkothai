package com.afinal.sdnath.sdnath;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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
public class BloodRecieverFragment extends Fragment {


    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    String uid;
    RecieverNotificationAdapter recieverNotificationAdapter;


    public BloodRecieverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blood_reciever, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));






        return view;
    }

    public void loadNotification() {

        Query query = firebaseFirestore.collection("notification").document("reciever")
                .collection(uid).orderBy("time", Query.Direction.DESCENDING).limit(3);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }


                List<Reciever> reciever = queryDocumentSnapshots.toObjects(Reciever.class);

                recieverNotificationAdapter = new RecieverNotificationAdapter(getActivity(), reciever);
                recyclerView.setAdapter(recieverNotificationAdapter);
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        loadNotification();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotification();
    }


}

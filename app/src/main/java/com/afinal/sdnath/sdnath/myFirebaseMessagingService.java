package com.afinal.sdnath.sdnath;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class myFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseFirestore myFirestore;
    FirebaseAuth auth;
    String mydistrict, mythana, myblood, uid;
    Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + "donorkothai@contact.com" + "/" + R.raw.blood_notification_sound);
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(final String token) {

        myFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();

        if (uid != null) {
            myFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        mydistrict = documentSnapshot.getString("district");
                        mythana = documentSnapshot.getString("thana");
                        myblood = documentSnapshot.getString("blood");

                        Map<String, Object> updatevalue = new HashMap<>();
                        updatevalue.put("token", token);
                        //myFirestore.collection("users").document(uid).update(updatevalue);
                        myFirestore.collection("blood").document("bank").collection(mydistrict).document(mythana).collection(myblood)
                                .document(uid).update(updatevalue);
                    }
                }
            });

        }

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().isEmpty()) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }else {
            showNotification(remoteMessage.getData());
        }




    }

    private void showNotification(Map<String, String> data) {


        String title = data.get("title");
        String message = data.get("body");
        String type=data.get("type");

        Intent intent = new Intent(this, Notification.class);

        if ("reciever".equals(type)){
            intent.putExtra("type","reciever");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BD")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.blood_drop)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.logo));
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(0, builder.build());
        }

        else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BD")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.blood_drop)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.logo));
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(0, builder.build());
        }



    }


    public void showNotification(String title, String message) {

        Intent intent = new Intent(this, Notification.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BD")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.blood_drop)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.logo));
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0, builder.build());
    }

}




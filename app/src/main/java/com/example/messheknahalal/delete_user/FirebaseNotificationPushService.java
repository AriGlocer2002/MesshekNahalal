package com.example.messheknahalal.delete_user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.messheknahalal.R;
import com.example.messheknahalal.loginScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseNotificationPushService extends FirebaseMessagingService {

    public static final int DELETE_PERSON_NOTIFICATION_ID = 666;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            return;
        }

        if(remoteMessage.getNotification() == null){
            return;
        }

        String email = firebaseUser.getEmail();

        if (remoteMessage.getData().containsKey("email") && !Objects.equals(remoteMessage.getData().get("email"), email)){
            return;
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        String id = "MESSAGE";

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "Delete User Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, loginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.human_skull)
                .setColorized(true)
                .setColor(Color.BLACK)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(DELETE_PERSON_NOTIFICATION_ID, notification.build());
        deleteCurrentFirebaseUser();

        super.onMessageReceived(remoteMessage);
    }

    public void deleteCurrentFirebaseUser() {
        firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ariel", "the user was successfully deleted");

                        Intent intent = new Intent(FirebaseNotificationPushService.this, loginScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        FirebaseMessaging.getInstance().subscribeToTopic(FCMSend.MESSHEK_NAHALAL_TOPIC).addOnCompleteListener(
                task -> Log.d("ariel", ""));

    }
}
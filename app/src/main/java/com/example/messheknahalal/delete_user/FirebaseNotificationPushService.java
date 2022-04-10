package com.example.messheknahalal.delete_user;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.loginScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

public class FirebaseNotificationPushService extends FirebaseMessagingService {

    public static final int DELETE_PERSON_NOTIFICATION_ID = 666;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("NewApi")
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

        if (remoteMessage.getData().containsKey("email") && !remoteMessage.getData().get("email").equals(email)){
            return;
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();


        String CHANNEL_ID = "MESSAGE";

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Message_Notification",
                NotificationManager.IMPORTANCE_HIGH);

        Intent intentToLoginScreen = new Intent(this, loginScreen.class);
        intentToLoginScreen.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pintentToLoginScreen = PendingIntent.getActivity(this, 0, intentToLoginScreen, PendingIntent.FLAG_IMMUTABLE);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.human_skull)
                .setColorized(true)
                .setColor(Color.BLACK)
                .setAutoCancel(true)
                .setContentIntent(pintentToLoginScreen);

        NotificationManagerCompat.from(this).notify(DELETE_PERSON_NOTIFICATION_ID, notification.build());


        String token = remoteMessage.getData().get("token");

        deleteCurrentFirebaseUser(email, token);

        super.onMessageReceived(remoteMessage);
    }

    public void deleteCurrentFirebaseUser(String email, String token) {

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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

        String email = firebaseUser.getEmail();

        if (email != null) {
            String path = Utils.emailToUserPath(email);
            FirebaseDatabase.getInstance().getReference("User").child(path).child("token").setValue(token);
        }

    }
}
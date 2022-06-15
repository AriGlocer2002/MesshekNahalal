package com.example.messheknahalal.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.messheknahalal.R;
import com.example.messheknahalal.User_screens.OrdersRecyclerViewScreenUser;
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
    public static final int ORDER_STATUS_UPDATED_NOTIFICATION_ID = 123456;
    public static final int PICKING_DATE_UPDATED_NOTIFICATION_ID = 645321;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            //user is not logged in
            return;
        }

        if(remoteMessage.getNotification() == null){
            //remoteMessage is not a notification
            return;
        }

        String email = firebaseUser.getEmail();

        if (remoteMessage.getData().containsKey("from") && Objects.equals(remoteMessage.getData().get("from"), email)){
            //main admin send the notification for everyone but this is his account
            return;
        }

        if (remoteMessage.getData().containsKey("message")){
            //main admin send the notification for everyone
            pushNotificationToAll(remoteMessage);
            return;
        }

        if (remoteMessage.getData().containsKey("email") && !Objects.equals(remoteMessage.getData().get("email"), email)){
            //admin send deleting notification or update order status notification
            //the email of target user and email of this user are equal
            //therefore this is the user that has to be deleted or to get notification about his order
            return;
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        String id = "MESSAGE";

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "Messhek Nahalal Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        boolean isDeleteUserNotification = !remoteMessage.getData().containsKey("orderNumber");

        Intent intent = new Intent(this, isDeleteUserNotification ? loginScreen.class : OrdersRecyclerViewScreenUser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, id)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(isDeleteUserNotification ? R.drawable.human_skull : R.drawable.ic_shopping_cart)
                .setColorized(true)
                .setColor(isDeleteUserNotification ? Color.BLACK : Color.GREEN)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (isDeleteUserNotification){
            notificationManager.notify(DELETE_PERSON_NOTIFICATION_ID, notification.build());
            deleteCurrentFirebaseUser();
        }
        else {
            notificationManager.notify(ORDER_STATUS_UPDATED_NOTIFICATION_ID, notification.build());
        }

        super.onMessageReceived(remoteMessage);
    }

    private void pushNotificationToAll(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        String id = "MESSAGE";

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "Messhek Nahalal Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, id)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(R.drawable.ic_baseline_calendar_month_24)
                .setColorized(true)
                .setColor(Color.BLACK)
                .setAutoCancel(true);

        notificationManager.notify(PICKING_DATE_UPDATED_NOTIFICATION_ID, notification.build());
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
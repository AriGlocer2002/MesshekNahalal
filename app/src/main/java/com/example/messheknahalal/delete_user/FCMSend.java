package com.example.messheknahalal.delete_user;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.messheknahalal.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {

    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    //API key for FCM
    private static final String SERVER_KEY = "key=AAAAMGJwnio:APA91bHmsrZxwvWPa4fHLO2tNLr5HuwtLRLfYZYsFuByqTroeAze8VIqJ8YyCo3WIKtSPqlwFCsbvckAtzsJQA3eHn4Tpy5r17O1ZrGdsk7s1KCI3OVJPp9BziVP4OJDk0oWeCiZcF8I";

    public static void sendNotificationToOnePerson(@NonNull Context context, String email) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String title = "";
        String body = "";


        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JSONObject json = new JSONObject();
            json.put("to", "/topics/" + "Notification_to_" + Utils.emailForFCM(email));
            JSONObject notification = new JSONObject();

            title = "User account deleted";

            body = "Your account was deleted by Admin";

            notification.put("title", title);
            notification.put("body", body);

            json.put("notification", notification);

            JSONObject data = new JSONObject();
            data.put("email", email);

            json.put("data", data);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("ariel", "FCM " + response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            error.getCause();
                        }
                    })
            {
                @NonNull
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationsToDeletePerson(Context context, String email) {
        sendNotificationToOnePerson(context, email);
    }
}

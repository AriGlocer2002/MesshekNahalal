package com.example.messheknahalal.notifications;

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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {

    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    //API key for FCM
    private static final String SERVER_KEY = "key=AAAAMGJwnio:APA91bHmsrZxwvWPa4fHLO2tNLr5HuwtLRLfYZYsFuByqTroeAze8VIqJ8YyCo3WIKtSPqlwFCsbvckAtzsJQA3eHn4Tpy5r17O1ZrGdsk7s1KCI3OVJPp9BziVP4OJDk0oWeCiZcF8I";

    public static final String MESSHEK_NAHALAL_TOPIC = "messhek_nahalal_topic";

    public static void sendNotificationToDeletePerson(Context context, String email) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        
        try {
            JSONObject json = new JSONObject();
            json.put("to", "/topics/" + MESSHEK_NAHALAL_TOPIC);
            JSONObject notification = new JSONObject();

            String title = "User account deleted";

            String body = "Your account was deleted by Admin";

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

    public static void sendNotificationToPerson(Context context, String email, long orderNumber) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject json = new JSONObject();
            json.put("to", "/topics/" + MESSHEK_NAHALAL_TOPIC);
            JSONObject notification = new JSONObject();

            String title = "Order№:" + orderNumber + " status updated";

            String body = "Your order's status was updated by Admin";

            notification.put("title", title);
            notification.put("body", body);

            json.put("notification", notification);

            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("orderNumber", orderNumber);

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

    public static void sendNotificationToAllUsers(Context context, String message) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        try {
            JSONObject json = new JSONObject();
            json.put("to", "/topics/" + MESSHEK_NAHALAL_TOPIC);
            JSONObject notification = new JSONObject();

            String title = "Updated pick up orders date";

            String body = "The main Admin has set a new date for picking up the orders.\n" +
                    "Now it is " + message;

            notification.put("title", title);
            notification.put("body", body);

            json.put("notification", notification);

            JSONObject data = new JSONObject();
            data.put("message", message);
            data.put("from", email);

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

}

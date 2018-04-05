package com.taxilive_driver.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.taxilive_driver.Activity.MainActivity;
import com.taxilive_driver.R;
import com.taxilive_driver.util.SavePref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SavePref savePref;
    private static int i;
    String request_id = "", start_time_from = "", estimation_cost = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        savePref = new SavePref(getApplicationContext());
        Log.d(TAG, "From: " + remoteMessage.getData());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData());
        String notification_code = remoteMessage.getData().get("notification_code");

        // if customer submit request and request received by employee
        if (notification_code.equals("1")) {
            String message = remoteMessage.getData().get("message");

            try {
                JSONObject obj = new JSONObject(remoteMessage.getData().get("body"));
                request_id = obj.getString("request_id");
                estimation_cost = obj.getString("estimation_cost");
                start_time_from = obj.getString("start_time_from");
//
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (savePref.get_presentactivity().equals("")) {
                sendNotification(getApplicationContext(), message, request_id, estimation_cost, start_time_from);
            } else if (savePref.get_presentactivity().equals("MainActivity")) {
                sendMessage(start_time_from, estimation_cost, request_id, "1");
            }


//            sendNotification(getApplicationContext(), message, request_id, estimation_cost, start_time_from);
        }
        if (notification_code.equals("0")) {

            String message = remoteMessage.getData().get("message");
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData().get("body"));
                request_id = obj.getString("request_id");
                estimation_cost = obj.getString("estimation_cost");
                start_time_from = obj.getString("start_time_from");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (savePref.get_presentactivity().equals("")) {
                sendNotification(getApplicationContext(), message, request_id, estimation_cost, start_time_from);
            } else if (savePref.get_presentactivity().equals("MainActivity")) {
                sendMessage(start_time_from, estimation_cost, request_id, "0");
            }


        } else if (notification_code.equals("118")) {
            String message = remoteMessage.getData().get("message");
            if (savePref.get_presentactivity().equals("")) {
                sendNotification(getApplicationContext(), message, request_id, estimation_cost, start_time_from);
            } else if (savePref.get_presentactivity().equals("MainActivity")) {
                sendMessage(start_time_from, estimation_cost, request_id, "118");
            }
        } else if (notification_code.equals("111")) {
            final String message = remoteMessage.getData().get("message");

            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            };
            mainHandler.post(myRunnable);
            if (savePref.get_presentactivity().equals("")) {
                simpleNotification(getApplicationContext(), message);
            } else if (savePref.get_presentactivity().equals("MainActivity")) {
                simpleNotification(getApplicationContext(), message);
                sendMessage("", "", "", "111");
            }
        } else {
            String message = remoteMessage.getData().get("message");
            simpleNotification(getApplicationContext(), message);
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void sendNotification(Context context, String message, String request_id, String estimation_cost, String start_time_from) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("request_id", request_id);
        intent.putExtra("estimation_cost", estimation_cost);
        intent.putExtra("start_time_from", start_time_from);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = null;

        notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(icon1)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true).setColor(context.getResources().getColor(R.color.colorAccent))
                .setSound(defaultSoundUri).
                        setOngoing(false)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i++, notificationBuilder);
    }

    private void sendMessage(String start_time_from, String estimation_cost, String request_id, String notification_code) {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("request_id", request_id);
        intent.putExtra("estimation_cost", estimation_cost);
        intent.putExtra("start_time_from", start_time_from);
        intent.putExtra("notification_code", notification_code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void cancel_ride(String notification_code) {
        Intent intent = new Intent("cancel_ride");
        intent.putExtra("notification_code", notification_code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void simpleNotification(Context context, String message) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = null;

        notificationBuilder = new  Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(icon1)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setSound(defaultSoundUri).
                        setOngoing(false)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i++, notificationBuilder);
    }


}

package com.pwszit.singiel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.app.NotificationCompat;
//import net.simplifiedcoding.simplifiedcodingchat.R;
//import net.simplifiedcoding.simplifiedcodingchat.helper.Constants;
//import net.simplifiedcoding.simplifiedcodingchat.helper.Message;
//import net.simplifiedcoding.simplifiedcodingchat.helper.NotificationHandler;


//public class GCMPushReceiverService extends GCMTokenRefreshListenerService
   public class GCMPushReceiverService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
        }

        //getting the title and the body
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String uri = remoteMessage.getData().get("url");
        String id = remoteMessage.getData().get("id");

        //then here we can use the title and body to build a notification


        //Adding notification data to the intent
        sendNotification(body, title, id, uri);

    }

    private void sendNotification(String body, String title, String id, String uri) {
        //Creating a broadcast intent
        Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
        //Adding notification data to the intent
        pushNotification.putExtra("body", body);
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("id", id);

        //We will create this class to handle notifications

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chatroom to add the new message
            if (uri != null && !uri.equals(Uri.EMPTY)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            } else {
                Intent intent = new Intent(this, ChatMessagingActivity.class);
                intent.putExtra("name", title);
                intent.putExtra("id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            }
        } else {
            if (uri != null && !uri.equals(Uri.EMPTY)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            } else {
                Intent intent = new Intent(this, ChatMessagingActivity.class);
                intent.putExtra("name", title);
                intent.putExtra("id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            }
            //If app is in foreground displaying push notification

        }
    }
}
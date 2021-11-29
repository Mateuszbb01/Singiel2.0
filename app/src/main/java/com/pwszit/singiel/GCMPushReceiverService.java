package com.pwszit.singiel;

import android.content.Intent;
import android.content.SharedPreferences;

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
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        //then here we can use the title and body to build a notification

        sharedPreferences = getSharedPreferences("notification_text", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", title);
        editor.putString("body", body);
        editor.putString("id", "saa");
        editor.apply();
        String id = "3";
        sendNotification(body, title, id);


    }

    private void sendNotification(String body, String title, String id) {
        //Creating a broadcast intent
        Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
        //Adding notification data to the intent
        pushNotification.putExtra("body", body);
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("id", id);

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chatroom to add the new message
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            notificationHandler.showNotificationMessage(title, body);
        }
    }
}
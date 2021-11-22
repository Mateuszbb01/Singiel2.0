package com.pwszit.singiel;
import android.content.Intent;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;

//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.app.NotificationCompat;
//import net.simplifiedcoding.simplifiedcodingchat.R;
//import net.simplifiedcoding.simplifiedcodingchat.helper.Constants;
//import net.simplifiedcoding.simplifiedcodingchat.helper.Message;
//import net.simplifiedcoding.simplifiedcodingchat.helper.NotificationHandler;


//public class GCMPushReceiverService extends GCMTokenRefreshListenerService
   public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String id = data.getString("id");
        sendNotification(message, title, id);
    }

    private void sendNotification(String message, String title, String id) {
        //Creating a broadcast intent
        Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
        //Adding notification data to the intent
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("name", title);
        pushNotification.putExtra("id", id);

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chatroom to add the new message
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            notificationHandler.showNotificationMessage(title, message);
        }
    }
}
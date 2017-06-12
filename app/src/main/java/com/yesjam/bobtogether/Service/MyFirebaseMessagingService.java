package com.yesjam.bobtogether.Service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.yesjam.bobtogether.Etc;
import com.yesjam.bobtogether.MainActivity;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.yesjam.bobtogether.R;
import com.yesjam.bobtogether.SQLite.ChatDBHelper;

import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private ChatDBHelper chatDBHelper;
//    private ChatRoomListDBHelper chatRoomListDBHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        chatDBHelper = new ChatDBHelper(MyFirebaseMessagingService.this);
//        chatRoomListDBHelper = new ChatRoomListDBHelper(MyFirebaseMessagingService.this);

        chatDBHelper.setChat(remoteMessage.getData().get("roomUid")
                , remoteMessage.getData().get("message")
                , remoteMessage.getData().get("fromName")
                , remoteMessage.getData().get("date")
                , 1
                , remoteMessage.getData().get("email"));

//        if (chatRoomListDBHelper.hasRoomUid(remoteMessage.getData().get("roomUid"))) {
//            chatRoomListDBHelper.deleteRoom(remoteMessage.getData().get("roomUid"));
//        }

//        chatRoomListDBHelper.setChatRoom(remoteMessage.getData().get("roomName")
//                , remoteMessage.getData().get("roomUid"));

        chatDBHelper.close();
//        chatRoomListDBHelper.close();

        LocalBroadcastManager
                .getInstance(MyFirebaseMessagingService.this)
                .sendBroadcast(new Intent("socialana.com.eom.socialana.notify_second"));

        if (!Etc.running||Etc.running) {
            Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, Etc.PUSH_NOTIFICATION_REQUEST_CODE, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_person_black_48dp)
                    .setContentTitle(remoteMessage.getData().get("fromName"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

}
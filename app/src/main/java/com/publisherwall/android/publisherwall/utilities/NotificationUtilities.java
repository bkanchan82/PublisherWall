package com.publisherwall.android.publisherwall.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.publisherwall.android.publisherwall.EventDetailActivity;
import com.publisherwall.android.publisherwall.NetworkDetailActivity;
import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.EventEntry;

import java.util.Map;

public class NotificationUtilities {

    public static final int EVENT_REMINDER_NOTIFICATION_ID = 111;
    public static final String NEW_EVENT_NOTIFICATION_TITLE = "New Event";
    public static final String EVENT_REMINDER_NOTIFICATION_TITLE = "Event Reminder";

    public static final int NEW_EVENT_NOTIFICATION_ID = 112;
    public static final int NEW_EVENT_PENDING_INTENT_ID = 1212;

    public static final int NEW_NETWORK_NOTIFICATION_ID = 113;
    public static final int NEW_NETWORK_PENDING_INTENT_ID = 1213;
    public static final String NEW_NETWORK_NOTIFICATION_TITLE = "New Network";

    public static void processMessage(Map<String, String> data, Context context) {
        Intent intent = new Intent(context, EventDetailActivity.class);

        String eid = data.get("eid");
        String title = data.get("event_title");
        String url = data.get("url");
        String event_date = data.get("event_date");
        String location = data.get("location");
        String event_image = data.get("event_img");
        String is_fav = data.get("is_fav");
        String detail = data.get("detail");
        String fav_count = data.get("fav_count");

        EventEntry eventEntry = new EventEntry(eid,
                title,url,event_date,location,detail,event_image,is_fav,fav_count);

        EventData eventData = new EventData(eventEntry);

        intent.putExtra(EventDetailActivity.EXTRA_EVENT_DATA, eventData);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder
                .getPendingIntent(NEW_EVENT_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT);

        if (data.get("notification").equals("1")) {

            AppExecutor.getAppExecutorInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getAppDatabaseInstance(context.getApplicationContext()).eventDao().insertEntry(eventEntry);
                }
            });

            String notificationContent = context.getString(R.string.event_notification_content_format, eventData.getTitle(), eventData.getDate(), eventData.getLocation());
            createRequiredNotification(context,
                    NEW_EVENT_NOTIFICATION_ID,
                    NEW_EVENT_NOTIFICATION_TITLE,
                    notificationContent,
                    pendingIntent,
                    context.getString(R.string.event_notification_channel_id),
                    context.getString(R.string.event_notification_channel_title),
                    context.getString(R.string.event_notification_channel_description));

        } else if (data.get("notification").equals("3")) {
            String notificationContent = context.getString(R.string.event_notification_content_format, eventData.getTitle(), eventData.getDate(), eventData.getLocation());
            createRequiredNotification(context,
                    NEW_EVENT_NOTIFICATION_ID,
                    EVENT_REMINDER_NOTIFICATION_TITLE,
                    notificationContent,
                    pendingIntent,
                    context.getString(R.string.event_notification_channel_id),
                    context.getString(R.string.event_reminder_notification_title),
                    context.getString(R.string.event_notification_channel_description));

        } else if (data.get("notification").equals("2")) {
            Intent networkIntent = new Intent(context, NetworkDetailActivity.class);
            networkIntent.putExtra(Intent.EXTRA_TEXT, data.get(""));
            networkIntent.putExtra(NetworkDetailActivity.EXTRA_SHARE_URL, data.get(""));

            TaskStackBuilder networkStackBuilder = TaskStackBuilder.create(context);
            networkStackBuilder.addNextIntentWithParentStack(networkIntent);
            PendingIntent networkPendingIntent = networkStackBuilder
                    .getPendingIntent(NEW_NETWORK_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT);
//            PendingIntent networkPendingIntent = PendingIntent.getActivity(context,0,networkIntent,0);
            String notificationContent = data.get("network_name");
            createRequiredNotification(context,
                    NEW_NETWORK_NOTIFICATION_ID,
                    NEW_NETWORK_NOTIFICATION_TITLE,
                    notificationContent,
                    networkPendingIntent,
                    context.getString(R.string.network_notification_channel_id),
                    context.getString(R.string.network_notification_channel_name),
                    context.getString(R.string.network_notification_channel_description));

        }


    }

    public static void createRequiredNotification(Context context,
                                                  int notificationId,
                                                  String notificationTitle,
                                                  String notificationContent,
                                                  PendingIntent pendingIntent,
                                                  String notificationChannelId,
                                                  String notificationChannelName,
                                                  String notificationChannelDescription) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(notificationChannelId, notificationChannelName, importance);
            channel.setDescription(notificationChannelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, notificationChannelId)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largIcon(context))
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContent))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, mBuilder.build());

    }


    private static Bitmap largIcon(Context context) {
        Resources resources = context.getResources();

        Bitmap largIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        return largIcon;
    }

}

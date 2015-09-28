package com.wbread.linuxvoice.service;

import android.animation.AnimatorSet;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.wbread.linuxvoice.MainActivity;
import com.wbread.linuxvoice.R;
import com.wbread.linuxvoice.data.FeedItem;
import com.wbread.linuxvoice.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsUpdateListener extends BroadcastReceiver {

    SharedPreferences sPref;
    Context gCont;

    @Override
    public void onReceive(Context context, Intent intent){
        sPref = PreferenceManager.getDefaultSharedPreferences(context);

        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
        {
            Intent serviceLauncher = new Intent(context, LinuxVoiceService.class);
            context.startService(serviceLauncher);
        }


        gCont = context;

        boolean isConnected = AppUtils.isNetworkConnected(context);
        if(isConnected){// ONLINE
            GetLastRss();
        }else{ // OFFLINE
            //TODO: what if offline?
        }

    }

    void GetLastRss(){
        new Thread(new Runnable() {
            public void run() {
                List<FeedItem> feedItems = new ArrayList<FeedItem>();
                AppUtils.ReadRss(feedItems);

                if(feedItems.size()>0){
                    String prevUpdate = AppUtils.loadLastNewsfeedUpdate(sPref);
                    String curUpdate = feedItems.get(0).getPubDate();

                    if(!prevUpdate.equals(curUpdate)){
                        MakeNewsfeedNotification(gCont);
                        AppUtils.saveLastNewsfeedUpdate(curUpdate, sPref);
                    }
                }
            }
        }).start();



    }

    public void MakeNewsfeedNotification (Context context) {

        Resources res = context.getResources();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notBuilder = new Notification.Builder(context)
                .setContentTitle("LinuxVoice update")
                .setContentText("News at LinuxVoice")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_drawer);

        Notification noti;
        int notificationID = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.JELLY_BEAN) {
            // call something for API Level 16+
            noti = notBuilder.build();
            notificationManager.notify(notificationID, noti);
        } else if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
            // call something for API Level 11+
            noti = notBuilder.getNotification();
            notificationManager.notify(notificationID, noti);
        }

    }


    public void SetNewsUpdateListener(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NewsUpdateListener.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60, pi); // Millisec * Second * Minute
    }

    public void CancelNewsUpdateListener(Context context)
    {
        Intent intent = new Intent(context, NewsUpdateListener.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}

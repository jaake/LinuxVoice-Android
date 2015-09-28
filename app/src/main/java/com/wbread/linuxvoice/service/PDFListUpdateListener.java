package com.wbread.linuxvoice.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.wbread.linuxvoice.MainActivity;
import com.wbread.linuxvoice.R;
import com.wbread.linuxvoice.data.FeedItem;
import com.wbread.linuxvoice.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gleb on 18.09.2015.
 */
public class PDFListUpdateListener extends BroadcastReceiver {

    SharedPreferences sPref;
    Context gCont;

    @Override
    public void onReceive(Context context, Intent intent){
        sPref = PreferenceManager.getDefaultSharedPreferences(context);

        gCont = context;

        boolean isConnected = AppUtils.isNetworkConnected(context);
        if(isConnected){// ONLINE
            GetLastPDFlist();
        }else{ // OFFLINE
            //TODO: what if offline?
        }

    }

    void GetLastPDFlist(){
        new Thread(new Runnable() {
            public void run() {
                List<FeedItem> feedItems = new ArrayList<FeedItem>();
                AppUtils.ReadPDFlist(feedItems);

                if(feedItems.size()>0){
                    String prevUpdate = AppUtils.loadLastPDFListUpdate(sPref);
                    String curUpdate = feedItems.get(0).getPubDate();

                    if(!prevUpdate.equals(curUpdate)){
                        MakeNewPDFListNotification(gCont);
                        AppUtils.saveLastPDFListUpdate(curUpdate, sPref);
                    }
                }
            }
        }).start();



    }

    public void MakeNewPDFListNotification (Context context) {

        Resources res = context.getResources();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notBuilder = new Notification.Builder(context)
                .setContentTitle("LinuxVoice update")
                .setContentText("New Magazine at LinuxVoice")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_drawer);

        Notification noti;
        int notificationID = 2;
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


    public void SetPDFListUpdateListener(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PDFListUpdateListener.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60, pi); // Millisec * Second * Minute
    }

    public void CancelPDFListUpdateListener(Context context)
    {
        Intent intent = new Intent(context, PDFListUpdateListener.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


}

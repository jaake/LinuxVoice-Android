package com.wbread.linuxvoice.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;


public class LinuxVoiceService extends Service {
    NewsUpdateListener rss_update_Listener = new NewsUpdateListener();
    PodcastUpdateListener podcast_update_Listener = new PodcastUpdateListener();
    PDFListUpdateListener pdflist_update_Listener = new PDFListUpdateListener();

    SharedPreferences sPref;
    private final IBinder mBinder = new LinuxVoiceBinder();

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onCreate() {
        super.onCreate();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onDestroy() {
        rss_update_Listener.CancelNewsUpdateListener(this);
        podcast_update_Listener.CancelPodcastUpdateListener(this);
        pdflist_update_Listener.CancelPDFListUpdateListener(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        setPeriodicalServices();
        return super.onStartCommand(intent, flags, startId);
    }


    void setPeriodicalServices() {
        setRSSListener();
        setPodcastListener();
        setPDFListListener();
    }

    public void setRSSListener() {
        boolean getrss = sPref.getBoolean("get_rss_notification", true);
        if (getrss) {
            rss_update_Listener.SetNewsUpdateListener(this);
        }else{
            rss_update_Listener.CancelNewsUpdateListener(this);
        }
    }

    public void setPodcastListener() {
        boolean getpodcast = sPref.getBoolean("get_podcast_notification", true);
        if (getpodcast) {
            podcast_update_Listener.SetPodcastUpdateListener(this);
        }else{
            podcast_update_Listener.CancelPodcastUpdateListener(this);
        }
    }

    public void setPDFListListener() {
        boolean getpdflist = sPref.getBoolean("get_magazine_notification", true);
        if (getpdflist) {
            pdflist_update_Listener.SetPDFListUpdateListener(this);
        }else{
            pdflist_update_Listener.CancelPDFListUpdateListener(this);
        }
    }

    public class LinuxVoiceBinder extends Binder {
        public LinuxVoiceService getService() {
            return LinuxVoiceService.this;
        }
    }
}


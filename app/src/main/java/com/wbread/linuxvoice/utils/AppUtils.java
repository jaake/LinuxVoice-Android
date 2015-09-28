package com.wbread.linuxvoice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.wbread.linuxvoice.data.FeedItem;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by Gleb on 11.09.2015.
 */
public class AppUtils {

    private static String URL_FEED = "http://www.linuxvoice.com/feed/";
    private static String URL_PODCAST_FEED = "http://www.linuxvoice.com/podcast_ogg.rss";
    private static String URL_PDF_LIST = "http://www.linuxvoice.com/feed/?cat=33";


    public static String SAVED_LastNewsfeedUpdate = "last_newsfeed_update";
    public static String SAVED_LastPodcastUpdate = "last_podcast_update";
    public static String SAVED_LastPDFListUpdate = "last_pdflist_update";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        else{
            return ni.isConnectedOrConnecting();
        }
    }

    public static void saveLastNewsfeedUpdate(String last_update, SharedPreferences sPref) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LastNewsfeedUpdate, last_update);
        ed.commit();
    }
    public static String loadLastNewsfeedUpdate(SharedPreferences sPref) {
        String savedText = sPref.getString(SAVED_LastNewsfeedUpdate, "");
        return savedText;
    }

    public static void saveLastPodcastUpdate(String last_update, SharedPreferences sPref) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LastPodcastUpdate, last_update);
        ed.commit();
    }
    public static String loadLastPodcastUpdate(SharedPreferences sPref) {
        String savedText = sPref.getString(SAVED_LastPodcastUpdate, "");
        return savedText;
    }

    public static void saveLastPDFListUpdate(String last_update, SharedPreferences sPref) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LastPDFListUpdate, last_update);
        ed.commit();
    }
    public static String loadLastPDFListUpdate(SharedPreferences sPref) {
        String savedText = sPref.getString(SAVED_LastPDFListUpdate, "");
        return savedText;
    }

    public static void ReadRss(List<FeedItem> feedItems){

//        List<FeedItem> feedItems = new ArrayList<FeedItem>();
        try {
            try {
                try {
                    URL url = new URL(URL_FEED);
                    RssFeed feed = RssReader.read(url);

                    if(feed!=null){
                        ArrayList<RssItem> rssItems = feed.getRssItems();
                        for(RssItem rssItem : rssItems) {
                            FeedItem item = new FeedItem();

                            item.setDescription(rssItem.getDescription());
                            item.setLink(rssItem.getLink());
                            item.setTitle(rssItem.getTitle());

                            String content = rssItem.getContent().replaceAll("href=\"/", "href=\"http://www.linuxvoice.com/");
                            item.setContent(content);

                            item.setPubDate(rssItem.getPubDate().toString());

                            Spanned S = Html.fromHtml(item.getDescription());

                            String icon = null;

                            try{
                                ImageSpan[] is = (ImageSpan[]) S.getSpans(0,0,Class.forName("android.text.style.ImageSpan"));
                                icon = is[0].getSource();
                                item.setIcon_url(icon);

                                String desc = S.toString().substring(1);
                                item.setDescription(desc);

                            }catch(ClassNotFoundException e){
                            }


                            feedItems.add(item);
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return feedItems;
    }

    public static void ReadPodcast(List<FeedItem> feedItems) {

        try {
            try {
                try {
                    URL url = new URL(URL_PODCAST_FEED);
                    RssFeed feed = RssReader.read(url);

                    if (feed != null) {
                        ArrayList<RssItem> rssItems = feed.getRssItems();
                        for (RssItem rssItem : rssItems) {
                            FeedItem item = new FeedItem();

                            item.setDescription(rssItem.getDescription());
                            item.setLink(rssItem.getLink());
                            item.setTitle(rssItem.getTitle());
                            item.setPubDate(rssItem.getPubDate().toString());
                            item.setEnclosure_url(rssItem.getEnclosure_url());
                            item.setIsPlaying(false);

                            feedItems.add(item);
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReadPDFlist(List<FeedItem> feedItems){

        try {
            try {
                try {
                    URL url = new URL(URL_PDF_LIST);
                    RssFeed feed = RssReader.read(url);

                    if(feed!=null){
                        ArrayList<RssItem> rssItems = feed.getRssItems();
                        for(RssItem rssItem : rssItems) {
                            FeedItem item = new FeedItem();

                            item.setDescription(rssItem.getDescription());
                            item.setLink(rssItem.getLink());
                            item.setTitle(rssItem.getTitle());

                            String content = rssItem.getContent().replaceAll("href=\"/", "href=\"http://www.linuxvoice.com/");
                            item.setContent(content);

                            item.setPubDate(rssItem.getPubDate().toString());

                            Spanned S = Html.fromHtml(item.getDescription());

                            String icon = null;

                            try{
                                ImageSpan[] is = (ImageSpan[]) S.getSpans(0,0,Class.forName("android.text.style.ImageSpan"));
                                icon = is[0].getSource();
                                item.setIcon_url(icon);

                                String desc = S.toString().substring(1);
                                item.setDescription(desc);

                            }catch(ClassNotFoundException e){
                            }



                            feedItems.add(item);
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}

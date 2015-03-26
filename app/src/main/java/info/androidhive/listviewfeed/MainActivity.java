package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;
import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);

		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

        new Thread(new Runnable() {
            public void run() {
                ReadRss();
            }
        }).start();


        // These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
//		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
//		getActionBar().setIcon(
//				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));

/*
		// We first check for cached request
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					URL_FEED, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}
*/
	}

    private void ReadRss(){

      try {
        try {
            try {
                URL url = new URL("http://www.linuxvoice.com/feed/");
                RssFeed feed = RssReader.read(url);

                if(feed!=null){
                    ArrayList<RssItem> rssItems = feed.getRssItems();
                    for(RssItem rssItem : rssItems) {
                        FeedItem item = new FeedItem();

                        item.setDescription(rssItem.getDescription());
                        item.setLink(rssItem.getLink());
                        item.setTitle(rssItem.getTitle());
                        item.setPubDate(rssItem.getPubDate().toString());

                        feedItems.add(item);
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.wbread.linuxvoice.adapter;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wbread.linuxvoice.FeedImageView;
import com.wbread.linuxvoice.MainActivity;
import com.wbread.linuxvoice.NewsArticleActivity;
import com.wbread.linuxvoice.R;
import com.wbread.linuxvoice.app.AppController;
import com.wbread.linuxvoice.data.FeedItem;

public class FeedListAdapter extends BaseAdapter {
    private static final int SHOW_PROGRESS = 123;

	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    MediaPlayer mediaPlayer;
    private int playbackPosition=0;

////////////////////////////////////////////

    static class ViewHolderItem {
        private Button b1,b2,b3,b4;
        private double startTime = 0;
        private double finalTime = 0;
        private Handler myHandler = new Handler();;
        private int forwardTime = 5000;
        private int backwardTime = 5000;
        private SeekBar seekbar;
        private TextView tx1,tx2,tx3;

        public static int oneTimeOnly = 0;
    }

////////////////////////////////////////////



    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null){
            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.feed_item, null);

            viewHolder = new ViewHolderItem();
//            viewHolder.b1 = (Button) convertView.findViewById(R.id.button);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
        RelativeLayout llItem = (RelativeLayout) convertView.findViewById(R.id.llItem);

        final FeedItem item = feedItems.get(position);

		name.setText(item.getTitle());

		// Converting timestamp into x ago format
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				Long.parseLong(item.getTimeStamp()),
//				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(item.getPubDate());

		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getDescription())) {
			statusMsg.setText(item.getDescription());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
		if (item.getLink() != null) {
			url.setText(Html.fromHtml("<a href=\"" + item.getLink() + "\">"
					+ item.getLink() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			// url is null, remove from the view
			url.setVisibility(View.GONE);
		}
        url.setVisibility(View.GONE);


            // user profile pic
		profilePic.setImageUrl(item.getIcon_url(), imageLoader);

        if(item.getContent()!=null){
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                    intent.putExtra("ArticleContent", item.getContent());
                    intent.putExtra("ArticleHeader", item.getTitle());
                    activity.startActivity(intent);                    }
            });

        }

		return convertView;
	}

}

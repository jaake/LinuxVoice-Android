package com.wbread.linuxvoice.adapter;


import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.wbread.linuxvoice.MainActivity;
import com.wbread.linuxvoice.R;
import com.wbread.linuxvoice.app.AppController;
import com.wbread.linuxvoice.data.FeedItem;

public class PodcastListAdapter extends BaseAdapter {
    private static final int SHOW_PROGRESS = 123;

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    MediaPlayer mediaPlayer;
    private int playbackPosition=0;

////////////////////////////////////////////

    static class ViewHolderItem {
        private ImageView ivButtonRewind;
        private ImageView ivButtonPause;
        private ImageView ivButtonPlay;
        private ImageView ivButtonFastForward;
        private double startTime = 0;
        private double finalTime = 0;
        private Handler myHandler = new Handler();;
        private int forwardTime = 5000;
        private int backwardTime = 5000;
        private SeekBar seekbar;
        private TextView tvStartTime;
        private TextView tvFinalTime;
    }

////////////////////////////////////////////



    public PodcastListAdapter(Activity activity, List<FeedItem> feedItems) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolderItem viewHolder;

        if (convertView == null){
            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.podcast_item, null);

            viewHolder = new ViewHolderItem();
            viewHolder.ivButtonRewind = (ImageView) convertView.findViewById(R.id.ivButtonRewind);
            viewHolder.ivButtonPause = (ImageView) convertView.findViewById(R.id.ivButtonPause);
            viewHolder.ivButtonPlay = (ImageView) convertView.findViewById(R.id.ivButtonPlay);
            viewHolder.ivButtonFastForward = (ImageView) convertView.findViewById(R.id.ivButtonFastForward);

            viewHolder.tvStartTime=(TextView)convertView.findViewById(R.id.tvStartTime);
            viewHolder.tvFinalTime=(TextView)convertView.findViewById(R.id.tvFinalTime);

            viewHolder.seekbar =(SeekBar)convertView.findViewById(R.id.seekBar);

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
        final ImageView ivMainPlay = (ImageView) convertView.findViewById(R.id.ivMainPlay);
        RelativeLayout llItem = (RelativeLayout) convertView.findViewById(R.id.llItem);

        final FeedItem item = feedItems.get(position);

////////////////////////////////////////////
/*        if (item.getEnclosure_url() != null) {
            ChangeMediaControlsVisibility(View.VISIBLE, viewHolder);

            SetMedia(item.getEnclosure_url(), viewHolder);
        }else{
            ChangeMediaControlsVisibility(View.GONE, viewHolder);
        }
        */
        if(item.getIsPlaying())
            ChangeMediaControlsVisibility(View.VISIBLE, viewHolder);
        else
            ChangeMediaControlsVisibility(View.GONE, viewHolder);
////////////////////////////////////////////


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

        ivMainPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeMediaControlsVisibility(View.VISIBLE, viewHolder);

                SetMedia(item.getEnclosure_url(), viewHolder, position);

            }
        });

////////////////////////////////////////////

        // user profile pic
//        profilePic.setImageUrl(item.getIcon_url(), imageLoader);

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

    void ChangeMediaControlsVisibility(int Vis, ViewHolderItem viewHolder){
        viewHolder.ivButtonRewind.setVisibility(Vis);
        viewHolder.ivButtonPause.setVisibility(Vis);
        viewHolder.ivButtonPlay.setVisibility(Vis);
        viewHolder.ivButtonFastForward.setVisibility(Vis);

        viewHolder.tvStartTime.setVisibility(Vis);
        viewHolder.tvFinalTime.setVisibility(Vis);

        viewHolder.seekbar.setVisibility(Vis);
    }


    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
//        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
//                e.printStackTrace();
            }
        }
    }


    void SetMedia(final String url, final ViewHolderItem vhItem, final int listPos){


        vhItem.seekbar.setClickable(false);
//        vhItem.ivButtonPause.setEnabled(false);
        vhItem.startTime = 0;

        PushPlayButton(url, vhItem, listPos);

        vhItem.ivButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushPlayButton(url, vhItem, listPos);
            }
        });

        vhItem.ivButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
//                vhItem.ivButtonPause.setEnabled(false);
//                vhItem.ivButtonPlay.setEnabled(true);

            }
        });

        vhItem.ivButtonFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) vhItem.startTime;

                if ((temp + vhItem.forwardTime) <= vhItem.finalTime) {
                    vhItem.startTime = vhItem.startTime + vhItem.forwardTime;
                    mediaPlayer.seekTo((int) vhItem.startTime);
                    Toast.makeText(activity.getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vhItem.ivButtonRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) vhItem.startTime;

                if ((temp - vhItem.backwardTime) > 0) {
                    vhItem.startTime = vhItem.startTime - vhItem.backwardTime;
                    mediaPlayer.seekTo((int) vhItem.startTime);
                    Toast.makeText(activity.getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void PushPlayButton(String url, ViewHolderItem vhItem, int listPos){
        try{
            playAudio(url);
        }catch(Exception e){

        }

        Toast.makeText(activity.getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();

        vhItem.finalTime = mediaPlayer.getDuration();
        mediaPlayer.seekTo((int) vhItem.startTime);

        mediaPlayer.start();

        vhItem.seekbar.setMax((int) vhItem.finalTime);
        vhItem.tvFinalTime.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) vhItem.finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) vhItem.finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) vhItem.finalTime)))
        );

        vhItem.tvStartTime.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) vhItem.startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) vhItem.startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) vhItem.startTime)))
        );

        vhItem.seekbar.setProgress((int) vhItem.startTime);
        UpdateSongTime(vhItem);
//                vhItem.myHandler.postDelayed(UpdateSongTime(vhItem),100);

//        vhItem.ivButtonPause.setEnabled(true);
//        vhItem.ivButtonPlay.setEnabled(false);


        for (int i=0; i < feedItems.size(); i++) {
            FeedItem fitem = feedItems.get(i);
            if(i == listPos)
                fitem.setIsPlaying(true);
            else
                fitem.setIsPlaying(false);
            feedItems.set(i, fitem);
        }
        notifyDataSetChanged();
    }

    void UpdateSongTime(ViewHolderItem vhi) {
        class updateSongTimeTask implements Runnable {
            ViewHolderItem vhItem;
            updateSongTimeTask(ViewHolderItem viewHolderItem) { vhItem = viewHolderItem; }
            public void run() {
                vhItem.startTime = mediaPlayer.getCurrentPosition();
                final String tt = String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) vhItem.startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) vhItem.startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) vhItem.startTime)));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vhItem.tvStartTime.setText(tt);
                        vhItem.seekbar.setProgress((int) vhItem.startTime);
                    }
                });
                vhItem.myHandler.postDelayed(this, 100);
            }
        }
        Thread t = new Thread(new updateSongTimeTask(vhi));
        t.start();
    }

}
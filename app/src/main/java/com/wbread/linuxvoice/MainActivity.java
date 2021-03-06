package com.wbread.linuxvoice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wbread.linuxvoice.service.LinuxVoiceService;


public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        RSSfeedFragment.OnFragmentInteractionListener,
        PDFlistFragment.OnFragmentInteractionListener,
        NewsArticleFragment.OnFragmentInteractionListener,
        PrefFragment.OnFragmentInteractionListener,
        PodcastFeedFragment.OnFragmentInteractionListener
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public void onFragmentInteraction(Uri uri){}

    String articleContent = null;
    String articleHeader = null;

    LinuxVoiceService lvs;
    ServiceConnection sConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent int1 = getIntent();
        articleContent = int1.getStringExtra("ArticleContent");
        articleHeader = int1.getStringExtra("ArticleHeader");

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LinuxVoiceService.class);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                lvs = ((LinuxVoiceService.LinuxVoiceBinder) binder).getService();
            }
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        bindService(intent, sConn, Context.BIND_AUTO_CREATE);
        startService(intent);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(articleContent!=null && articleHeader!=null){
            position = -1;
        }

        switch (position + 1) {
            case 0:
                ft.replace(R.id.container, NewsArticleFragment.newInstance(articleContent,articleHeader));
                articleContent = null;
                articleHeader = null;
                break;
            case 1:
                ft.replace(R.id.container, RSSfeedFragment.newInstance("",""));
                break;
            case 2:
                ft.replace(R.id.container, PodcastFeedFragment.newInstance("",""));
                break;
            case 3:
                ft.replace(R.id.container, PDFlistFragment.newInstance("",""));
                break;
            case 4:
//                ft.replace(R.id.container, VotePlayersFragment.newInstance("",""));
                break;
            case 5:
                ft.replace(R.id.container, PrefFragment.newInstance("",""));
                break;
        }
        ft.commitAllowingStateLoss();


    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.nav_drawer, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

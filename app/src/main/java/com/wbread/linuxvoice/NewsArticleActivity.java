package com.wbread.linuxvoice;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class NewsArticleActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        RSSfeedFragment.OnFragmentInteractionListener,
        PDFlistFragment.OnFragmentInteractionListener,
        PodcastFeedFragment.OnFragmentInteractionListener
{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    public void onFragmentInteraction(Uri uri){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout_article));


        WebView webView = (WebView) findViewById(R.id.webView);

        Intent int1 = getIntent();
        String articleContent = int1.getStringExtra("ArticleContent");
        String articleHeader = int1.getStringExtra("ArticleHeader");
        String sHeader = "<h1 class=\"title single-title\">" + articleHeader + "</h1>";

        articleContent = "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + sHeader+articleContent;


        try{
            Spanned S = Html.fromHtml(articleContent);
            ImageSpan[] is = (ImageSpan[]) S.getSpans(0,0,Class.forName("android.text.style.ImageSpan"));

        }catch(ClassNotFoundException e){
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", articleContent, "text/html", "UTF-8", "");
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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position + 1) {
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
        }
        ft.commitAllowingStateLoss();


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

package com.wbread.linuxvoice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class NewsArticleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_article, menu);

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

        return true;
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

package com.wbread.linuxvoice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsArticleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsArticleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ArticleContent = "ArticleContent";
    private static final String ARG_paramArticleHeader = "ArticleHeader";

    // TODO: Rename and change types of parameters
    private String mArticleContent;
    private String mArticleHeader;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramArticleContent Parameter 1.
     * @param paramArticleHeader Parameter 2.
     * @return A new instance of fragment NewsArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsArticleFragment newInstance(String paramArticleContent, String paramArticleHeader) {
        NewsArticleFragment fragment = new NewsArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ArticleContent, paramArticleContent);
        args.putString(ARG_paramArticleHeader, paramArticleHeader);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleContent = getArguments().getString(ARG_ArticleContent);
            mArticleHeader = getArguments().getString(ARG_paramArticleHeader);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_article, container, false);

        WebView webView = (WebView) v.findViewById(R.id.webView);

        String articleContent = mArticleContent;
        String articleHeader = mArticleHeader;
        String sHeader = "<h1 class=\"title single-title\">" + articleHeader + "</h1>";

        articleContent = "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + sHeader+articleContent;


        try{
            Spanned S = Html.fromHtml(articleContent);
            ImageSpan[] is = (ImageSpan[]) S.getSpans(0,0,Class.forName("android.text.style.ImageSpan"));

        }catch(ClassNotFoundException e){
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", articleContent, "text/html", "UTF-8", "");


        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

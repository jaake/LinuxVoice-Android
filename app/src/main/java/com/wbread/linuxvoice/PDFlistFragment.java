package com.wbread.linuxvoice;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wbread.linuxvoice.adapter.FeedListAdapter;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PDFlistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PDFlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PDFlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String URL_FEED = "http://www.linuxvoice.com/feed/?cat=33";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PDFlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PDFlistFragment newInstance(String param1, String param2) {
        PDFlistFragment fragment = new PDFlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PDFlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    MainActivity act = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pdflist, container, false);

        act = (MainActivity) getActivity();

        listView = (ListView) v.findViewById(R.id.list);
        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(act, feedItems);
        listView.setAdapter(listAdapter);

        new Thread(new Runnable() {
            public void run() {
                ReadPDFlist();
            }
        }).start();


        return v;
    }


    private void ReadPDFlist(){

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

                        act.runOnUiThread(new Runnable() {
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

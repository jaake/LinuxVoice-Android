package com.wbread.linuxvoice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbread.linuxvoice.service.LinuxVoiceService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrefFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrefFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
     * @return A new instance of fragment PrefFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrefFragment newInstance(String param1, String param2) {
        PrefFragment fragment = new PrefFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
    {

    }

    public PrefFragment() {
        // Required empty public constructor
    }

    MainActivity act;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        act = (MainActivity) getActivity();

        addPreferencesFromResource(R.xml.preference);

        final SwitchPreference rssPref      = (SwitchPreference) findPreference("get_rss_notification");
        final SwitchPreference podcastPref  = (SwitchPreference) findPreference("get_podcast_notification");
        final SwitchPreference magazinePref = (SwitchPreference) findPreference("get_magazine_notification");

        rssPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object obj) {
                rssPref.setChecked(Boolean.parseBoolean(obj.toString()));
                if (act.lvs != null) {
                    act.lvs.setRSSListener();
                }
                return true;
            }
        });

        podcastPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object obj) {
                podcastPref.setChecked(Boolean.parseBoolean(obj.toString()));
                if (act.lvs != null) {
                    act.lvs.setPodcastListener();
                }
                return true;
            }
        });

        magazinePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object obj) {
                magazinePref.setChecked(Boolean.parseBoolean(obj.toString()));
                if (act.lvs != null) {
                    act.lvs.setPDFListListener();
                }
                return true;
            }
        });

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

package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import plt.tud.de.example.R;

/**
 * Fragment to display settings
 */
public class LinkedDataFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.activity_device_explorer, container, false);
        return inflater.inflate(R.layout.linkeddatalist, container, false);
    }
}

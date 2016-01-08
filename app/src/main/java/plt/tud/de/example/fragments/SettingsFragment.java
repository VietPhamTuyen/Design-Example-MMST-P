package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import plt.tud.de.example.R;

/**
 * Fragment to display settings
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
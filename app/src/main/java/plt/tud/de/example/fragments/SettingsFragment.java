package plt.tud.de.example.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;

/**
 * Fragment to display settings
 */
public class SettingsFragment extends Fragment {
    private View view;

    private static final String DEFAULT_LD_ENDPOINT = "http://eatld.et.tu-dresden.de/sparql-auth";
    private static final String DEFAULT_LD_URI = "http://eatld.et.tu-dresden.de/mti-mmst2015_g2_1";

    private static final String PREF_KEY_LD_ENDPOINT = "LDEndpoint";
    private static final String PREF_KEY_LD_URI = "LDUri";

    static Controller controller = new Controller();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_settings, container, false);

        final TextView editLDEndpoint = (TextView) view.findViewById(R.id.action_setLDEndpoint);
        final TextView editLDUri = (TextView) view.findViewById(R.id.action_setLDUri);

        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        final String ldEndpoint = settings.getString(PREF_KEY_LD_ENDPOINT, DEFAULT_LD_ENDPOINT);
        final String ldUri = settings.getString(PREF_KEY_LD_URI, DEFAULT_LD_URI);

        updateSetting(editLDEndpoint, PREF_KEY_LD_ENDPOINT, ldEndpoint);
        updateSetting(editLDUri, PREF_KEY_LD_URI, ldUri);

        editLDEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) view.findViewById(R.id.action_setLDEndpoint);
                String setting = PREF_KEY_LD_ENDPOINT;
                String titleText = getResources().getText(R.string.pref_title_linkedData_endpoint).toString();
                editSetting(tv, setting, titleText);
            }
        });

        editLDUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) view.findViewById(R.id.action_setLDUri);
                String setting = PREF_KEY_LD_URI;
                String titleText = getResources().getText(R.string.pref_title_linkedData_uri).toString();
                editSetting(tv, setting, titleText);
            }
        });

        Button settingsToDefault = (Button) view.findViewById(R.id.action_settings_default);
        settingsToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSetting(editLDEndpoint, PREF_KEY_LD_ENDPOINT, DEFAULT_LD_ENDPOINT);
                updateSetting(editLDUri, PREF_KEY_LD_URI, DEFAULT_LD_URI);
            }
        });


        Button conntectTest = (Button) view.findViewById(R.id.buttonConnectTest);
        conntectTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    controller.createLD("connectTest", "", "", "");

                    controller.deleteList();
                    controller.createLD("getMaintenancePlan", "", "", "");
                    controller.getStartList();


                } else {
                    Toast.makeText(getActivity(), "keine Internetverbindung", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateSetting(TextView tv, String preference, String newValue) {
        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, newValue);
        editor.commit();
        tv.setText(newValue);






    }

    private void editSetting(TextView textView, String settingString, String titleText) {
        final TextView tv = textView;
        final String setting = settingString;

        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        String editText = settings.getString(setting, "");

        final Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.settings_dialog);
        TextView title = (TextView) d.findViewById(R.id.label);
        title.setText(titleText);

        final EditText edit = (EditText) d.findViewById(R.id.edit1);
        edit.setText(editText);

        Button ok = (Button) d.findViewById(R.id.button2);
        Button cancel = (Button) d.findViewById(R.id.button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSetting(tv, setting, edit.getText().toString());
                if (setting.equals(PREF_KEY_LD_ENDPOINT))
                    controller.changeEndpoint(edit.getText().toString());
                if (setting.equals(PREF_KEY_LD_URI))
                    controller.changeUri(edit.getText().toString());
                d.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }




}
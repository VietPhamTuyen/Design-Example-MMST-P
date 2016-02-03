package plt.tud.de.example.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;

/**
 * Fragment to display settings
 */
public class SettingsFragment extends Fragment {
    private View view;

    private static final String DEFAULT_OPC_ADRESS = "opc.tcp://10.4.53.25:4845/PCSOSClient1";
    private static final String DEFAULT_LD_ENDPOINT = "http://eatld.et.tu-dresden.de/sparql";
    private static final String DEFAULT_LD_URI = "http://eatld.et.tu-dresden.de/gzat";

    private static final String PREF_KEY_OPC_ADRESS = "OPCAdress";
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
        final String opcAdress = settings.getString(PREF_KEY_OPC_ADRESS, DEFAULT_OPC_ADRESS);
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


        Button conntectTest = (Button)view.findViewById(R.id.buttonConnectTest);
        conntectTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.createLD("connectTest", "", "", "");
            }
        });

        return view;
    }

    private void updateSetting(TextView tv, String preference, String newValue){
        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, newValue);
        editor.commit();
        tv.setText(newValue);
    }

    private void editSetting(TextView textView, String settingString, String titleText){
        final TextView tv = textView;
        final String setting = settingString;

        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        String editText = settings.getString(setting, "");

        final Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.settings_dialog);
        TextView title = (TextView)d.findViewById(R.id.label);
        title.setText(titleText);

        final EditText edit = (EditText) d.findViewById(R.id.edit1);
        edit.setText(editText);

        Button ok = (Button) d.findViewById(R.id.button2);
        Button cancel = (Button) d.findViewById(R.id.button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSetting(tv, setting, edit.getText().toString());
                if (setting.equals(PREF_KEY_LD_ENDPOINT)) controller.changeEndpoint(edit.getText().toString());
                if (setting.equals(PREF_KEY_LD_URI)) controller.changeUri(edit.getText().toString());
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
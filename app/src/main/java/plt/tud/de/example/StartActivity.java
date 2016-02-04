package plt.tud.de.example;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import plt.tud.de.example.Adapter.PlanAdapter;
import plt.tud.de.example.Adapter.TourAdapter;
import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.Tour;
import plt.tud.de.example.showAppInformations.AppInformationActivity;

/**
 * Created by Viet on 05.01.2016.
 */
public class StartActivity extends AppCompatActivity {

    private static final String DEFAULT_LD_ENDPOINT = "http://eatld.et.tu-dresden.de/sparql-auth";
    private static final String DEFAULT_LD_URI = "http://eatld.et.tu-dresden.de/mti-mmst2015_g2_1";

    private static final String PREF_KEY_LD_ENDPOINT = "LDEndpoint";
    private static final String PREF_KEY_LD_URI = "LDUri";

    static boolean isActive = false;
    static Controller controller = new Controller();
    ListView tourList;
    static boolean init = false;
    StartActivity start =this;

    private long timer = 0;
    int screen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        isActive = true;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tourList = (ListView) findViewById(R.id.listTour);
        tourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                TextView text = (TextView) view;
                if (!init) {
                    controller.setCurrentTour(controller.getListname(0));
                    init = true;
                }

                if (controller.checkIfTour(text.getText().toString())) { //if tour is clicked
                    controller.changeNavDToMaintenanceList(text.getText().toString());
                    controller.setCurrentTour(text.getText().toString());
                    screen=1;
                    Log.i("current Tour", text.getText().toString());
                } else if (position == 0) {                             //if back is clicked
                    controller.setNavList();
                    screen=0;
                } else {
                    Log.i("current Plan", text.getText().toString());



                    long t = System.currentTimeMillis();
                    if (t - timer > 5000) {    // time from last toast to now
                        controller.setCurrentPlan(text.getText().toString());
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        makeToastNoTime("still loading");

                    }
                }


            }
        });


       // getLDData();
        controller.createLD("getMaintenancePlan", "", "", ""); //TODO

       controller.updateStartActivity(this);
        setTitle(getResources().getText(R.string.app_name));
    }



    private void getLDData() {
        if (isNetworkAvailable()) {
            controller.deleteList();
            SharedPreferences settings = this.getSharedPreferences("settings", 0);
            String ldEndpoint = settings.getString(PREF_KEY_LD_ENDPOINT, DEFAULT_LD_ENDPOINT);
            String ldUri = settings.getString(PREF_KEY_LD_URI, DEFAULT_LD_URI);
            controller.changeEndpoint(ldEndpoint);
            controller.changeUri(ldUri);
            controller.createLD("getMaintenancePlan", "", "", ""); //TODO

            controller.updateStartActivity(this);
        } else {
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) start.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





    /**
     * refresh List
     * @param listItem
     */
    public void updateListView(ArrayList<String> listItem) {
        Log.i("debug", "start updateListView");
        List<String> newString = listItem;
        String[] inputStringList = newString.toArray(new String[newString.size()]);
        ListView parameterList = (ListView) findViewById(R.id.listTour);
        parameterList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, inputStringList));
    }


    @Override
    public void onBackPressed() {
        if(screen == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to close the app?").setCancelable(true).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StartActivity.super.onBackPressed();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            controller.setNavList();
            screen=0;

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }
    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }
    // TODO http://developer.android.com/training/keyboard-input/navigation.html

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
            {
                //your Action code
               // return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }




    public void makeToastNoTime(String text) {
        final String txt = text;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(start, txt, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void makeToast(String text) {
        final String txt = text;
        timer = System.currentTimeMillis();

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(start, txt, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent2 = new Intent(start, AppInformationActivity.class);
            intent2.putExtra("site", "settings");
            startActivity(intent2);
            return true;
        }
        if (id == R.id.about) {
            Intent intent2 = new Intent(start, AppInformationActivity.class);
            intent2.putExtra("site", "about");
            startActivity(intent2);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

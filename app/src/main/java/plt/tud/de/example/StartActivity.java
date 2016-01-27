package plt.tud.de.example;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viet on 05.01.2016.
 */
public class StartActivity extends AppCompatActivity {

    static boolean isActive = false;
    Controller controller = new Controller();
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

                if (controller.checkTour(text.getText().toString())) { //if tour is clicked
                    controller.changeNavDToMaintenanceList(text.getText().toString());
                    controller.setCurrentTour(text.getText().toString());
                    screen=1;
                    Log.i("current Tour", text.getText().toString());
                } else if (position == 0) {                             //if back is clicked
                    controller.setNavList();
                    screen=0;
                } else {
                    controller.setCurrentPlan(text.getText().toString());
                    Log.i("current Plan", text.getText().toString());



                    long t = System.currentTimeMillis();
                    if (t - timer > 5000) {    // time from last toast to now
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        makeToastNoTime("still loading");

                    }
                }


            }
        });



        controller.createLD("getMaintenancePlan", "", "", ""); //TODO

        controller.updateStartActivity(this);


        setTitle(getResources().getText(R.string.app_name));
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
}

package plt.tud.de.example.showAppInformations;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;
import plt.tud.de.example.fragments.AboutFragment;
import plt.tud.de.example.fragments.SettingsFragment;

/**
 * Created by Viet on 03.02.2016.
 */
public class AppInformationActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawer;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    String destination;

    private Context mContext = null;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

    FragmentStatePagerAdapter adapterViewPager;
static Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_information);
        controller.synchAppinfo(this);
        Bundle extras = getIntent().getExtras();
        String destination = extras.getString("site"); //could be settings or about

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
      //  setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        //Set the Connection fragment as start fragment
        if ((findViewById(R.id.content_frame) != null) && (destination.equals("about"))) {
            actionBar.setTitle("Über diese App");
            // if restored from a previous state,no need to do anything
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the content frame
            AboutFragment connFragment = new AboutFragment();

            // pass extras
            connFragment.setArguments(getIntent().getExtras());

            // Add the fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, connFragment).commit();
        }else if ((findViewById(R.id.content_frame) != null) && (destination.equals("settings"))) {

            actionBar.setTitle("Einstellungen");
            // if restored from a previous state,no need to do anything
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the content frame
            SettingsFragment connFragment = new SettingsFragment();

            // pass extras
            connFragment.setArguments(getIntent().getExtras());

            // Add the fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, connFragment).commit();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void successLDTest(boolean status){
        try{
            Log.i("successLDTest", String.valueOf(status) );
            if(status)
            Toast.makeText(this,"Verbindung erfolgreich", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this,"keine Verbindung", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.i("successLDTest", "error "+e.getMessage() );
        }
    }

}

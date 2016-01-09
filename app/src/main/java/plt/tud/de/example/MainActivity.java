package plt.tud.de.example;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.app.ActionBar.Tab;
import android.app.ActionBar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import plt.tud.de.example.fragments.AboutFragment;
import plt.tud.de.example.fragments.ContentFragment;
import plt.tud.de.example.fragments.Fragment_Implementation;
import plt.tud.de.example.fragments.Fragment_Result;
import plt.tud.de.example.fragments.Fragment_Return;
import plt.tud.de.example.fragments.Fragment_Task;
import plt.tud.de.example.fragments.LinkedDataFragment;
import plt.tud.de.example.fragments.SettingsFragment;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {


    private String[] drawerListViewItems;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawer;

    private Context mContext = null;
    public static LDQueryActivity LD = new LDQueryActivity();

    ListView tweetList;
    ArrayAdapter<String> arrayAdapter;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String myTAG = "debug";
    public int currentPage;
    public int tabPosition = 0;

    static Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        controller.synch(this);
        setContentView(R.layout.activity_main);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //Create Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                   /* host Activity */
                mDrawerLayout,          /* DrawerLayout object */
                toolbar,                /* toolbar to act upon */
                R.string.drawer_open,   /* "open drawer" description for accessibility */
                R.string.drawer_close   /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        controller.setNavList();

        mDrawer.setOnItemClickListener(new DrawerItemClickListener());


        /**
         * on swiping the viewpager make respective tab selected
         * */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                //actionBar.setSelectedNavigationItem(position);
                Log.i("tab position", String.valueOf(position));
                tabPosition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        Bundle extras = getIntent().getExtras();
        String inputString = extras.getString("position");
        int position = Integer.parseInt(inputString);   // TODO whatList?
        //changeList(position);                            //TODO check for C2
        controller.changeNavDToMaintenanceList(position);


    }


    public void setMenuView(ArrayList<String> incomingList) {
        //drawerListViewItems = getResources().getStringArray(R.array.items);

        List<String> newString = incomingList;


        drawerListViewItems = newString.toArray(new String[newString.size()]);


        mDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawer.setAdapter(new ArrayAdapter<String>(this,
                R.layout.list_item, drawerListViewItems));
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to go back?").setCancelable(true).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.super.onBackPressed();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }

    }


    /**
     * call from LDQuery.Activity.handleMessage()
     *
     * @param listItem
     */
    public void linkedDataListInput(ArrayList<String> listItem) {

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        tweetList = (ListView) this.findViewById(R.id.linkedDataList);
        tweetList.setAdapter(arrayAdapter);

    }


    public void set_currentPage(int pageIndex) {
        mViewPager.setCurrentItem(pageIndex);
    }


    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.button_task_ok:
                set_currentPage(1);
                break;
            case R.id.button_implementation_ok:
                set_currentPage(2);
                break;
            case R.id.button_return_ok:
                set_currentPage(3);
                break;
            case R.id.button_result_ok:
                break;
            case R.id.button_task_cancel:
                break;
            case R.id.button_implementation_cancel:
                set_currentPage(0);
                break;
            case R.id.button_return_cancel:
                set_currentPage(1);
                break;
            case R.id.button_result_cancel:
                set_currentPage(2);
                break;
        }
    }

//--------------------------------------------------------------------------------

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        MainActivity main;

        public SectionsPagerAdapter(FragmentManager fm, MainActivity main) {
            super(fm);
            this.main = main;
            Log.i(myTAG, "create SectionPagerAdapter");
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(myTAG, "getItem" + String.valueOf(position));
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = Fragment.instantiate(mContext, Fragment_Task.class.getName());
                    break;
                case 1:
                    fragment = Fragment.instantiate(mContext, Fragment_Implementation.class.getName());
                    break;
                case 2:
                    fragment = Fragment.instantiate(mContext, Fragment_Return.class.getName());
                    break;
                case 3:
                    fragment = Fragment.instantiate(mContext, Fragment_Result.class.getName());
                    break;
                default:
                    fragment = Fragment.instantiate(mContext, Fragment_Task.class.getName());
            }
            return fragment;
        }


        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i(myTAG, "getPageTitle" + String.valueOf(position));
            switch (position) {
                case 0:
                    return "Aufgabe";
                case 1:
                    return "Durchfuehrung";
                case 2:
                    return "Rueckgabewert";
                case 3:
                    return "Ergebnis";
            }
            return null;
        }


    }

    /**
     * display workingSteps in list in Fragment_Task
     *
     * @param position - position in List
     */
    public void changeList(int position) {
        ArrayList<String> listItem = new ArrayList<>();
        listItem = controller.getWorkingSteps(position);
        List<String> newString = listItem;

        String[] inputStringList = newString.toArray(new String[newString.size()]);

        // ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<String>(this, R.layout.list_item, inputStringList);
        ListView parameterList = (ListView) findViewById(R.id.listView_task);
        parameterList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, inputStringList));

    }

    /**
     * listener for Navigation Drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView text = (TextView) view;

            if (controller.checkTour(text.getText().toString())) { //if tour is clicked
                controller.changeNavDToMaintenanceList(position);
            }else if (position == 0){
                controller.setNavList();
            }else {


                    if (tabPosition == 0) {   //in fragment_task
                        changeList(position);
                    }

                    mDrawerLayout.closeDrawers();



                // controller.setNavList(); // have to show positions?
            }


        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }


}

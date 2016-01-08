package plt.tud.de.example;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import plt.tud.de.example.fragments.AboutFragment;
import plt.tud.de.example.fragments.ContentFragment;
import plt.tud.de.example.fragments.Fragment_Implementation;
import plt.tud.de.example.fragments.Fragment_Result;
import plt.tud.de.example.fragments.Fragment_Return;
import plt.tud.de.example.fragments.Fragment_Task;
import plt.tud.de.example.fragments.LinkedDataFragment;
import plt.tud.de.example.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawer;

    private Context mContext=null;
    public static LDQueryActivity LD = new LDQueryActivity();

    ListView tweetList;
    ArrayAdapter<String> arrayAdapter;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String myTAG = "debug";
    public int currentPage;

    static Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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




        //Creating the floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.connectBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete
                controller.createLD("getMaintenancePlan","","","");

                //Snackbar.make(findViewById(R.id.connectBtn), "Start your neat stuff here", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        mDrawer = (NavigationView) findViewById(R.id.left_drawer);
        setupDrawerContent(mDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.action_connection:
                fragmentClass = ContentFragment.class;
                findViewById(R.id.connectBtn).setVisibility(View.VISIBLE);
                break;
            case R.id.action_about:
                fragmentClass = AboutFragment.class;
                findViewById(R.id.connectBtn).setVisibility(View.GONE);
                break;
            case R.id.action_settings:
                fragmentClass = SettingsFragment.class;
                findViewById(R.id.connectBtn).setVisibility(View.GONE);
                break;

            case R.id.action_linkedData:
                //TODO

                fragmentClass = LinkedDataFragment.class;
                findViewById(R.id.connectBtn).setVisibility(View.VISIBLE);

                controller.createLD("getMaintenancePlan","","",""); //TODO
                break;
            default:
                fragmentClass = ContentFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
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
         super.onBackPressed();
         } else {
         // Otherwise, select the previous step.
             mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
         }

    }


    /**
     * call from LDQuery.Activity.handleMessage()
     * @param listItem
     */
    public void linkedDataListInput(ArrayList<String> listItem){

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        tweetList= (ListView)this.findViewById(R.id.linkedDataList);
        tweetList.setAdapter(arrayAdapter);

    }




    public void set_currentPage(int pageIndex) {
        mViewPager.setCurrentItem(pageIndex);
    }


    public void onButtonClicked(View v){
        switch(v.getId()){
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
            Log.i(myTAG,"getItem"+ String.valueOf(position));
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
            Log.i(myTAG,"getPageTitle"+ String.valueOf(position));
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

    /** TODO
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(enableAdd)
            menu.add(0, MENU_ADD, Menu.NONE, R.string.your-add-text).setIcon(R.drawable.your-add-icon);
        if(enableList)
            menu.add(0, MENU_LIST, Menu.NONE, R.string.your-list-text).setIcon(R.drawable.your-list-icon);
        if(enableRefresh)
            menu.add(0, MENU_REFRESH, Menu.NONE, R.string.your-refresh-text).setIcon(R.drawable.your-refresh-icon);
        if(enableLogin)
            menu.add(0, MENU_LOGIN, Menu.NONE, R.string.your-login-text).setIcon(R.drawable.your-login-icon);
        return super.onPrepareOptionsMenu(menu);
    }
     */

}

package plt.tud.de.example;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.ActionBar;

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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import plt.tud.de.example.Adapter.PlanAdapter;
import plt.tud.de.example.Adapter.StatusAdapter;
import plt.tud.de.example.Adapter.TourAdapter;
import plt.tud.de.example.fragments.Fragment_Implementation;
import plt.tud.de.example.fragments.Fragment_Result;
import plt.tud.de.example.fragments.Fragment_Return;
import plt.tud.de.example.fragments.Fragment_Task;
import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.Status;
import plt.tud.de.example.model.Tour;
import plt.tud.de.example.showAppInformations.AppInformationActivity;

public class MainActivity extends AppCompatActivity implements
        ActionBar.TabListener {


    public static boolean isActive = false;
    private String[] drawerListViewItems;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawer;
    android.support.v7.app.ActionBar actionBar;

    private Context mContext = null;

    ListView tweetList;
    ArrayAdapter<String> arrayAdapter;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String myTAG = "debug";
    public int currentPage;
    public int tabPosition = 0;

    static Controller controller = new Controller();


    MainActivity main = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        controller.synch(this);
        setContentView(R.layout.activity_main);
        isActive = true;


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);


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
                Log.i("onDrawerClosed", "close");
                super.onDrawerClosed(view);
                //   invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                Log.i("onDrawerOpened", "open");
                super.onDrawerOpened(drawerView);
                //    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //show all found tours in the navigationDrawer
        controller.setNavList();

        mDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawer.setOnItemClickListener(new DrawerItemClickListener());


        /**
         * on swiping the viewpager make respective tab selected
         * */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.i("position", " " + String.valueOf(position));
                Log.i("tab position", String.valueOf(position));
                //save current tab position
                tabPosition = position;
                currentPage = position;
                if (tabPosition == 0) {
                    changeListTask();
                } else if (tabPosition == 1) {
                    changeListImplementation();
                } else if (tabPosition == 2) {
                    changeListReturn();
                } else if (tabPosition == 3) {
                    changeListResult();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        controller.changeNavDToMaintenanceList();


        changeListTask();
        changeListImplementation();
        changeListReturn();
        changeListResult();

        setTitleName();


    }


    /**
     * keycode - event.keycode - input "Kurzbeschreibung_Dreh-Drueckstelle_HID.pdf" page 2
     * 111 - KEYCODE_ESCAPE 8
     * 19 - KEYCODE_DPAD_UP 3     -> 47 - KEYCODE_W
     * 20 - KEYCODE_DPAD_DOWN 4   -> 51 - KEYCODE_S
     * 21 - KEYCODE_DPAD_LEFT 2   -> 29 - KEYCODE_A
     * 22 - KEYCODE_DPAD_RIGHT 5  -> 32 - KEYCODE_D
     * 67 - KEYCODE_DEL 6
     * 112 - KEYCODE_FORWARD_DEL 7
     * x
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("key", "----------------------------");
        Log.i("Keycode", " " + String.valueOf(keyCode));
        Log.i("Keyevent", " " + event.toString());

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER: {
                return super.onKeyDown(keyCode, event);
            }

            case KeyEvent.KEYCODE_DPAD_LEFT: {
                if (currentPage == 0) {

                    mDrawerLayout.openDrawer(GravityCompat.START);

                } else if (!(currentPage == 0)) {
                    set_currentPage(currentPage - 1);
                }

                return false;
            }

            case KeyEvent.KEYCODE_DPAD_RIGHT: {

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();

                } else if (!(currentPage == 3)) {
                    set_currentPage(currentPage + 1);
                } else { // current page == 3
                    //TODO add dialog
                    controller.nextPlan();

                    setTitleName();
                }

                return false;

            }
            case KeyEvent.KEYCODE_DPAD_UP: {
                //     if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                super.onKeyDown(keyCode, event);

            }
            case KeyEvent.KEYCODE_DPAD_DOWN: {
                //   if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){

                super.onKeyDown(keyCode, event);
            }
            case KeyEvent.KEYCODE_DEL: {
                return super.onKeyDown(keyCode, event);

            }
            case KeyEvent.KEYCODE_BACK: {
                onBackPressed();
                return super.onKeyDown(keyCode, event);
            }
            case KeyEvent.KEYCODE_ESCAPE: {
                onBackPressed();
                return super.onKeyDown(keyCode, event);
            }

        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER: {
                this.getCurrentFocus().clearFocus();
                return false;
            }
            case KeyEvent.KEYCODE_DPAD_LEFT: {
                this.getCurrentFocus().clearFocus();
                return false;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT: {
                this.getCurrentFocus().clearFocus();
                return false;
            }
        }
        return true;
    }


    public void switchToast() {
        Toast.makeText(this, "Switch to next maintenanceplan", Toast.LENGTH_SHORT).show();
    }

    public void beginAgain() {
        Toast.makeText(this, "All Tours done, start with first", Toast.LENGTH_SHORT).show();
    }

    public void makeToast(String text) {
        final String txt = text;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(main, txt, Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * refresh List in Navigation Drawer
     *
     * @param incomingList ... inputList
     */
    public void setMenuViewTour(ArrayList<Tour> incomingList) {
        if (incomingList.get(0) instanceof Tour) {

        }
        //drawerListViewItems = getResources().getStringArray(R.array.items);

        ListView parameterList = (ListView) findViewById(R.id.left_drawer);
        //parameterList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, incomingList));

        if (!incomingList.get(0).getName().equals("Tour")) {
            Tour tour = new Tour("Tour");
            incomingList.add(0, tour);
        }

        TourAdapter adapter = new TourAdapter(this, incomingList);
        parameterList.setAdapter(adapter);

    }


    public void setMenuViewPlan(ArrayList<Plan> incomingList) {
        //drawerListViewItems = getResources().getStringArray(R.array.items);

        ListView parameterList = (ListView) findViewById(R.id.left_drawer);
        //parameterList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, incomingList));

        Plan zurueck = new Plan("zurueck", "zurueck", "zurueck");

        incomingList.add(0, zurueck);
        PlanAdapter adapter = new PlanAdapter(this, incomingList);
        parameterList.setAdapter(adapter);

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
        Log.i("home", "home");
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent2 = new Intent(main, AppInformationActivity.class);
            intent2.putExtra("site", "settings");
            startActivity(intent2);
            return true;
        }
        if (id == R.id.about) {
            Intent intent2 = new Intent(main, AppInformationActivity.class);
            intent2.putExtra("site", "about");
            startActivity(intent2);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {


        if (mViewPager.getCurrentItem() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to close the app?").setCancelable(true).
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

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        tweetList = (ListView) this.findViewById(R.id.linkedDataList);
        tweetList.setAdapter(arrayAdapter);

    }

    public void callpage(int page) {
        set_currentPage(page);
    }


    public void set_currentPage(int pageIndex) {
        mViewPager.setCurrentItem(pageIndex);
    }


    public void onButtonClicked(View v) {
        switch (v.getId()) {

            case R.id.button_result_ok:
                switchToast();
                Log.i("button", "button_result_ok");
                //TODO add dialog
                controller.nextPlan();
                setTitleName();
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
     */
    public void changeListTask() {

        ArrayList<String> listItem = new ArrayList<>();
        String currentKennzeichen = controller.getCurrentKennzeichen();
        listItem.add(currentKennzeichen);

        controller.saveCurrentKennzeichen(currentKennzeichen);
        Log.i("debug", "getWorkingSteps");
        List<String> newString = listItem;

        String[] inputStringList = newString.toArray(new String[newString.size()]);
        try {
            // ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<String>(this, R.layout.list_item, inputStringList);
            if (listItem.size() > 0) {
                Log.i("debug", "list >0");
                if (tabPosition == 0) {
                    ListView parameterList = (ListView) findViewById(R.id.listView_task);
                    try {
                        parameterList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, inputStringList));
                    } catch (Exception e) {

                    }
                }
            } else {
                Toast.makeText(this, "to be loadet",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "to be loadet",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }


    /**
     * display workingSteps in list in Fragment_Implementation
     */
    public void changeListImplementation() {
        ArrayList<String> listItem = new ArrayList<>();
        listItem = controller.getWorkingSteps();
        Log.i("debug", "getWorkingSteps");
        List<String> newString = listItem;

        String[] inputStringList = newString.toArray(new String[newString.size()]);

        // ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<String>(this, R.layout.list_item, inputStringList);
        if (listItem.size() > 0) {
            if (tabPosition == 1) {
                ListView parameterList = (ListView) findViewById(R.id.listView_implementation);
                parameterList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, inputStringList));
            }
        } else {
            Toast.makeText(this, "to be loadet",
                    Toast.LENGTH_LONG).show();
        }

    }


    //TODO
    public void changeListResult() {
        ArrayList<Status> CurrentResult = controller.showCurrentResult();
        Log.i("debug", "getWorkingSteps");

        Status[] inputStringList = CurrentResult.toArray(new Status[CurrentResult.size()]);

        // ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<String>(this, R.layout.list_item, inputStringList);
        if (CurrentResult.size() > 0) {
            if (tabPosition == 3) {
                ListView parameterList = (ListView) findViewById(R.id.listView_result);
                // parameterList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, inputStringList));

                StatusAdapter adapter = new StatusAdapter(this, CurrentResult);
                parameterList.setAdapter(adapter);


            }
        } else {
            Toast.makeText(this, "to be loadet",
                    Toast.LENGTH_LONG).show();
        }

    }


    //TODO
    public void changeListReturn() {

    }


    public void setTitleName() {
        String checkedP = " ";
        if (controller.isChecked()) {
            checkedP = " Pcheck ";
        }
        setTitle(controller.getCurrentTour() + checkedP + controller.getCurrentPlan());
    }


    /**
     * listener for Navigation Drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            try {


                LinearLayout layout = (LinearLayout) view;
                CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkBox1);
                changeView(checkBox);

            } catch (Exception e) {
                Log.i("error", " " + e.getMessage());

            }

        }
    }


    /*
     *
     */
    public void changeView(CheckBox checkBox) throws Exception {
        /**
         CheckBox checkBox = new CheckBox(mContext);
         if(view instanceof LinearLayout) {
         LinearLayout layout = (LinearLayout) view;
         checkBox = (CheckBox) layout.findViewById(R.id.checkBox1);
         }else if(view instanceof CheckBox){

         }else {
         throw new Exception();
         }
         */
        Log.i("changeView", "show checkboxtext " + checkBox.getText());
        if (controller.checkIfTour(checkBox.getText().toString())) {             //if tour is clicked
            if (!checkBox.getText().toString().equals("Tour")) {
                controller.changeNavDToMaintenanceList(checkBox.getText().toString());
                controller.setCurrentTour(checkBox.getText().toString());
            }
        } else if (checkBox.getText().equals("zurueck")) {                       //if back is clicked
            controller.setNavList();                                            //change to Tour List
        } else {
            controller.setCurrentPlan(checkBox.getText().toString());
            setTitleName();


            changeListTask();
            changeListImplementation();
            changeListReturn();
            changeListResult();

            mDrawerLayout.closeDrawers();

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


    @Override
    public void onStart() {
        super.onStart();
        Log.i("main", "onStart");
        isActive = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("main", "onResume");
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("main", "onStop");
        isActive = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("main", "onPause");
        isActive = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_about, menu);
        return true;
    }


}

package plt.tud.de.example;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.Status;
import plt.tud.de.example.model.Tour;
import plt.tud.de.example.model.WorkingStep;
import plt.tud.de.example.showAppInformations.AppInformationActivity;

/**
 * Created by Viet on 06.01.2016.
 */
public class Controller {

    // static ArrayList<String> tourList = new ArrayList<String>();
    static ArrayList<Tour> tourList = new ArrayList<>();
    static ArrayList<String> navigationDrawerList = new ArrayList<>();            //Nav Drawer Tour, C1,C2,C3...
    static ArrayList<String> maintenanceList = new ArrayList<>();                 //plan list for currentTour
    static ArrayList<String> workingStepStringList = new ArrayList<>();           //Workingsteps for currentPlan

    static String currentTour = "";
    static String currentKennzeichen = "";
    static String currentPlan = "";
    static String currentMaintenanceStep = "";

    static ArrayList<Plan> planList = new ArrayList<Plan>();                //every single plan, no order
    static LDQuery LDA = new LDQuery();
    static MainActivity main;
    static StartActivity start;

    static AppInformationActivity aia = new AppInformationActivity();
    static long timer = 0;

    static ArrayList<String> writeLDList = new ArrayList<>();

    public Controller() {
    }

    public void synch(MainActivity main) {
        this.main = main;
    }

    public void synchAppinfo(AppInformationActivity aia) {
        this.aia = aia;
    }

    //TODO start this with createLD("getMaintenancePlan","","",""); anywhere
    public void createLD(String requestedData, String plan, String tourID, String workingStep) {
        //reset possibleStatusList in current workingsteps before getting new ones
        boolean reload = true;
        if (requestedData.equals("getPossibleStatus")) {
            for (Plan plans : planList) {
                if (plans.getMaintenancePlan().equals(currentPlan)) {
                    if (!plans.check) {
                        ArrayList<WorkingStep> steps = plans.getStepList();
                        for (WorkingStep step : steps) {
                            Log.i("reset", " " + step.getWorkingLabel());
                            step.resetPossibleStatusList();
                        }
                    } else {
                        reload = false;
                        break;
                    }

                }
            }
        }
        if (reload) {
            LDA.createLD(requestedData, plan, tourID, workingStep);
        }
    }

    /**
     * call in LDQuery if a new item from Linked Data is found
     *
     * @param maintenancePlan
     * @param tourID
     * @param kennzeichen
     */
    public void createPlan(String maintenancePlan, String tourID, String kennzeichen) {
        for (Plan existingPlan : planList) {
            if (existingPlan.getMaintenancePlan().equals(maintenancePlan)) {
                return;
            }
        }
        Plan plans = new Plan(maintenancePlan, tourID, kennzeichen);
        planList.add(plans);


        for (Plan plan : planList) {
            String tourIDname = plan.getTourID();
            tourexist:
            {
                //filter add if tour not exist
                for (Tour tour : tourList) {
                    if (tour.getName().equals(tourIDname)) {
                        break tourexist;
                    }
                }
                Tour t = new Tour(plans.getTourID());
                tourList.add(t);
                navigationDrawerList.add(plans.getTourID());
            }
        }


        Log.i("createPlan", " " + maintenancePlan + " " + tourID);
        try {
            getStartList();
        } catch (Exception e) {
            Log.i("error", e.getMessage());
        }
        createLD("getWorkingSteps", maintenancePlan, tourID, "");

    }


    /**
     * from 2. request
     * look for right Plan, save workingStep in there
     *
     * @param maintenancePlan
     * @param workingStep
     */
    public void saveStep(String maintenancePlan, String tourID, String workingStep) {
        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(maintenancePlan)) {
                Log.i("saveStep", maintenancePlan + " , " + workingStep);
                plans.saveStep(workingStep);
                createLD("getWorkingTitle", maintenancePlan, tourID, workingStep);

            }
        }

    }


    /**
     * 3rd LD request, look for right Plan, save workingLabel (how to do it) to the right workingLink(link for the WorkingStep)
     *
     * @param maintenancePlan
     * @param workingStep
     * @param workinglabel
     */
    public void saveWorkingLabel(String maintenancePlan, String workingStep, String workinglabel) {
        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(maintenancePlan)) {
                plans.saveWorkingLabel(workingStep, workinglabel);

            }
        }
    }


    public ArrayList<Plan> getPlanList() {
        return planList;
    }

    public void setCurrentTour(String currentTour) {
        this.currentTour = currentTour;
    }

    public void setCurrentPlan(String currentPlan) {
        //TODO add last linked Data call

        this.currentPlan = currentPlan;
        Log.i("create LD", "getPossibleStatus");
        createLD("getPossibleStatus", currentPlan, currentTour, "");
    }


    /**
     * show a list with all tourIDs  in the navigation drawer
     */
    public void setNavList() {
        navigationDrawerList = new ArrayList<>();
        //TODO
        // tourList = new ArrayList<>();


        for (Plan plan : planList) {
            String tourID = plan.getTourID();
            tourexist:
            {
                //filter add if tour not exist
                for (Tour tour : tourList) {
                    if (tour.getName().equals(tourID)) {
                        break tourexist;
                    }
                }
                Tour t = new Tour(tourID);
                tourList.add(t);
                navigationDrawerList.add(tourID);
            }
        }
        if (main.isActive) {
            main.setMenuViewTour(tourList);
        }

        if (start.isActive) {
            start.updateListView(navigationDrawerList);
        }

    }


    /**
     * show a list with all plans from a certain tour in the navigation drawer
     */
    public void changeNavDToMaintenanceList(String listPlan) {
        maintenanceList = new ArrayList<String>();
        ArrayList<Plan> pList = new ArrayList<>();

        maintenanceList.add("back");

        String plan = listPlan;
        for (Plan p : planList) {
            if (p.getTourID().equals(plan)) {         //add the maintenanceplan if its in the right tour
                maintenanceList.add(p.getMaintenancePlan());
                pList.add(p);
            }
        }
        if (main.isActive) {
            main.setMenuViewPlan(pList);
        }

        if (start.isActive) {
            start.updateListView(maintenanceList);
        }


    }

    public void changeNavDToMaintenanceList() {
        Log.i("changeNavDToM", " ");
        ArrayList<Plan> pList = new ArrayList<>();
        maintenanceList = new ArrayList<String>();
        maintenanceList.add("Zurück");
        String plan = currentTour;
        Log.i("currentTour", " " + currentTour);
        for (Plan p : planList) {
            if (p.getTourID().equals(plan)) {         //add the maintenanceplan if its in the right tour
                maintenanceList.add(p.getMaintenancePlan());
                pList.add(p);
            }
        }

        Log.i("MList size", " " + String.valueOf(maintenanceList.size()));

        if (main.isActive) {
            Log.i("main", "is active ");
            main.setMenuViewPlan(pList);
        } else if (start.isActive) {
            start.updateListView(maintenanceList);
        }
    }


    public String getListname(int position) {

        return tourList.get(position).getName();
    }


    public ArrayList<String> getWorkingSteps() {
        workingStepStringList = new ArrayList<>();

        String check = "";
        for (Plan p : planList) {
            if (p.getMaintenancePlan().equals(currentPlan)) {
                if (p.check) {
                    check = "check";
                }
            }
        }


        Log.i("currentPlan", " " + currentPlan + " " + check);
        for (Plan p : planList) {
            // Log.i("plan", " "+ p.getMaintenancePlan());
            if (currentPlan.equals(p.getMaintenancePlan())) { // if its the right maintenanceStep
                ArrayList<WorkingStep> stepList = p.getStepList();
                for (WorkingStep step : stepList) {                      //then add the working labels to the shown Array
                    workingStepStringList.add(step.getWorkingLabel());
                }
            }

        }
        return workingStepStringList;
    }


    /**
     * look if tour_check is a tour or not (maintenancePlan)
     *
     * @param tour_check
     * @return
     */
    public boolean checkIfTour(String tour_check) {
        for (Tour tour : tourList) {
            if (tour.getName().equals(tour_check)) {
                //save currentTour
                Log.i("checkIfTour", "current Tour: " + tour_check);
                currentTour = tour_check;
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> showCurrentImplementation() {

        return workingStepStringList;
    }


    public ArrayList<String> showCurrentTask() {
        ArrayList<String> currentTask = new ArrayList<>();
        currentTask.add(currentKennzeichen);
        return currentTask;
    }


    public ArrayList<Status> showCurrentResult() {
        ArrayList<Status> currentResult = new ArrayList<>();
        for (Plan plan : planList) {
            if (currentPlan.equals(plan.getMaintenancePlan())) {
                ArrayList<WorkingStep> workingStepList = plan.getStepList();

                for (WorkingStep step : workingStepList) {
                    ArrayList<Status> possibleStepList = step.getPossibleStatusList();
                    for (Status posStatus : possibleStepList) {
                        currentResult.add(posStatus);
                    }
                }
            }
        }

        return currentResult;
    }


    public void updateStartActivity(StartActivity startActivity) {
        this.start = startActivity;
        getStartList();
    }

    public void getStartList() {
        ArrayList<String> listItem = new ArrayList<>();

        listItem.add("Tour");
        for (Plan plan : planList) {
            String tourID = plan.getTourID();
            tourexist:
            {
                //filter existing tours
                for (String tour : listItem) {
                    if (tour.equals(tourID)) {
                        break tourexist;
                    }
                }
                listItem.add(tourID);

            }
        }

        if (start.isActive) {

            start.updateListView(listItem);
        }
    }

    /**
     * show Kennzeichen in first Tab ( Fragment Implementation)
     *
     * @return
     */
    public String getCurrentKennzeichen() {
        Log.i("currentMaintenanceStep", " " + currentMaintenanceStep);
        Log.i("currentPlan", " " + currentPlan);
        for (Plan p : planList) {
            if (currentPlan.equals(p.getMaintenancePlan())) { // if its the right maintenanceStep
                currentKennzeichen = p.getKennzeichen();
            }
        }
        return currentKennzeichen;
    }

    public void saveCurrentKennzeichen(String currentKennzeichen) {
        this.currentKennzeichen = currentKennzeichen;
    }

    public String getCurrentPlan() {
        return currentPlan;
    }

    public void nextPlan() {

        //TODO skip if checked?

        for (int counter = 0; counter < maintenanceList.size(); counter++) {
            if (counter + 1 < maintenanceList.size()) {
                if (maintenanceList.get(counter).equals(currentPlan)) {

                    for (Plan p : planList) {
                        if (p.getMaintenancePlan().equals(currentPlan)) {
                            p.check();                      //check this plan
                        }
                    }

                    setCurrentPlan(maintenanceList.get(counter + 1)); //current plan = next plan
                    //currentPlan =

                    main.callpage(0);


                    changeNavDToMaintenanceList();
                    return;
                }
            } else {     // all plans done
                for (int tourCounter = 1; tourCounter < tourList.size(); tourCounter++) {
                    for (Plan p : planList) {
                        if (p.getMaintenancePlan().equals(currentPlan)) {
                            p.check();                      //check this plan
                        }
                    }
                    if (tourCounter + 1 < tourList.size()) {                //not all tours done

                        if (currentTour.equals(tourList.get(tourCounter).getName())) {
                            //TODO check only if all plans are checked

                            Tour tour = tourList.get(tourCounter);
                            tour.check();                            //check this tour
                            currentTour = tourList.get(tourCounter + 1).getName();        //current Tour = next tour
                            Log.i("----- ", " ---------------");
                            Log.i("current Tour", " " + tourList.get(tourCounter).getName());
                            Log.i("next Tour", " " + tourList.get(tourCounter + 1).getName());
                            changeNavDToMaintenanceList();
                            setCurrentPlan(maintenanceList.get(1));                       //current plan = first plan
                            //currentPlan = maintenanceList.get(1);
                            main.callpage(0);


                            return;
                        }

                    } else {  //all tours done, start with first again
                        //TODO jump to first?
                        main.beginAgain();
                        Tour tour = tourList.get(tourCounter);
                        tour.check();                            //check this tour


                        currentTour = tourList.get(1).getName();        //current Tour = first
                        changeNavDToMaintenanceList();
                        Log.i("before setCurrentPlan", "");
                        setCurrentPlan(maintenanceList.get(1));         //current plan = first plan
                        //currentPlan = maintenanceList.get(1);

                        main.callpage(0);


                        return;
                    }
                }


            }

        }

    }


    public String getCurrentTour() {
        return currentTour;
    }

    public boolean isChecked() {
        for (Plan plan : planList) {
            if (currentPlan.equals(plan.getMaintenancePlan())) {
                if (plan.check) {
                    return true;
                }

            }

        }
        return false;
    }


    public void savePossibleStat(String maintenancePlan, String workingstep, String possStatLab, String planaddress, String statusAddress) {
        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(maintenancePlan)) {
                plans.savePossibleStat(workingstep, possStatLab, planaddress, statusAddress);

            }
        }

    }


    public void makeToast(String text) {

        long t = System.currentTimeMillis();
        //every 2 sec a new Toast
        if (t - timer > 2000) {
            timer = t;

            if (main.isActive)
                main.makeToast(text);
            if (start.isActive) {
                start.makeToast(text);
            }
        }
    }

    public void addLDwriteList(String text) {
        writeLDList.add(text);
    }


    public void setStatuscheck(Status stat, boolean check) {
        for (Plan plan : planList) {
            if (plan.getMaintenancePlan().equals(currentPlan)) {
                ArrayList<WorkingStep> currentWorkingStepList = plan.getStepList();

                for (WorkingStep workingStep : currentWorkingStepList) {
                    ArrayList<Status> statusList = workingStep.getPossibleStatusList();

                    for (Status status : statusList) {
                        if (status == stat) {
                            stat.setCheck(check);
                        }
                    }

                }
            }
        }
    }


    public ArrayList<Tour> getTourList() {
        return tourList;
    }

    public void setTourList(ArrayList<Tour> tourList) {
        this.tourList = tourList;
    }

    public void changeView(CheckBox cb) {
        try {
            main.changeView(cb);
        } catch (Exception e) {
            Log.i("changeViewError-Con", " " + e.getMessage());
        }

    }


    public boolean getTourCheck(String name) {
        for (Tour tour : tourList) {
            String tourName = tour.getName();
            if (name.equals(tourName)) {
                return tour.check;
            }
        }

        return false;
    }

    public void changeEndpoint(String endpoint) {
        LDA.changeEndpoint(endpoint);

    }

    public void changeUri(String uri) {
        LDA.changeUri(uri);

    }


    public void successLDTest(boolean status) {
        aia.successLDTest(status);
    }


    public void deleteList() {
        tourList = new ArrayList<>();
        navigationDrawerList = new ArrayList<>();
        maintenanceList = new ArrayList<>();
        workingStepStringList = new ArrayList<>();

        currentTour = "";
        currentKennzeichen = "";
        currentPlan = "";
        currentMaintenanceStep = "";

        planList = new ArrayList<Plan>();
    }


    public void writeLD(){

        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(currentPlan)) {
                    ArrayList<WorkingStep> steps = plans.getStepList();
                    for (WorkingStep step : steps) {
                        ArrayList<Status> statusList = step.getPossibleStatusList();
                        LDA.createLD("delete",step.getStepLink(),"","");

                        for(Status stat: statusList) {
                            if (stat.getCheck()) {

                                LDA.createLD("write",stat.getWorkingStep(),stat.getstatusAddress(),"");
                            }
                        }
                    }

            }
        }
    }
}

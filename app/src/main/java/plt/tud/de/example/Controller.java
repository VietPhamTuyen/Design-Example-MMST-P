package plt.tud.de.example;

import android.util.Log;

import java.util.ArrayList;

import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.Tour;
import plt.tud.de.example.model.WorkingStep;

/**
 * Created by Viet on 06.01.2016.
 */
public class Controller {

    // static ArrayList<String> tourList = new ArrayList<String>();
    static ArrayList<Tour> tourList = new ArrayList<>();
    static ArrayList<String> navigationDrawerList = new ArrayList<String>();    //Nav Drawer Tour, C1,C2,C3...
    static ArrayList<String> maintenanceList = new ArrayList<String>();         //plan list for currentTour
    static ArrayList<String> workingStepStringList = new ArrayList<String>();       //Workingsteps for currentPlan

    static String currentTour = "";
    static String currentKennzeichen = "";
    static String currentPlan = "";

    static ArrayList<Plan> planList = new ArrayList<Plan>();                //every single plan, no order
    static LDQueryActivity LDA = new LDQueryActivity();
    static MainActivity main;
    static String currentMaintenanceStep = "";
    static StartActivity start;


    public Controller() {
    }

    public void synch(MainActivity main) {
        this.main = main;
    }

    //TODO start this with createLD("getMaintenancePlan","","",""); anywhere
    public void createLD(String requestedData, String plan, String tourID, String workingStep) {
        LDA.createLD(requestedData, plan, tourID, workingStep);
    }

    /**
     * call in LDQueryActivity if a new item from Linked Data is found
     *
     * @param maintenancePlan
     * @param tourID
     * @param kennzeichen
     */
    public void createPlan(String maintenancePlan, String tourID, String kennzeichen) {
        for (Plan existingPlan : planList) {
            if (existingPlan.getMaintenancePlan() == maintenancePlan) {
                return;
            }
        }
        Plan plans = new Plan(maintenancePlan, tourID, kennzeichen);
        planList.add(plans);
        Tour t = new Tour(plans.getTourID());
        tourList.add(t);
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
     * @param workingLink
     */
    public void saveStep(String maintenancePlan, String tourID, String workingLink) {
        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(maintenancePlan)) {
                Log.i("saveStep", maintenancePlan + " , " + workingLink);
                plans.saveStep(workingLink);
                createLD("getWorkingTitle", maintenancePlan, tourID, workingLink);

            }
        }

    }


    /**
     * 3rd LD request, look for right Plan, save workingLabel (how to do it) to the right workingLink(link for the WorkingStep)
     *
     * @param maintenancePlan
     * @param workingLink
     * @param workinglabel
     */
    public void saveWorkingLabel(String maintenancePlan, String workingLink, String workinglabel) {
        for (Plan plans : planList) {
            if (plans.getMaintenancePlan().equals(maintenancePlan)) {
                plans.saveWorkingLabel(workingLink, workinglabel);

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
        this.currentPlan = currentPlan;
    }

    //TODO delete
    public String getString(int place) {
        Plan plan;
        plan = planList.get(place);
        String stringShow = plan.getString();
        return stringShow;
    }


    /**
     * show a list with all tourIDs  in the navigation drawer
     */
    public void setNavList() {
        navigationDrawerList = new ArrayList<String>();
        navigationDrawerList.add("Tour");
        tourList = new ArrayList<>();



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
            main.setMenuView(navigationDrawerList);
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

        maintenanceList.add("Zurück");

        String plan = listPlan;
        for (Plan p : planList) {
            if (p.getTourID().equals(plan)) {         //add the maintenanceplan if its in the right tour
                maintenanceList.add(p.getMaintenancePlan());
            }
        }
        if (main.isActive) {
            main.setMenuView(maintenanceList);
        }

        if (start.isActive) {
            start.updateListView(maintenanceList);
        }


    }

    public void changeNavDToMaintenanceList() {
        Log.i("changeNavDToM", " ");

        maintenanceList = new ArrayList<String>();
        maintenanceList.add("Zurück");
        String plan = currentTour;
        Log.i("currentTour", " " + currentTour);
        for (Plan p : planList) {
            if (p.getTourID().equals(plan)) {         //add the maintenanceplan if its in the right tour
                maintenanceList.add(p.getMaintenancePlan());
            }
        }

        Log.i("MList size", " " + String.valueOf(maintenanceList.size()));

        if (main.isActive) {
            Log.i("main", "is active ");
            main.setMenuView(maintenanceList);
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


        workingStepStringList.add("Tour: " + currentTour + "| Plan: " + currentPlan + "| " + check);
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
    public boolean checkTour(String tour_check) {
        for (Tour tour : tourList) {
            if (tour.getName().equals(tour_check)) {
                //save currentTour
                Log.i("checkTour", "current Tour: " + tour_check);
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


        start.updateListView(listItem);
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
                    currentPlan = maintenanceList.get(counter + 1);           //current plan = next plan

                    main.callpage(0);
                    return;
                }
            } else {     // all plans done
                for (int tourCounter = 0; tourCounter < tourList.size(); tourCounter++) {
                    if (tourCounter + 1 < tourList.size()) {
                        if (currentTour.equals(tourList.get(tourCounter).getName())) {
                            //TODO check only if all plans are checked
                            tourList.get(tourCounter).check();                            //check this tour
                            currentTour = tourList.get(tourCounter + 1).getName();        //current Tour = next tour
                            Log.i("----- ", " ---------------");
                            Log.i("current Tour", " " + tourList.get(tourCounter).getName());
                            Log.i("next Tour", " " + tourList.get(tourCounter + 1).getName());
                            changeNavDToMaintenanceList();
                            currentPlan = maintenanceList.get(1);
                            main.callpage(0);
                            return;
                        }
                    } else {  //all tours done, start with first again
                        //TODO jump to first?
                        main.beginAgain();
                        tourList.get(tourCounter).check();                            //check this tour


                        currentTour = tourList.get(0).getName();        //current Tour = first
                        changeNavDToMaintenanceList();
                        currentPlan = maintenanceList.get(1);

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


}

package plt.tud.de.example;

import android.util.Log;

import java.util.ArrayList;

import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.WorkingStep;

/**
 * Created by Viet on 06.01.2016.
 */
public class Controller {

    static ArrayList<String> tourList = new ArrayList<String>();
    static ArrayList<String> navigationDrawerList = new ArrayList<String>();
    static ArrayList<String> maintenanceList = new ArrayList<String>();
    static ArrayList<String> workingStepStringList = new ArrayList<String>();

    static ArrayList<Plan> planList = new ArrayList<Plan>();
    static LDQueryActivity LDA = new LDQueryActivity();
    static MainActivity main;
    static String currentMaintenanceStep = "";
    static StartActivity startActivity;

    public Controller() {
    }

    public void synch(MainActivity main) {
        this.main = main;
    }

    //TODO start this with createLD("getMaintenancePlan","","",""); anywhere
    public void createLD(String requestedData, String plan, String tourID, String workingStep) {
        LDA.createLD(requestedData, plan, tourID, workingStep);
    }


    public void createPlan(String maintenancePlan, String tourID, String kennzeichen) {
        for (Plan existingPlan : planList) {
            if (existingPlan.getMaintenancePlan() == maintenancePlan) {
                return;
            }
        }
        Plan plans = new Plan(maintenancePlan, tourID, kennzeichen);
        planList.add(plans);
        Log.i("createPlan", " " + maintenancePlan + " " + tourID);
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
     * 3. LD request, look for right Plan, save workingLabel (how to do it) to the right workingLink(link for the WorkingStep)
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

        try{
            getStartList();
        }catch(Exception e){
            Log.i("error", e.getMessage());
        }
    }


    //TODO call it
    public ArrayList<Plan> getPlanList() {
        return planList;
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
        tourList = new ArrayList<String>();
        for (Plan plan : planList) {
            String tourID = plan.getTourID();
            tourexist:
            {
                //filter existing tours
                for (String tour : tourList) {
                    if (tour.equals(tourID)) {
                        break tourexist;
                    }
                }
                tourList.add(tourID);
                navigationDrawerList.add(tourID);
            }
        }
        main.setMenuView(navigationDrawerList);
    }


    /**
     * show a list with all plans from a certain tour in the navigation drawer
     */
    public void changeNavDToMaintenanceList(int position) {
        position--; // offset from "Zurück"
        maintenanceList = new ArrayList<String>();
        maintenanceList.add("Zurück");
        String plan = tourList.get(position);
        for (Plan p : planList) {
            if (p.getTourID().equals(plan)) {         //add the maintenanceplan if its in the right tour
                maintenanceList.add(p.getMaintenancePlan());
            }
        }
            main.setMenuView(maintenanceList);


    }


    public ArrayList<String> getWorkingSteps(int position) {
        workingStepStringList = new ArrayList<>();


        currentMaintenanceStep = maintenanceList.get(position);
        workingStepStringList.add("Plan: " + currentMaintenanceStep);
        for (Plan p : planList) {
            if (currentMaintenanceStep.equals(p.getMaintenancePlan())) { // if its the right maintenanceStep
                ArrayList<WorkingStep> stepList = p.getStepList();
                for (WorkingStep step : stepList) {                      //then add the working labels to the shown Array
                    workingStepStringList.add(step.getWorkingLabel());
                }
            }

        }
        return workingStepStringList;
    }


    public boolean checkTour(String tour_check) {
        for (String tour : tourList) {
            if (tour.equals(tour_check)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> showCurrentTask() {

        return workingStepStringList;
    }



    public void updateStartActivity(StartActivity startActivity) {
        this.startActivity = startActivity;
        getStartList();
    }

    public void getStartList(){
        ArrayList<String> listItem = new ArrayList<String>();



        listItem = new ArrayList<String>();
        listItem.add("Tour");
        for (Plan plan : planList) {
            String tourID = plan.getTourID();
            tourexist:{
                //filter existing tours
                for (String tour : listItem) {
                    if (tour.equals(tourID)) {
                        break tourexist;
                    }
                }
                listItem.add(tourID);
            }
        }


        startActivity.updateListView(listItem);
    }

}

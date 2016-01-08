package plt.tud.de.example;

import android.util.Log;

import java.util.ArrayList;

import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.WorkingStep;

/**
 * Created by Viet on 06.01.2016.
 */
public class Controller {

    static ArrayList<Plan> planList = new ArrayList<>();
    static LDQueryActivity LDA = new LDQueryActivity();

    public Controller() {

    }


    //TODO start this with createLD("getMaintenancePlan","","",""); anywhere
    public void createLD(String requestedData, String plan, String tourID, String workingStep) {
        LDA.createLD(requestedData, plan, tourID, workingStep);
    }


    public void createPlan(String maintenancePlan, String tourID, String kennzeichen) {
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
    }


    //TODO call it
    public ArrayList<Plan> getPlanList() {
        return planList;
    }


    public String getString(int place) {
        Plan plan;
        plan = planList.get(place);

        String stringShow = plan.getString();


        return stringShow;
    }

}

package plt.tud.de.example.model;

import java.util.ArrayList;

/**
 * Created by Viet on 07.01.2016.
 */
public class Plan {
    ArrayList<WorkingStep> workingStepList = new ArrayList<>();
    String maintenancePlan;
    String tourID;  //e.g. C2
    String kennzeichen;
    public boolean check = false;




    public Plan(String maintenancePlan, String tourID, String kennzeichen){
        this.maintenancePlan = maintenancePlan;
        this.tourID = tourID;
        this.kennzeichen = kennzeichen;
    }


    public void saveStep(String stepLink){
        WorkingStep step = new WorkingStep(stepLink);
        workingStepList.add(step);
    }

    public ArrayList<WorkingStep> getStepList(){
        return workingStepList;

    }

    public String getMaintenancePlan(){
        return maintenancePlan;
    }
    public String getTourID(){
        return tourID;
    }

    /**
     * 3. LD request, save workingLabel (how to do it) to the right workingLink(link for the WorkingStep)
     * @param workingLink
     * @param workinglabel
     */
    public void saveWorkingLabel(String workingLink, String workinglabel){
        for(WorkingStep step : workingStepList){
            if(step.getStepLink().equals(workingLink)){
                step.saveWorkingLabel(workinglabel);
            }
        }

    }

    public String getString(){
        String stringShow= tourID+" || "+maintenancePlan;
        for (WorkingStep step :workingStepList){
            stringShow= stringShow +"\n "+ step.getString();
        }

        return stringShow;
    }

    public String getKennzeichen(){
        return kennzeichen;
    }

    public void check(){
        check = true;
    }




    public void savePossibleStat(String workingstep, String possStatLab, String planaddress, String statusAddress){
        for(WorkingStep step : workingStepList){
            if(step.getStepLink().equals(workingstep)){
                step.savePossibleStatus(possStatLab,planaddress,workingstep, statusAddress);
            }
        }
    }
}

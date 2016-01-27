package plt.tud.de.example.model;

import java.util.ArrayList;

/**
 * Created by Viet on 07.01.2016.
 */
public class WorkingStep {
    String stepLink;
    String workingLabel;
    ArrayList<String> possibleStatusList = new ArrayList<>();



    public WorkingStep(String stepLink ){
        this.stepLink = stepLink;
    }

    public String getStepLink(){
        return stepLink;
    }

    public String getWorkingLabel(){
        return workingLabel;
    }

    public void saveWorkingLabel(String workingLabel){
        this.workingLabel = workingLabel;

    }

    public String getString(){
        //String stringShow = stepLink+", "+ workingLabel; TODO
        String stringShow = workingLabel;
        return stringShow;

    }

    public void savePossibleStatus(String possibleStatus){
        possibleStatusList.add(possibleStatus);
    }

    public ArrayList<String> getPossibleStatusList(){
        return possibleStatusList;
    }


    public void resetPossibleStatusList(){
        possibleStatusList = new ArrayList<>();
    }
}

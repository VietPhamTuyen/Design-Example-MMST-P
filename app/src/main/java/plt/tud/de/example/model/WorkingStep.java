package plt.tud.de.example.model;

/**
 * Created by Viet on 07.01.2016.
 */
public class WorkingStep {
    String stepLink;
    String workingLabel;


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

}

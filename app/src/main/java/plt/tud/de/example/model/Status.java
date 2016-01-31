package plt.tud.de.example.model;

/**
 * Created by Viet on 29.01.2016.
 */
public class Status {
    String statusName;
    String workingStep;
    boolean check = false;

    public Status(String statusName, String workingStep) {
        this.workingStep = workingStep;
        this.statusName = statusName;
    }

    public void setCheck(boolean check){
        this.check = check;
    }

    public boolean getCheck(){
        return check;
    }

    public String getStatusName(){
        return statusName;
    }

    public String getStepName(){
        return workingStep;
    }
}

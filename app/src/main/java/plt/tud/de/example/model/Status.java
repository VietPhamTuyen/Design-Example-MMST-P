package plt.tud.de.example.model;

import android.util.Log;

/**
 * Created by Viet on 29.01.2016.
 */
public class Status {
    String statusName;
    String workingStep;
    String planaddress;
    boolean check = false;

    public Status(String statusName, String workingStep, String planaddress) {
        this.statusName = statusName;
        this.workingStep = workingStep;
        this.planaddress = planaddress;
    }

    public void setCheck(boolean check){
        Log.i("status",statusName+" "+ workingStep);
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

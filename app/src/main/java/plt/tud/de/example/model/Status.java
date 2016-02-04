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
    String workingstep;
    String workingLabel;
    String statusAddress;

    public Status(String statusName,  String planaddress,String workingstep, String workingLabel, String statusAddress) {
        this.statusName = statusName;
        this.workingstep = workingstep;
        this.planaddress = planaddress;
        this.workingLabel = workingLabel;
        this.statusAddress = statusAddress;
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
        return workingLabel;
    }

    public String getWorkingStep(){return workingstep;}

    public String getstatusAddress(){return statusAddress;}
}

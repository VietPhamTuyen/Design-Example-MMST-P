package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;
import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.WorkingStep;

/**
 * Created by Viet on 05.01.2016.
 */
public class Fragment_Task extends Fragment {
    Controller controller = new Controller();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_task, container, false);



        /**
        ArrayList<Plan> planList;
        planList = controll.getPlanList();

        ArrayList<String> listItem = new ArrayList<>();
        listItem.add(String.valueOf(LDDDList.size()));
        int counter= 0;
        for (Plan plan : planList) {
            ArrayList<WorkingStep> workingSteps;
            workingSteps = plan.getStepList();
            listItem.add(plan.getMaintenancePlan()+", ");
            for(WorkingStep step: workingSteps){
                step.getWorkingLabel();
                listItem.add(step.getWorkingLabel());
            }
            counter++;
         }
         */
        Log.i("create frag Task", "task"+ String.valueOf(controller.getPlanList().size()));

        ArrayList<String> listItem = new ArrayList<>();

        for(int counter = 0; counter < controller.getPlanList().size(); counter++){
            listItem.add(controller.getString(counter));
            Log.i("getString", controller.getString(counter));
        }

        ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<>(getContext(), R.layout.list_item, listItem);
        ListView parameterList = (ListView) view.findViewById(R.id.listView_task);

        parameterList.setAdapter(arrayAdapter_list);


        return view;



    //    listView_task
    //    return inflater.inflate(R.layout.fragment_task, container, false);
    }
}

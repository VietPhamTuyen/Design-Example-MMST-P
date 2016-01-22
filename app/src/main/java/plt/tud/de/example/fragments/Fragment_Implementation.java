package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;

/**
 * Created by Viet on 05.01.2016.
 */
public class Fragment_Implementation extends Fragment {
    Controller controller = new Controller();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_implementation, container, false);
        Log.i("create frag Task", "task" + String.valueOf(controller.getPlanList().size()));

        //ArrayList<String> listItem = new ArrayList<>();
        ArrayList<String> listItem = controller.showCurrentImplementation();
        Log.i("stringSize", String.valueOf(listItem.size()));


        ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<>(getContext(), R.layout.list_item, listItem);
        ListView parameterList = (ListView) view.findViewById(R.id.listView_implementation);

        try{
        parameterList.setAdapter(arrayAdapter_list);
    } catch(Exception e){
        Log.i("error fragmentTask", " "+ e.getMessage());
    }

        return view;
    }
}

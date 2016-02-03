package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import plt.tud.de.example.Controller;
import plt.tud.de.example.Adapter.StatusAdapter;
import plt.tud.de.example.R;
import plt.tud.de.example.model.Status;

/**
 * Created by Viet on 05.01.2016.
 */
public class Fragment_Result extends Fragment {
    static Controller controller = new Controller();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_result, container, false);




        View view = inflater.inflate(R.layout.fragment_result, container, false);


        //ArrayList<String> listItem = new ArrayList<>();
        ArrayList<Status> listItem = controller.showCurrentResult();

       // ArrayAdapter<String> arrayAdapter_list = new ArrayAdapter<>(getContext(), R.layout.list_item, listItem);
        ListView parameterList = (ListView) view.findViewById(R.id.listView_result);




        Status[] showList = listItem.toArray(new Status[listItem.size()]);

        StatusAdapter adapter = new StatusAdapter(getActivity(), listItem);
        try {
            parameterList.setAdapter(adapter);
        } catch (Exception e) {
            Log.i("error fragmentTask", " " + e.getMessage());
        }



        return view;










    }
}

package plt.tud.de.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import plt.tud.de.example.R;

/**
 * Created by Viet on 05.01.2016.
 */
public class Fragment_Implementation extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /**
        Button button = (Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                main.set_currentPage(3);
            }
        });
         */

        return inflater.inflate(R.layout.fragment_implementation, container, false);
    }
}

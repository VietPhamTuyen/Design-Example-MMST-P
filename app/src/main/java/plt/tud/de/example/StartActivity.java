package plt.tud.de.example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viet on 05.01.2016.
 */
public class StartActivity extends AppCompatActivity {

    Controller controller = new Controller();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ListView tourList = (ListView) findViewById(R.id.listTour);
        tourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //TODO postion-- ?
                //position--;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("position",String.valueOf(position)); //TODO add listitem
                // i.putExtra("NodeID", selectedDevice);
                // i.putExtra("PositionOnList", selectedNumber);

                startActivity(intent);
            }
        });


        ListView listTour = (ListView)findViewById(R.id.listTour);

        controller.createLD("getMaintenancePlan","","",""); //TODO

        controller.updateStartActivity(this);
    }



    public void  updateListView(ArrayList<String> listItem){
        List<String> newString = listItem;
        String[] inputStringList = newString.toArray(new String[newString.size()]);
        ListView parameterList = (ListView) findViewById(R.id.listTour);
        parameterList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, inputStringList));
    }


    @Override
    public void onBackPressed() {

            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to terminate the connection?").setCancelable(false).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StartActivity.super.onBackPressed();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }



















































    // TODO http://developer.android.com/training/keyboard-input/navigation.html


}

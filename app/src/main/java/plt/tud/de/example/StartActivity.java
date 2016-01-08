package plt.tud.de.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("",""); //TODO add listitem
                // i.putExtra("NodeID", selectedDevice);
                // i.putExtra("PositionOnList", selectedNumber);
                startActivity(intent);
            }
        });


        ListView listTour = (ListView)findViewById(R.id.listTour);

        controller.createLD("getMaintenancePlan","","",""); //TODO


    }

    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                // i.putExtra("NodeID", selectedDevice);
                // i.putExtra("PositionOnList", selectedNumber);
                startActivity(i);

                break;
        }
    }


}

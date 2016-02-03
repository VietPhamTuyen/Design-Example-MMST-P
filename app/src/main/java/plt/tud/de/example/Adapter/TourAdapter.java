package plt.tud.de.example.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import plt.tud.de.example.Controller;
import plt.tud.de.example.R;
import plt.tud.de.example.model.Status;
import plt.tud.de.example.model.Tour;

/**
 * Created by Viet on 01.02.2016.
 */
public class TourAdapter extends ArrayAdapter {
    ArrayList<Tour> statusItem;

    Context context;

    static Controller controller = new Controller();

    public TourAdapter(Context context, ArrayList<Tour> resource) {
        super(context, R.layout.row, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.statusItem = new ArrayList<>();
        this.statusItem.addAll(resource);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.row_nav_drawer, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.textView1);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);

            holder.code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    //change to



                    //controller.changeView(v);
                }
            });


            holder.name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Tour _tour = (Tour) cb.getTag();

                     Log.i("checkbox", "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());

                    // _tour.check();
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Tour tour = statusItem.get(position);

        holder.name.setText(tour.getName());
        //  holder.code.setText(state.getStepName()+"\n"+ state.getStatusName() );
        //  holder.name.setText(state.getStatusName());

        boolean tCheck = controller.getTourCheck(tour.getName());
        holder.name.setChecked(tCheck);

        holder.name.setTag(tour);


        if (tour.getName().equals("Tour")) {
            holder.name.setVisibility(View.INVISIBLE);
            holder.code.setText("Tour");
        } else {

        }

        return convertView;
    }
}



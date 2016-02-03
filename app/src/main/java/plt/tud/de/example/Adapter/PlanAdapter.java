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
import plt.tud.de.example.model.Plan;
import plt.tud.de.example.model.Status;

/**
 * Created by Viet on 01.02.2016.
 */
public class PlanAdapter extends ArrayAdapter {
    ArrayList<Plan> planItem;

    Context context;

    static Controller controller = new Controller();

    public PlanAdapter(Context context, ArrayList<Plan> resource) {
        super(context, R.layout.row, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.planItem = new ArrayList<>();
        this.planItem.addAll(resource);
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

                    if (view.getText().equals("zurueck")) {
                        controller.setNavList();
                    }

                }
            });


            holder.name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Plan _plan = (Plan) cb.getTag();

                    Log.i("clickListener", "" + cb.getText() + " is " + cb.isChecked());
                    // _plan.check();

                    controller.changeView(cb);
                    Log.i("planAdapter", "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());

                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Plan plan = planItem.get(position);

        holder.name.setText(plan.getMaintenancePlan());
        //  holder.code.setText(state.getStepName()+"\n"+ state.getStatusName() );
        //  holder.name.setText(state.getStatusName());

        holder.name.setChecked(plan.check);

        holder.name.setTag(plan);


        if (plan.getTourID().equals("zurueck") && plan.getMaintenancePlan().equals("zurueck")) {
            holder.name.setVisibility(View.INVISIBLE);
            holder.code.setText("zurück");
        } else {

        }


        return convertView;
    }
}



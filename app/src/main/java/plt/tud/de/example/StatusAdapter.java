package plt.tud.de.example;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import plt.tud.de.example.model.Status;

/**
 * Created by Viet on 30.01.2016.
 */
public class StatusAdapter extends ArrayAdapter {
    ArrayList<Status> statusItem ;

    Context context;

    static Controller controller = new Controller();
    public StatusAdapter(Context context, ArrayList<Status> resource) {
        super(context, R.layout.row, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.statusItem = new ArrayList<>();
        this.statusItem.addAll(resource);
    }

    private class ViewHolder
    {
        TextView code;
        CheckBox name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null)
        {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.row, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.textView1);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    Status _state = (Status) cb.getTag();

                    Log.i("checkbox", "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());

                    _state.setCheck((cb.isChecked()));
                }
            });

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Status state = statusItem.get(position);

        holder.name.setText(state.getStepName()+"\n"+ state.getStatusName() );
      //  holder.code.setText(state.getStepName()+"\n"+ state.getStatusName() );
        //  holder.name.setText(state.getStatusName());
        holder.name.setChecked(state.getCheck());

        holder.name.setTag(state);

        return convertView;
    }
}



//Log.i("stat",statusItem[position].getStatusName());
//        controller.setStatuscheck(statusItem[position], true);
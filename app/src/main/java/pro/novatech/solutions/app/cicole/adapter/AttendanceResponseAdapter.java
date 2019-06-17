package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Student.AttendanceResponse;

/**
 * Created by pro on 2018/02/20.
 */
public  class AttendanceResponseAdapter extends ArrayAdapter<AttendanceResponse> {
    Context context;
    int resource;

    public AttendanceResponseAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        AttendanceResponse attendanceResponse = getItem(position);

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater inflater = LayoutInflater.from(parent.getContext());


            convertView = inflater.inflate(resource, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.absent_date = (TextView) convertView.findViewById(R.id.absent_date);
            viewHolder.absent_reason = (TextView) convertView.findViewById(R.id.absent_reason);
            convertView.setTag(viewHolder);//add set tag here

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.absent_date.setText(attendanceResponse.getDate());
        viewHolder.absent_reason.setText(attendanceResponse.getReason());
        return convertView;
          /*
            // Get the data item for this position
            AttendanceResponse attendanceResponse = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }
            // Lookup view for data population
            TextView absent_date = (TextView) convertView.findViewById(R.id.absent_date);
            TextView absent_reason = (TextView) convertView.findViewById(R.id.absent_reason);
            // Populate the data into the template view using the data object
            absent_date.setText(attendanceResponse.getDate());
            absent_reason.setText(attendanceResponse.getReason());
            // Return the completed view to render on screen
            return convertView;
            */
    }
    public  class ViewHolder {
        public  TextView absent_date;
        public TextView absent_reason;
    }
}



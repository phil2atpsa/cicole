package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Teacher.AbsentDaysResponse;

public class StudentsAttendancesAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<AbsentDaysResponse> absentDaysResponses =new ArrayList<>();


    //public constructor
    public StudentsAttendancesAdapter(Context context, ArrayList absentDaysResponses) {
        this.context = context;
        this.absentDaysResponses = absentDaysResponses;
    }


    @Override
    public int getCount() {
        return absentDaysResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return absentDaysResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.single_attendance_row, parent, false);
        }

        AbsentDaysResponse currentItem = (AbsentDaysResponse) getItem(position);

        TextView textViewDate = (TextView)
                convertView.findViewById(R.id.date);
        TextView textViewIReason = (TextView)
                convertView.findViewById(R.id.reason);

        textViewDate.setText(currentItem.getDate());
        textViewIReason.setText(currentItem.getReason());
        // returns the view for the current row
        return convertView;


    }
}
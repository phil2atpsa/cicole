package pro.novatech.solutions.app.cicole.fragment.students;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.Student;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Student.TimeTableResponse;

/**
 * Created by p.lukengu on 4/7/2017.
 */

public class TimeTable  extends ListFragment implements AdapterView.OnItemClickListener   {

    private TimeTableResponseAdapter adapter;
    private TextView no_messages;
    private View titles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.time_table, container, false);
        titles = v.findViewById(R.id.titles);
        Bundle bundle = getArguments();
        StudentResponse studentResponse = (StudentResponse) bundle.getSerializable("student_response");

        adapter = new TimeTableResponseAdapter(getActivity(), R.layout.time_table_row);
        no_messages = (TextView) v.findViewById(R.id.no_messages);

        new Student(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                List< TimeTableResponse> response =   ((  TimeTableResponse) object).getEntries();
                if(response.size() == 0){
                    no_messages.setText("No Entry found");
                    titles.setVisibility(View.GONE);
                } else {
                    no_messages.setVisibility(View.GONE);;
                    titles.setVisibility(View.VISIBLE);
                }
                adapter.addAll( response);
            }

            @Override
            public void onFailure(KelasiApiException e) {

            }
        },true).TimeTable(studentResponse.getStudent_id());



        setListAdapter(adapter);





        return v;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class TimeTableResponseAdapter extends ArrayAdapter<TimeTableResponse> {
        Context context;
        int resource;

        public TimeTableResponseAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.resource = resource;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            TimeTableResponse timeTableResponse = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }
            // Lookup view for data population
            TextView room = (TextView) convertView.findViewById(R.id.room);
            TextView weekday = (TextView) convertView.findViewById(R.id.weekday);

            TextView subject = (TextView) convertView.findViewById(R.id.subject);
          //  TextView classtiming = (TextView) convertView.findViewById(R.id.classtiming);

            TextView start_time = (TextView) convertView.findViewById(R.id.start_time);
            TextView end_time = (TextView) convertView.findViewById(R.id.end_time);
            // Populate the data into the template view using the data object

            room.setText(timeTableResponse.getRoom());
            weekday.setText(timeTableResponse.getWeekday());

            subject.setText(timeTableResponse.getSubject());
          //  classtiming.setText(timeTableResponse.getClasstiming());

            start_time.setText(timeTableResponse.getStart_time());
            end_time.setText(timeTableResponse.getEnd_time());

            // Return the completed view to render on screen
            return convertView;
        }
    }



}

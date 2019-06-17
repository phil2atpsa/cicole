package pro.novatech.solutions.app.cicole.fragment.students;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import pro.novatech.solutions.app.cicole.adapter.AttendanceResponseAdapter;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.AttendanceResponse;
import pro.novatech.solutions.kelasiapi.v1.Student.Student;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/7/2017.
 */

public class AbsentDays extends ListFragment implements AdapterView.OnItemClickListener {


        private AttendanceResponseAdapter adapter;
        private TextView no_messages;
        private View  titles;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);

            View v  =  inflater.inflate(R.layout.absent_days, container, false);
            titles = v.findViewById(R.id.titles);

            Bundle bundle = getArguments();
            StudentResponse studentResponse = (StudentResponse) bundle.getSerializable("student_response");

            adapter = new AttendanceResponseAdapter(getActivity(), R.layout.absent_days_row);

            no_messages = (TextView) v.findViewById(R.id.no_messages);

             new Student(getActivity(), new OnServiceResponseListener() {
                 @Override
                 public void onSuccess(Object object) {
                     List<AttendanceResponse> response =   (( AttendanceResponse) object).getAttendanceList();
                     if(response.size() == 0){
                         no_messages.setText("0 Absent Day");
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
             },true).AbsentDays(studentResponse.getStudent_id(), 2);



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


}

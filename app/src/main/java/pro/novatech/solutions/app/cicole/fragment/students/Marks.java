package pro.novatech.solutions.app.cicole.fragment.students;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pro.novatech.solutions.app.cicole.adapter.MarkListAdapter;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Mark.MarkResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.Student;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;

/**
 * Created by p.lukengu on 4/7/2017.
 */

public class Marks extends Fragment  {

    private MarkListAdapter markListAdapter;
    private ExpandableListView expListView;
    private List<SubjectResponse> listDataHeader = new ArrayList<SubjectResponse>();
    private HashMap<SubjectResponse, List<MarkResponse>> listDataChild = new HashMap<SubjectResponse, List<MarkResponse>>();
    private StudentResponse studentResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.marks, container, false);
       // MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        Bundle bundle = getArguments();
        studentResponse = (StudentResponse) bundle.getSerializable("student_response");
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);


        markListAdapter = new MarkListAdapter(getActivity(), listDataHeader, listDataChild);

        prepareListData();
        // setting list adapter
        expListView.setAdapter(markListAdapter);

        return v;

    }

    public void   prepareListData(){
            new Student(getActivity(), new OnServiceResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    listDataHeader = ((SubjectResponse) object).getSubjectList();
                    for(final SubjectResponse subjectResponse : listDataHeader){
                         new Student(getActivity(), new OnServiceResponseListener() {
                             @Override
                             public void onSuccess(Object object) {
                                 listDataChild.put(subjectResponse, ((MarkResponse) object).getMarkList());
                             }

                             @Override
                             public void onFailure(KelasiApiException e) {

                             }
                         }, true).Mark(studentResponse.getStudent_id(), subjectResponse.getSubject_id());
                    }
                }

                @Override
                public void onFailure(KelasiApiException e) {

                }
            }, true).Subject(studentResponse.getStudent_id());
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

}

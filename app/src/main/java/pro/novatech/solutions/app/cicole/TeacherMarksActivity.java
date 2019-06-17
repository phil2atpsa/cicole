package pro.novatech.solutions.app.cicole;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import org.json.JSONException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.app.cicole.adapter.TeacherMarkListAdapter;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;
import pro.novatech.solutions.kelasiapi.v1.Teacher.TeacherMarksResponse;

public class TeacherMarksActivity extends AppCompatActivity implements PropertyChangeListener {

    private TeacherMarkListAdapter markListAdapter;
    private ExpandableListView expListView;
    private List<TeacherMarksResponse> listData = new ArrayList<TeacherMarksResponse>();
    private StudentResponse studentResponse;
    private UserAccessResponse user;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_marks);
        // MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());



        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(TeacherMarksActivity.this);
        studentResponse = (StudentResponse) bundle.getSerializable("student_response");
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        try {
            user = myPreferenceManager.getUserSession();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            StringBuilder student_name = new StringBuilder("");
            try {
                student_name.append(studentResponse.getFirst_name());
            } catch(NullPointerException e){}

            try {
                student_name.append(" "+studentResponse.getMiddle_name());
            } catch(NullPointerException e){}

            try {
                student_name.append(" "+studentResponse.getLast_name());
            } catch(NullPointerException e){}

            setTitle(student_name);
            prepareListData();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }






    }

    public void   prepareListData() throws UnsupportedEncodingException {
        new Teacher( this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                listData = ((TeacherMarksResponse) object).getTeacherMarksList();
                TeacherMarkListAdapter adapter = new TeacherMarkListAdapter(context, listData);
                adapter.setStudent_id(studentResponse.getStudent_id());
                adapter.setUser(user);
                adapter.addPropertyChangeListener(TeacherMarksActivity.this);

                expListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(KelasiApiException e) {

            }
        }, true).Marks(user.getAuth_key(), studentResponse.getStudent_id());
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
    public void propertyChange(PropertyChangeEvent evt) {

        if( (boolean) evt.getNewValue()) {
            try {
                prepareListData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}

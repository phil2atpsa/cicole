package pro.novatech.solutions.app.cicole;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import pro.novatech.solutions.app.cicole.adapter.AttendanceResponseAdapter;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.SuccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.AttendanceResponse;
import pro.novatech.solutions.kelasiapi.v1.Student.Student;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;

public class StudentAttendanceActivity extends AppCompatActivity {

    protected StudentResponse studentResponse;

    protected SharedPreferences spref;
    private UserAccessResponse user;
    private ListView absent_days;
    private LinearLayout add_form;
    private DatePicker absent_date;
    private Button btn_submit;
    private EditText absent_reason;
    private Handler handler = new Handler();


    private AttendanceResponseAdapter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        absent_days = (ListView) findViewById(R.id.absent_days);
        add_form  = (LinearLayout) findViewById(R.id.add_form);
        absent_date = (DatePicker) findViewById(R.id.absent_date);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        absent_reason = (EditText) findViewById(R.id.absent_reason);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StudentAttendanceActivity.this)
                        .setMessage( getResources().getString(R.string.save_attendance_narrative))
                        .setTitle("Confirm")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("student_id", studentResponse.getStudent_id());
                                            params.put("date",
                                                    absent_date.getYear()+"-"+ (absent_date.getMonth() + 1) +"-"+
                                                            absent_date.getDayOfMonth());
                                            params.put("reason", absent_reason.getText());

                                            try {
                                                new Teacher( StudentAttendanceActivity.this, new OnServiceResponseListener() {
                                                    @Override
                                                    public void onSuccess(Object object) {



                                                        Toast.makeText(StudentAttendanceActivity.this,
                                                                ((SuccessResponse) object).getMessage() ,Toast.LENGTH_LONG).show();
                                                        fillAbsentDays();
                                                    }

                                                    @Override
                                                    public void onFailure(KelasiApiException e) {
                                                        Toast.makeText(StudentAttendanceActivity.this,
                                                                e.getMessage(),Toast.LENGTH_LONG).show();
                                                    }

                                                }, true).Attendance(params, user.getAuth_key());
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {



                                    }
                                }).show();

            }
        });




        //item.setIcon(getResources().getDrawable(R.drawable.ic_refresh_white_24dp));



        //page_toolbar.setMenu(builder);

        setSupportActionBar(toolbar);


        final MyPreferenceManager myPreferenceManager = new MyPreferenceManager(this);

        Bundle bundle = getIntent().getExtras();
        studentResponse = (StudentResponse) bundle.getSerializable("student_response");
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        absent_date.setMaxDate(System.currentTimeMillis());


        try {
            user =  myPreferenceManager.getUserSession();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        fillAbsentDays();

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
    }


    private void fillAbsentDays() {

        adapter = new AttendanceResponseAdapter(
                StudentAttendanceActivity.this, R.layout.absent_days_row);

        new Student(StudentAttendanceActivity.this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                List<AttendanceResponse> response =   (( AttendanceResponse) object).getAttendanceList();
                adapter.addAll( response);

            }

            @Override
            public void onFailure(KelasiApiException e) {

            }
        },true).AbsentDays(studentResponse.getStudent_id(), 2);


        absent_days.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.students_attendances, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.grid_item_absent) {
            add_form.setVisibility(View.VISIBLE);
            return super.onOptionsItemSelected(item);
        }
        return false;
    }


}

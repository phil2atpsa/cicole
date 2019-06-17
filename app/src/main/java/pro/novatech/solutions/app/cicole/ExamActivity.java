package pro.novatech.solutions.app.cicole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.adapter.ExamDataAdapter;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Exams.ExamsResponse;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;

public class ExamActivity extends AppCompatActivity {


    private ArrayList<ExamsResponse> examsResponse = new ArrayList<ExamsResponse>();
    private UserAccessResponse user;
    private RecyclerView recyclerView;
    private TextView no_students;
    private CardView card;
    private MenuItem searchItem;
    private ExamDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Bundle b = getIntent().getExtras();

        SubjectResponse subject = (SubjectResponse) b.getSerializable("subject_response");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(ExamActivity.this);
        try {
            user = myPreferenceManager.getUserSession();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(subject.getName()+"( "+subject.getCode()+" )");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        card = (CardView)  findViewById(R.id.card);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ExamActivity.this);
        recyclerView.setLayoutManager(layoutManager);



        new Teacher(ExamActivity.this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                examsResponse.clear();
                examsResponse.addAll(((ExamsResponse) object).getExamsList());
                adapter = new ExamDataAdapter(examsResponse, ExamActivity.this);

                recyclerView.setAdapter(adapter);
                // searchItem.setVisible(studentResponses.size() != 0);




                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector( ExamActivity.this, new GestureDetector.SimpleOnGestureListener() {

                        @Override public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                    });
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

             /*   View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);


                    Bundle bundle = new Bundle();

                    bundle.putInt("message_id", position);
                    bundle.putString("messages", messagesString);
                    bundle.putBoolean("is_new", false);
                    getActivity().startActivity(new Intent(getActivity(), TextMessageActivity.class).putExtras(bundle));


                }*/

                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }



                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });

            }

            @Override
            public void onFailure(KelasiApiException e) {
                card.setVisibility(View.VISIBLE);
                no_students.setText(e.getMessage());
            }
        }, true).Exams(user.getAuth_key(), subject.getSubject_id());

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
        /*if (id == R.id.add_exam) {


            return super.onOptionsItemSelected(item);
        }*/
        return false;
    }
}

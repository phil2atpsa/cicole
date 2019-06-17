package pro.novatech.solutions.app.cicole.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.adapter.SubjectDataAdapter;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;

/**
 * Created by p.lukengu on 4/8/2017.
 */

public class Subjects extends Fragment  {

    private   ArrayList<SubjectResponse> subjectResponses = new ArrayList<SubjectResponse>();

    private UserAccessResponse user;
    private RecyclerView recyclerView;
    private TextView no_subjects;
    private CardView card;
    private MenuItem searchItem;
    private SubjectDataAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.subject_list, container, false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        card = (CardView)  v.findViewById(R.id.card);
        no_subjects = (TextView) v.findViewById(R.id.no_subjects);

        initViews();
        return v;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            initViews();
            return super.onOptionsItemSelected(item);
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
       // super.onCreateOptionsMenu(menu, inflater);


        /*
             MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
         */
        //inflater.inflate(R.menu.fragment_students, menu);
       // menu.findItem(R.id.action_settings).setVisible(false);

    }

    private  void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        new Teacher(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                subjectResponses.clear();
                subjectResponses.addAll(((SubjectResponse) object).getSubjectList());
                adapter = new SubjectDataAdapter(subjectResponses, getActivity());

                recyclerView.setAdapter(adapter);
                // searchItem.setVisible(studentResponses.size() != 0);
                if(subjectResponses.size() == 0){
                    card.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else{
                    card.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }




                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector( getActivity(), new GestureDetector.SimpleOnGestureListener() {

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
                no_subjects.setText(e.getMessage());
            }
        }, true).Subjects(user.getAuth_key());



    }




}

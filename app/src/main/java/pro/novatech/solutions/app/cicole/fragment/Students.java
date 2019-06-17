package pro.novatech.solutions.app.cicole.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.MainActivity;
import pro.novatech.solutions.app.cicole.adapter.StudentDataAdapter;
import pro.novatech.solutions.app.cicole.adapter.ViewPagerAdapter;
import pro.novatech.solutions.app.cicole.fragment.students.AbsentDays;
import pro.novatech.solutions.app.cicole.fragment.students.Fees;
import pro.novatech.solutions.app.cicole.fragment.students.Marks;
import pro.novatech.solutions.app.cicole.fragment.students.TimeTable;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Guardian.Guardian;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/8/2017.
 */

public class Students extends Fragment  {
    private ArrayList<StudentResponse> studentResponses = new ArrayList<StudentResponse>();
    private UserAccessResponse user;
    private RecyclerView recyclerView;
    private TextView no_students;
    private CardView card;

    private StudentDataAdapter adapter;
    private StudentResponse studentResponse;

    private TabLayout tabs;
    private ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  =  inflater.inflate(R.layout.student_list, container, false);
        setHasOptionsMenu(true);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        card = (CardView) v.findViewById(R.id.card);
        no_students = (TextView) v.findViewById(R.id.no_students);
        tabs = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.viewpager);
        Bundle b = getArguments();

        if(b != null && b.containsKey("student_response")) {
            card.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tabs.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);



            SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //int menuId = spref.getInt("student_menu", R.id.grid_item_absent);

            //  display(R.id.grid_item_absent);

       //     Toast.makeText(getActivity(), ((StudentResponse) b.getSerializable("student_response")).ToJSONObjectString(), Toast.LENGTH_LONG).show();


            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            Fragment fragment  = new AbsentDays();
            fragment.setArguments(b);
            adapter.addFragment(fragment, getString(R.string.grid_item_absent));

            fragment  = new Fees();
            fragment.setArguments(b);
            adapter.addFragment(fragment, getString(R.string.grid_item_fees));

            fragment  = new Marks();
            fragment.setArguments(b);
            adapter.addFragment(fragment, getString(R.string.grid_item_marks));



            fragment  = new TimeTable();
            fragment.setArguments(b);
            adapter.addFragment(fragment, getString(R.string.grid_item_timetable));

            viewPager.setAdapter(adapter);
            tabs.setupWithViewPager(viewPager);

            if(spref.contains("student_tab")){
                viewPager.setCurrentItem(spref.getInt("student_tab", 1));

            }

        } else {

            card.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);

            initViews();
        }




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
         MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
         */
        inflater.inflate(R.menu.fragment_students, menu);
       // if(((MainActivity)getActivity()).getArguments().containsKey("student_response")){
          //  menu.findItem(R.id.action_refresh).setVisible(false);
        //}
       // menu.findItem(R.id.action_settings).setVisible(false);

    }

    private  void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        new Guardian(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                studentResponses.clear();
                studentResponses.addAll(((StudentResponse) object).getStudentsList());
                adapter = new StudentDataAdapter(studentResponses, getActivity());

                recyclerView.setAdapter(adapter);
                // searchItem.setVisible(studentResponses.size() != 0);
                if(studentResponses.size() == 0){
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
                no_students.setText(e.getMessage());
            }
        }, true).Students(user.getAuth_key());



    }




}

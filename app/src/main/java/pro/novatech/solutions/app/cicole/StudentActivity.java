package pro.novatech.solutions.app.cicole;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.json.JSONException;

import pro.novatech.solutions.app.cicole.adapter.ViewPagerAdapter;
import pro.novatech.solutions.app.cicole.fragment.students.AbsentDays;
import pro.novatech.solutions.app.cicole.fragment.students.Fees;
import pro.novatech.solutions.app.cicole.fragment.students.Marks;
import pro.novatech.solutions.app.cicole.fragment.students.TimeTable;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
public class StudentActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FrameLayout content_frame;
    private MyPreferenceManager myPreferenceManager;
    private Toolbar page_toolbar;
    private   StudentResponse studentResponse;
    private  SharedPreferences spref;
    private UserAccessResponse user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);







        //item.setIcon(getResources().getDrawable(R.drawable.ic_refresh_white_24dp));



        //page_toolbar.setMenu(builder);

        setSupportActionBar(toolbar);


        final MyPreferenceManager myPreferenceManager = new MyPreferenceManager(this);

        Bundle bundle = getIntent().getExtras();
        studentResponse = (StudentResponse) bundle.getSerializable("student_response");
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            user =  myPreferenceManager.getUserSession();
            getSupportActionBar().setDisplayHomeAsUpEnabled(!"Student".equals(user.getAssignment()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        int menuId = spref.getInt("student_menu", R.id.grid_item_absent);

      //  display(R.id.grid_item_absent);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment fragment  = new AbsentDays();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.grid_item_absent));

        fragment  = new Fees();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.grid_item_fees));

        fragment  = new Marks();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.grid_item_marks));

       /* fragment  = new Report();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.grid_item_report));
        */

        fragment  = new TimeTable();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.grid_item_timetable));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(spref.contains("student_tab")){
            viewPager.setCurrentItem(spref.getInt("student_tab", 1));

        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.students, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement



       /* if(id == R.id.cart){
            startActivity(new Intent(Main.this, ShoppingCart.class));
        }*/

        // return super.onOptionsItemSelected(item);
        return false;
    }


}

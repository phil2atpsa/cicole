package pro.novatech.solutions.app.cicole;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;

import java.util.List;

import pro.novatech.solutions.app.cicole.adapter.ViewPagerAdapter;
import pro.novatech.solutions.app.cicole.fragment.Events;
import pro.novatech.solutions.app.cicole.fragment.Home;
import pro.novatech.solutions.app.cicole.fragment.News;
import pro.novatech.solutions.app.cicole.fragment.Profile;
import pro.novatech.solutions.app.cicole.fragment.Students;
import pro.novatech.solutions.app.cicole.fragment.StudentsAttendance;
import pro.novatech.solutions.app.cicole.fragment.Subjects;
import pro.novatech.solutions.app.cicole.fragment.TeacherMarks;
import pro.novatech.solutions.app.cicole.fragment.events.Upcomings;
import pro.novatech.solutions.app.cicole.fragment.notifications.AllMessages;
import pro.novatech.solutions.app.cicole.fragment.notifications.SentMessages;
import pro.novatech.solutions.app.cicole.fragment.notifications.callbacks.OnMessageCountListener;
import pro.novatech.solutions.app.cicole.fragment.notifications.service.IntentScheduler;
import pro.novatech.solutions.app.cicole.helper.DeviceUtils;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Guardian.Guardian;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccess;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMessageCountListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyPreferenceManager myPreferenceManager;
    private UserAccessResponse user;
    private TextView notification_badge_target;
    private Bundle bundle;
    private NavigationView navigationView;

    private FloatingActionButton fab;

    private SharedPreferences spref;
    private MenuItem reresh_menu;
    private MenuItem search_menu;
    private ViewPagerAdapter adapter;

    private int Menu_ID;
    private TextView userView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         IntentScheduler intentScheduler = new IntentScheduler(this);
         intentScheduler.scheduleMessageCount();


        spref = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setTitle(getString(R.string.app_name));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Click to send message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
              startActivity(new Intent(MainActivity.this, SendMessageActivity.class));

            }
        });
        fab.setVisibility(View.GONE);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
       // setupViewPager(viewPager);


       // tabLayout.setupWithViewPager(viewPager);
       /* tabLayout.getTabAt(0).setIcon(getDrawable(R.drawable.ic_attach_money_black_24dp));
        tabLayout.getTabAt(1).setIcon(getDrawable(R.drawable.ic_assignment_ind_black_24dp));
        tabLayout.getTabAt(2).setIcon(getDrawable(R.drawable.ic_photo_library_black_24dp));*/
        myPreferenceManager = new MyPreferenceManager(MainActivity.this);

        try {
            user = myPreferenceManager.getUserSession();
            //myPreferenceManager.clearCorrespondents();

         //   if(!myPreferenceManager.hasCorrespondents()){
                if(DeviceUtils.getInstance().DeviceIsConnected()) {
                   // Toast.makeText(this,user.getAssignment(), Toast.LENGTH_LONG ).show();

                    if ("Guardian".equals(user.getAssignment())) {
                        new Guardian(this, new OnServiceResponseListener() {
                            @Override
                            public void onSuccess(Object object) {
                                // System.out.println(((UserAccessResponse) object).ToJSONArrayString());
                                System.out.println(((UserAccessResponse) object).ToJSONArrayString());
                                myPreferenceManager.setCorrespondents(((UserAccessResponse) object).ToJSONArrayString());
                            }

                            @Override
                            public void onFailure(KelasiApiException e) {
                                //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }, false).Correspondents(user.getAuth_key());

                    }
                    if ("Teacher".equals(user.getAssignment())) {
                        new Teacher(this, new OnServiceResponseListener() {
                            @Override
                            public void onSuccess(Object object) {
                                // System.out.println(((UserAccessResponse) object).ToJSONArrayString());
                               // System.out.println(((UserAccessResponse) object).ToJSONArrayString());
                              //  Toast.makeText(getApplicationContext(),((UserAccessResponse)  object).ToJSONArrayString(), Toast.LENGTH_LONG ).show();
                                myPreferenceManager.setCorrespondents(((UserAccessResponse) object).ToJSONArrayString());
                            }

                            @Override
                            public void onFailure(KelasiApiException e) {
                                //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }, false).Correspondents(user.getAuth_key());

                    }
                }

           // }




           // showUi("home", getString(R.string.menu_home), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpDrawerMenu(navigationView);
        navigationView.setNavigationItemSelectedListener(this);

       // setBadge();


        View headerLayout = navigationView.getHeaderView(0);
        userView  = (TextView ) headerLayout.findViewById(R.id.userView);
        //ImageView imageView =(ImageView) headerLayout.findViewById(R.id.profile_image);
        //  onNavigationItemSelected(navigationView.getMenu().getItem(1));

        if("Student".equals(user.getAssignment())) {
            new UserAccess(this, new OnServiceResponseListener() {
                @Override
                public void onSuccess(Object object) {
                   // Bundle b = new Bundle();

                    //b.putSerializable("student_response", (StudentResponse) object);
                    //startActivity(new Intent(MainActivity.this, StudentActivity.class).putExtras(b));
                    StringBuilder student_name = new StringBuilder("");
                    try {
                        student_name.append(((StudentResponse) object).getFirst_name());
                    } catch(NullPointerException e){}

                    try {
                        student_name.append(" "+((StudentResponse) object).getMiddle_name());
                    } catch(NullPointerException e){}

                    try {
                        student_name.append(" "+((StudentResponse) object).getLast_name());
                    } catch(NullPointerException e){}

                    userView.setText(Html.fromHtml("<b>"+student_name+"</b>"));



                }

                @Override
                public void onFailure(KelasiApiException e) {
                    //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }, false).Student(user.getAuth_key());


        } else {
            StringBuilder name  = new StringBuilder("");
            if(user.getName() != null){
                name.append("<b>"+user.getName()+"</b>");
            }
            if(user.getSurname() != null){
                name.append("&nbsp;<b>"+user.getSurname());
            }
            if(user.getEmail() != null){
                //name.append("</b><br/>"+user.getEmail());
            }

            userView.setText(Html.fromHtml(name.toString()));

        }

        bundle = getIntent().getExtras();

        NavigationHistory();
    }

    private void NavigationHistory() {
        //Default
        // ((NavigationMenuView)navigationView.getChildAt(1)).setPadding(0, -60, 0, 0);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("menu")) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putInt("menu", b.getInt("menu"))
                    .putInt("selected_tab", b.getInt("selected_tab"))
                    .commit();
        }


        int menuId = spref.getInt("menu", R.id.nav_home);
        MenuItem menuItem;
        if(menuId != R.id.nav_logout )  {
            menuItem = navigationView.getMenu().findItem(menuId);
        } else{
            menuItem = navigationView.getMenu().findItem(R.id.nav_home);
        }
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);



    }



    public void setupViewPager(ViewPager viewPager, String UI, Bundle args) {
       // ViewPagerAdapter adapter = null;
        //viewPager.removeAllViews();

        //viewPager.removeAllViews();

        FragmentManager fragmentManager = getSupportFragmentManager();

        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (Fragment f : fragments) {
                //You can perform additional check to remove some (not all) fragments
                if(f != null) {
                    ft.remove(f);
                }

            }
            ft.commitNow();
        }
        adapter = new ViewPagerAdapter(fragmentManager);



       // final ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        switch(UI){
            case "messages":

                adapter.addFragment(new AllMessages(), getString(R.string.inbox));
                adapter.addFragment(new SentMessages(), getString(R.string.outbox));
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch(position){
                            case 0:
                                ((AllMessages)  adapter.getItem(position)).refresh();
                                break;
                            case 1:
                                ((SentMessages)  adapter.getItem(position)).refresh();
                                break;

                        }


                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });



               // tabLayout.getTabAt(0).setIcon(getDrawable(R.drawable.ic_inbox_white_24dp));
               // tabLayout.getTabAt(1).setIcon(getDrawable(R.drawable.ic_call_missed_outgoing_white_24dp));
                //tabLayout.setVisibility(View.VISIBLE);
                break;
            case "events":

              //  ViewPagerAdapter adapter1 = new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFragment(new Upcomings(), getString(R.string.upcoming));
              //  adapter.addFragment(new MonthlyView(), getString(R.string.monthly_view));

                viewPager.setAdapter(adapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch(position){
                            case 0:
                                ((Upcomings)  adapter.getItem(position)).refresh();
                                break;
                          /*  case 1:
                                ((MonthlyView)  adapter.getItem(position)).refresh();
                                break;
                           */
                        }


                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setIcon(getDrawable(R.drawable.ic_view_list_white_24dp));
                //tabLayout.getTabAt(1).setIcon(getDrawable(R.drawable.ic_view_week_black_24dp));
                tabLayout.setVisibility(View.VISIBLE);
                break;



        }

        //ft.commit();

    }
    private void setUpDrawerMenu( NavigationView navigationView) {

        if("Guardian".equals(user.getAssignment())){
            navigationView.inflateMenu(R.menu.guardian_drawer);
        }
        if("Teacher".equals(user.getAssignment())){
            navigationView.inflateMenu(R.menu.teacher_drawer);
        }

        if("Student".equals(user.getAssignment())){
            navigationView.inflateMenu(R.menu.students_drawer);
        }

        if(!"Student".equals(user.getAssignment())){
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_notification);
            View v = item.getActionView();
            notification_badge_target =  (TextView) v.findViewById(R.id.actionbar_notifcation_textview);
            notification_badge_target.setVisibility(View.GONE);
        }

        //item.setVisible(false);


       /* MenuItem item = navigationView.getMenu().findItem(R.id.nav_notification);
        MenuItemCompat.setActionView(item, R.layout.notification_badge);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);*/

      ///  notification_badge_target = (TextView) notificaitons.findViewById(R.id.actionbar_notifcation_textview);




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected  void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


       /* if(id == R.id.cart){
            startActivity(new Intent(Main.this, ShoppingCart.class));
        }*/

        // return super.onOptionsItemSelected(item);
        return false;
    }


    public void  onNavigationItemSelected(@NonNull int menuId) {
        MenuItem menuItem = navigationView.getMenu().findItem(menuId);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id;

        if(item == null){
            id = R.id.nav_home;
        } else {
            id = item.getItemId();
        }
       // refresh.setVisible(false);
        fab.setVisibility(View.GONE);

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putInt("menu",id).commit();

        switch(id) {
            case R.id.nav_home:
                showUi("home", getString(R.string.menu_home),null);
                break;
            case R.id.nav_profile:
                showUi("profile", getString(R.string.menu_profile),null);
                break;
           /* case R.id.grid_item_exam:
                showUi("exams", getString(R.string.menu_exams),null);
                break;*/

            case R.id.nav_absent:
                showUi("attendances", getString(R.string.menu_students_attendance),null);
                break;

            case R.id.nav_notification:
               // refresh.setVisible(true);
                fab.setVisibility(View.VISIBLE);
               // showUi("notifications", getString(R.string.nav_item_notifications), null);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.menu_notification));
                }
               // tabLayout = (TabLayout) findViewById(R.id.tabs);
                Bundle bundle = getIntent().getExtras();
                if(bundle != null && bundle.containsKey("message")){


                    MessageResponse messageResponse = (MessageResponse) bundle.getSerializable("message");
                    bundle = new Bundle();
                    bundle.putSerializable("messageResponse", messageResponse);
                    if(messageResponse.getIs_read()  == 0) {
                        startActivity(new Intent(MainActivity.this, TextMessageActivity.class).putExtras(bundle));
                    }
                }

                setupViewPager(viewPager,"messages", null);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("selected_tab", position).commit();

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                findViewById(R.id.content_frame).setVisibility(View.GONE);
                viewPager.setCurrentItem(0, true);

                break;
            case R.id.nav_students:
                fab.setVisibility(View.GONE);
                if("Student".equals(user.getAssignment())) {
                    new UserAccess(this, new OnServiceResponseListener() {
                        @Override
                        public void onSuccess(Object object) {

                            Bundle b =new Bundle();
                            StudentResponse studentResponse = (StudentResponse) object;
                            b.putSerializable("student_response", studentResponse);
                            // startActivity(new Intent(MainActivity.this, StudentActivity.class).putExtras(b));
                            StringBuilder name  = new StringBuilder("");
                            try {
                                name.append(studentResponse.getFirst_name());
                            } catch(NullPointerException e)
                            {}
                            try {
                                name.append(" "+studentResponse.getMiddle_name());
                            } catch(NullPointerException e)
                            {}
                            try {
                                name.append(" "+studentResponse.getLast_name());
                            } catch(NullPointerException e)
                            {}



                            showUi("students",  name.toString(), b);
                        }

                        @Override
                        public void onFailure(KelasiApiException e) {
                            //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }, false).Student(user.getAuth_key());


                } else {

                    showUi("students", getString(R.string.student_profile), null);
                }
                break;
            case R.id.nav_events:
               // showUi("events", getString(R.string.menu_e vents), null);

                fab.setVisibility(View.GONE);
                //showUi("notifications", getString(R.string.nav_item_notifications), null);

               if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.menu_events));
                }
                showUi("events", getString(R.string.grid_item_events), null);
                // tabLayout = (TabLayout) findViewById(R.id.tabs);

               /* setupViewPager(viewPager,"events", null);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("ev_selected_tab", position).commit();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                findViewById(R.id.content_frame).setVisibility(View.GONE);
                viewPager.setCurrentItem(spref.getInt("ev_selected_tab",0), true);
                */

                break;
            case R.id.nav_news:
                showUi("news", getString(R.string.grid_item_news), null);
                break;
            /*case R.id.nav_marks:
                showUi("marks", getString(R.string.grid_item_marks), null);
                break;*/
            case R.id.nav_logout:
                myPreferenceManager.terminateUserSession();
                myPreferenceManager.clearCorrespondents();

                spref.edit().remove("menu").commit();
                spref.edit().remove("selected_tabs").commit();
                spref.edit().remove("ev_selected_tab").commit();
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showUi(String UI, String title, final Bundle bundle){
        Fragment fragment = null;

        switch(UI){
            case "home":
                fragment = new Home();
                break;
            case "marks":
                fragment = new TeacherMarks();
                break;
            case "profile":
                fragment = new Profile();
                break;
            case "exams":
                fragment = new Subjects();
                break;

            case "attendances":
                fragment = new StudentsAttendance();
                break;
            case "students":
                fragment = new Students();

                break;
            case "news":
                fragment = new News();
                break;
            case "events":
                fragment = new Events();
                break;

        }

        if(bundle != null) {
            fragment.setArguments(bundle);
        }
        if (fragment != null) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }


    @Override
    public void OnMessageCount(int n) {

        BadgeView badgeView = new BadgeView(MainActivity.this, notification_badge_target);
        badgeView.setText(String.valueOf(n));
        if(n > 0) {
            badgeView.show();
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mediaPlayer.start();
        } else{
            badgeView.hide();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            } else

                Menu_ID = spref.getInt("menu", R.id.nav_home);






                if(Menu_ID ==  R.id.nav_home ) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(
                                    "Are you sure you want to exit the application?")
                            .setTitle("Confirm")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            finish();
                                        }
                                    })
                            .setNegativeButton(android.R.string.no,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                        }
                                    }).show();
                } else {

                    MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_home) ;
                    onNavigationItemSelected(menuItem);
                    menuItem.setChecked(true);

                }
            // finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

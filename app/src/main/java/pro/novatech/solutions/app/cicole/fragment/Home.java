package pro.novatech.solutions.app.cicole.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import pro.novatech.solutions.app.cicole.MainActivity;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;

/**
 * Created by p.lukengu on 4/4/2017.
 */

public class Home extends Fragment {

    private GridView home_grid;
    private UserAccessResponse user;
    private SharedPreferences spref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v  =  inflater.inflate(R.layout.home_fragment, container, false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());



       // UserAccessResponse userAccessResponse = myPreferenceManager.getUserSession();


        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }






        home_grid = (GridView)  v.findViewById(R.id.home_grid);
        if("Guardian".equals(user.getAssignment())) {
            home_grid.setAdapter(new GridAdapter(getActivity()));
            home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position){
                        case 0:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",0).commit();
                            break;
                        case 1:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_events);
                            break;
                        case 2:
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",1).commit();
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            break;
                        case 3:

                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_news);
                            break;
                        case 4:
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",2).commit();
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);

                            break;
                        case 5:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            break;
                        case 6:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            //spref.edit().putInt("student_tab",4).apply();
                           break;
                       /* case 7:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            break;
                        case 8:
                            new Guardian(getActivity(), new OnServiceResponseListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    List<StudentResponse> students =((StudentResponse) object).getStudentsList();
                                    if( students.size() == 1) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("student_response", students.get(0));
                                        bundle.putInt("student_tab", 4);
                                        getActivity().startActivity(new Intent(getActivity(), StudentActivity.class).putExtras(bundle));

                                    } else {
                                        ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                                    }
                                }

                                @Override
                                public void onFailure(KelasiApiException e) {

                                }
                            }, true).Students(user.getAuth_key());
                            break;*/

                    }
                }
            });
        }
        if("Teacher".equals(user.getAssignment())) {
            home_grid.setAdapter(new TeacherGridAdapter(getActivity()));
            home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_absent);
                           break;
                     /*   case 1:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_marks);
                            break;

                        case 2:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.grid_item_exam);
                            break;*/

                        case 1:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_events);
                            break;
                        case 2:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_news);
                            break;
                        case 3:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_notification);
                            break;
                    }

                }
            });
        }


        if("Student".equals(user.getAssignment())) {
            home_grid.setAdapter(new StudentGridAdapter(getActivity()));
            home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position){
                        case 0:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",0).commit();
                            break;
                        case 1:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_events);
                            break;
                        case 2:
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",1).commit();
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            break;
                        case 3:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_news);
                            break;
                        case 4:
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",2).commit();
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);

                            break;
                        /*case 5:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            break;*/
                        case 5:
                            ((MainActivity) getActivity()).onNavigationItemSelected(R.id.nav_students);
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("student_tab",3).commit();
                            break;


                    }
                }
            });
        }





        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
       // menu.findItem(R.id.cart).setVisible(false);
        //menu.findItem(R.id.search).setVisible(false);

    }

    public class TeacherGridAdapter extends BaseAdapter {

        private  LayoutInflater _inflater;



        private  Context mContext;
        private  String[] item_name;
        private final int[] images ={
                R.drawable.ic_assessment_24dp,
                //R.drawable.ic_assignment_turned_in_24dp,
                //R.drawable.ic_event_note_24dp,
                R.drawable.ic_event_black_24dp,
                R.drawable.ic_news_black_24dp,
                R.drawable.ic_mail_black_24dp

        };

        public TeacherGridAdapter(Context c) {
            mContext = c;
            _inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item_name = getResources().getStringArray(R.array.grid_teacher_menu);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            int item = (int) getItem(position);
            return buildView(view, item, position);
        }


        public View buildView(View view, int item , int position) {
            ViewHolder holder = new ViewHolder();
            if(view == null){
                view = _inflater.inflate(R.layout.home_grid, null);
                holder.menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.menu_icon.setImageResource(item);
            holder.name.setText(item_name[position]);



            return view;
        }
    }




    public class StudentGridAdapter extends BaseAdapter {

        private  LayoutInflater _inflater;

        private  Context mContext;
        private  String[] item_name;
        private final int[] images ={
                R.drawable.icon_absent,
                //R.drawable.icon_awards,
                R.drawable.icon_events,
                R.drawable.icon_fees,
                R.drawable.icon_news,
                R.drawable.icon_marks,
                //R.drawable.icon_report,
               // R.drawable.icon_student,
                R.drawable.icon_timetable};

        public StudentGridAdapter(Context c) {
            mContext = c;
            _inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item_name = getResources().getStringArray(R.array.grid_student_menu);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            int item = (int) getItem(position);
            return buildView(view, item, position);
        }


        public View buildView(View view, int item , int position) {
            ViewHolder holder = new ViewHolder();
            if(view == null){
                view = _inflater.inflate(R.layout.home_grid, null);
                holder.menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.menu_icon.setImageResource(item);
            holder.name.setText(item_name[position]);
            return view;
        }
    }



    public class GridAdapter extends BaseAdapter {

        private  LayoutInflater _inflater;

        private  Context mContext;
        private  String[] item_name;
        private final int[] images ={
                R.drawable.icon_absent,
                //R.drawable.icon_awards,
                R.drawable.icon_events,
                R.drawable.icon_fees,
                R.drawable.icon_news,
                R.drawable.icon_marks,
                //R.drawable.icon_report,
                R.drawable.icon_student,
                R.drawable.icon_timetable};


        public GridAdapter(Context c) {
            mContext = c;
            _inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item_name = getResources().getStringArray(R.array.grid_menu);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
             int item = (int) getItem(position);
            return buildView(view, item, position);
        }


        public View buildView(View view, int item , int position) {
            ViewHolder holder = new ViewHolder();
            if(view == null){
                view = _inflater.inflate(R.layout.home_grid, null);
                holder.menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.menu_icon.setImageResource(item);
            holder.name.setText(item_name[position]);



            return view;
        }
    }

    static class ViewHolder {
        public ImageView menu_icon;
        public TextView name;


    }
    
}

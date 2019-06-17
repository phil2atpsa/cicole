package pro.novatech.solutions.app.cicole.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import pro.novatech.solutions.app.cicole.helper.DateParser;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccess;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class Profile extends Fragment {

    private TextView mobile_number,profile_surname, profile_name, profile_email,profile_role,profile_created_at;
    private MyPreferenceManager myPreferenceManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.profile_fragment, container, false);

        myPreferenceManager =  new MyPreferenceManager(getActivity());
        profile_surname = (TextView) v.findViewById(R.id.profile_surname);
        profile_name = (TextView) v.findViewById(R.id.profile_name);
        profile_email = (TextView) v.findViewById(R.id.profile_email);
        profile_role  = (TextView) v.findViewById(R.id.profile_role);
        profile_created_at  = (TextView) v.findViewById(R.id.profile_created_at);
        mobile_number = v.findViewById(R.id.phone_number);
        try {
            profile_role.setText(myPreferenceManager.getUserSession().getAssignment());
            long timestamp = Long.parseLong(myPreferenceManager.getUserSession().getCreated_at()) * 1000L;
            profile_created_at.setText(DateParser.getDate(timestamp));
            mobile_number.setText(myPreferenceManager.getUserSession().getMobile_phone());


            if(!"Student".equals( myPreferenceManager.getUserSession().getAssignment())) {
              //  profile_surname.setVisibility(View.VISIBLE);
               // profile_email.setVisibility(View.VISIBLE);
                profile_name.setText(myPreferenceManager.getUserSession().getName());
                profile_surname.setText(myPreferenceManager.getUserSession().getSurname());
                profile_email.setText(myPreferenceManager.getUserSession().getEmail());



            } else {

                new UserAccess(getActivity(), new OnServiceResponseListener() {

                    @Override
                    public void onSuccess(Object object) {

                        StringBuilder student_name = new StringBuilder("");
                       // profile_surname.setVisibility(View.GONE);
                       // profile_email.setVisibility(View.GONE);

                        try {
                            student_name.append(((StudentResponse) object).getFirst_name());

                        } catch (NullPointerException e) {
                        }

                        try {
                            student_name.append(" " + ((StudentResponse) object).getMiddle_name());
                        } catch (NullPointerException e) {
                        }

                        try {
                            student_name.append(" " + ((StudentResponse) object).getLast_name());
                        } catch (NullPointerException e) {
                        }
                        profile_name.setText(student_name);
                    }

                    @Override
                    public void onFailure(KelasiApiException e) {

                    }
                }, false).Student(myPreferenceManager.getUserSession().getAuth_key());


            }

        } catch (JSONException e) {
            e.printStackTrace();
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

}

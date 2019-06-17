package pro.novatech.solutions.app.cicole.fragment.students;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;

/**
 * Created by p.lukengu on 4/8/2017.
 */

public class Report extends Fragment  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.report, container, false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());





        return v;

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

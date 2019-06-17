package pro.novatech.solutions.app.cicole.fragment.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;

import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Events.EventResponse;
import pro.novatech.solutions.kelasiapi.v1.Institution.Institution;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;


/**
 * Created by p.lukengu on 4/8/2017.
 */

public class MonthlyView extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendarwidget;
    private UserAccessResponse user;
    private TextView event_details;
    private static final java.text.DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        View v  =  inflater.inflate(R.layout.monthly_view, container, false);
        event_details = (TextView) v.findViewById(R.id.event_details);

        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        calendarwidget = ((MaterialCalendarView)v.findViewById(R.id.calendarView));

        calendarwidget.setOnDateChangedListener(this);
        calendarwidget.setOnMonthChangedListener(this);




        refresh();
        return v;

    }
    public void refresh(){
        if (getActivity()!=null) {
            new Institution(getActivity(), new OnServiceResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    List<EventResponse> events = ((EventResponse) object).getEventList();
                    // for(EventResponse ev : events){
                    EventResponse ev = events.get(0);
                    String date = ev.getDate();
                    String[] dates = date.split("-");
                    CalendarDay calendarDay = CalendarDay.from(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
                    //  Toast.makeText(getActivity(),calendarDay.toString(), Toast.LENGTH_LONG).show();

                    calendarwidget.setCurrentDate(calendarDay);
                    String html = "<h2>" + ev.getTitle() + "</h2>";
                    html += "<p style='text-align:justify'>" + ev.getDescription() + "</p>";
                    html += "<p><i>" + ev.getDate() + "&nbsp;&nbsp;" + ev.getStart_time() + " - " + ev.getEnd_time() + "</i><p>";

                    event_details.setText(Html.fromHtml(html));


                    //}

                }

                @Override
                public void onFailure(KelasiApiException e) {

                }
            }, true).Events(user.getAuth_key());

        }

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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}

package pro.novatech.solutions.app.cicole.fragment.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.adapter.EventDataAdapter;
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

public class Upcomings extends Fragment {

    private ArrayList<EventResponse> events = new ArrayList<EventResponse>();
    private UserAccessResponse user;
    private RecyclerView recyclerView;
    private TextView no_messages;
    private CardView card;
    private EventDataAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        View v  =  inflater.inflate(R.layout.upcoming, container,false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());


        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        card = (CardView)  v.findViewById(R.id.card);
        no_messages = (TextView) v.findViewById(R.id.no_messages);
        no_messages.setText( getActivity().getString( R.string.no_new_messages ));
        no_messages.setVisibility(View.GONE);
        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);



        no_messages.setText(getString(R.string.no_upcoming_events));
        card.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        refresh();

        return v;



    }

    public void refresh(){
      //  if(DeviceUtils.getInstance().DeviceIsConnected()) {
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);



            new Institution(getActivity(), new OnServiceResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    events.clear();
                    events.addAll(((EventResponse) object).getEventList());
                    adapter = new EventDataAdapter(events, getActivity());

                    recyclerView.setAdapter(adapter);

                    if(events.size() == 0){
                        no_messages.setText(getString(R.string.no_new_messages));
                        card.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    } else{
                        card.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                    }

                }

                @Override
                public void onFailure(KelasiApiException e) {
                    no_messages.setText(e.getMessage());
                    card.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }, true).Events(user.getAuth_key());

       /*} else {
            no_messages.setText(getString(R.string.internet_required));
            card.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }*/

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

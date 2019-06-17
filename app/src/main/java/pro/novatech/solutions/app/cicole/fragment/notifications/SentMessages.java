package pro.novatech.solutions.app.cicole.fragment.notifications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.adapter.MessageDataAdapter;
import pro.novatech.solutions.app.cicole.fragment.notifications.callbacks.OnDeletePerformedListener;
import pro.novatech.solutions.app.cicole.helper.DeviceUtils;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class SentMessages extends Fragment implements SearchView.OnQueryTextListener {

    private ArrayList<MessageResponse> messages = new ArrayList<MessageResponse>();
    private UserAccessResponse user;
    private RecyclerView recyclerView;

    private TextView no_messages;
    private CardView card;
    private  MenuItem searchItem;
    private  MessageDataAdapter adapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.all_messages, container, false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        card = (CardView)  v.findViewById(R.id.card);
        no_messages = (TextView) v.findViewById(R.id.no_messages);
        no_messages.setText( getActivity().getString( R.string.no_messages ));
        no_messages.setVisibility(View.GONE);
        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        no_messages.setText(getString(R.string.no_messages));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        if(DeviceUtils.getInstance().DeviceIsConnected()) {
            initViews();
        } else {
            no_messages.setText(getString(R.string.internet_required));
            card.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }



        return v;

    }

    private void initViews() {
        if (getActivity()!=null) {

            new pro.novatech.solutions.kelasiapi.v1.Notification.Message(getActivity(), new OnServiceResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    messages.clear();
                    messages.addAll(((MessageResponse) object).getMessageList());
                    adapter = new MessageDataAdapter(messages, getActivity(), new OnDeletePerformedListener() {
                        @Override
                        public void onSuccess(boolean success) {
                            initViews();
                        }
                    }, true);
                    recyclerView.setAdapter(adapter);

                    if(searchItem != null)
                        searchItem.setVisible(messages.size() != 0);


                    if (messages.size() == 0) {
                        card.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                        //searchItem.setVisible(false);
                    } else {
                        card.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        // searchItem.setVisible(true);
                    }

                }

                @Override
                public void onFailure(KelasiApiException e) {
                    no_messages.setText(e.getMessage());
                    card.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);


                }
            }, true).SentMessages(user.getAuth_key());
        }

    }

    public void refresh(){
        initViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);


       /*
        menu.findItem(R.id.action_settings).setVisible(false);
        inflater.inflate(R.menu.messages, menu);



        searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo( getActivity().getComponentName()));
            searchView.setOnQueryTextListener(this);
        }
        searchItem.setVisible(false);
        */

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
    public boolean onQueryTextSubmit(String query) {
        adapter.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.search(newText);
        return false;
    }
}

package pro.novatech.solutions.app.cicole.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.adapter.NewsDataAdapter;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Institution.Institution;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.News.NewsResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/13/2017.
 */

public class News  extends Fragment {
    private ArrayList<NewsResponse> newsResponses = new ArrayList<NewsResponse>();
    private UserAccessResponse user;
    private RecyclerView recyclerView;
    private NewsDataAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.news, container, false);
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(getActivity());
        try {
            user = myPreferenceManager.getUserSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        newsResponses.clear();

        new Institution(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {

                newsResponses.addAll(((NewsResponse) object).getNews());
                adapter = new NewsDataAdapter(newsResponses, getActivity());
                recyclerView.setAdapter(adapter);
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

            }
        }, true).News(user.getAuth_key());
        return v;

    }
}

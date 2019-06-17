package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Events.EventResponse;

/**
 * Created by p.lukengu on 4/11/2017.
 */

public class EventDataAdapter extends RecyclerView.Adapter<EventDataAdapter.ViewHolder>  {

    private ArrayList<EventResponse> events;
    private Context context;
    private  ArrayList<EventResponse> internal = new ArrayList<>();




    public EventDataAdapter(ArrayList<EventResponse> events, Context context) {
        this.events = events;
        this.context = context;
        internal.addAll(events);


    }

    public void search(String query){
        events.clear();
        if (query.length() == 0) {
            events.addAll(internal);

        }
        for(EventResponse eventResponse : internal){

            if(eventResponse.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    eventResponse.getDescription().toLowerCase().contains(query.toLowerCase())){
                events.add(eventResponse);
            }
        }

        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EventResponse eventResponse = events.get(position);



        holder.event_title.setText(eventResponse.getTitle());
        holder.event_description.setText(Html.fromHtml("<p style='text-align:justify'>"+eventResponse.getDescription()+"</p>"));
        String html  = eventResponse.getDate();
        html += "<br />"+eventResponse.getStart_time().concat(" - ").concat(eventResponse.getEnd_time());

        holder.event_date.setText(Html.fromHtml(html));





    }

    @Override
    public int getItemCount() {
        return events.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {
        private TextView event_title, event_date,event_description;

        public ViewHolder(View view) {
            super(view);

            event_title = (TextView)view.findViewById(R.id.event_title);
            event_date = (TextView)view.findViewById(R.id.event_date);
            event_description = (TextView)view.findViewById(R.id.event_description);


            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           /* menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Read");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Reply");
            menu.add(0, v.getId(), 0, "Delete");*/
        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

package pro.novatech.solutions.app.cicole.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.TextMessageActivity;
import pro.novatech.solutions.app.cicole.fragment.notifications.callbacks.OnDeletePerformedListener;
import pro.novatech.solutions.app.cicole.helper.DateParser;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.ViewHolder>  {

    private ArrayList<MessageResponse> messages;
    private Context context;
    private  ArrayList<MessageResponse> internal = new ArrayList<>();
    private OnDeletePerformedListener mCallback;
    private  boolean sent = false;



    public MessageDataAdapter(ArrayList<MessageResponse> messages,
                              Context context, OnDeletePerformedListener mCallback, boolean sent ) {
        this.messages = messages;
        this.context = context;
        internal.addAll(messages);
        this.mCallback = mCallback;
        this.sent = sent;

    }

    public void search(String query){
        messages.clear();
        if (query.length() == 0) {
            messages.addAll(internal);

        }
        for(MessageResponse messageResponse : internal){

            if(messageResponse.getMessage().toLowerCase().contains(query.toLowerCase()) ||
                    messageResponse.getSubject().toLowerCase().contains(query.toLowerCase())  ){
                messages.add(messageResponse);
            }
        }

        notifyDataSetChanged();
    }



    @Override
    public MessageDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageDataAdapter.ViewHolder holder, int position) {

       // holder.message_title.setText(messages.get(position).getSubject());
        holder.message_date.setText(DateParser.getDate( Long.parseLong(messages.get(position).getCreated_at())));
        holder.message_text.setText(messages.get(position).getMessage());

        holder.sender.setText(sent ? messages.get(position).getReceiver() : messages.get(position).getSender());

        if(messages.get(position).getIs_read() == 0){
         //   holder.message_title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.message_date.setTypeface( Typeface.DEFAULT_BOLD);
            holder.message_text.setTypeface( Typeface.DEFAULT_BOLD);
            holder.sender.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
           // holder.message_title.setTypeface(Typeface.DEFAULT);
            holder.message_date.setTypeface( Typeface.DEFAULT);
            holder.message_text.setTypeface( Typeface.DEFAULT);
            holder.sender.setTypeface(Typeface.DEFAULT);
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {
        private TextView  message_title, message_date,message_text,sender;
        private ImageView cancel_button;
        public ViewHolder(View view) {
            super(view);

            //message_title = (TextView)view.findViewById(R.id.message_title);
            message_date = (TextView)view.findViewById(R.id.message_date);
            message_text = (TextView)view.findViewById(R.id.message_text);
            sender = (TextView)view.findViewById(R.id.sender);
            cancel_button =(ImageView) view.findViewById(R.id.cancel_button);
            cancel_button.setOnClickListener(this);
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
            if (v.getId() == cancel_button.getId()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle(context.getString(R.string.confirmation));
                alertDialogBuilder
                        .setMessage(context.getString(R.string.message_deletion_msg))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                try {
                                    MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
                                    UserAccessResponse userAccessResponse = myPreferenceManager.getUserSession();
                                    MessageResponse messageResponse = messages.get(getAdapterPosition());
                                    new pro.novatech.solutions.kelasiapi.v1.Notification.Message(context, new OnServiceResponseListener() {
                                        @Override
                                        public void onSuccess(Object object) {
                                            mCallback.onSuccess(true);
                                            Toast.makeText(context, context.getString(R.string.message_deletion_confirmation),Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(KelasiApiException e) {
                                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                                        }
                                    }, true).RemoveMessage(userAccessResponse.getAuth_key(), new String[]{String.valueOf(messageResponse.getMessage_id())});

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        })
                        .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            } else {

                Bundle bundle = new Bundle();
                bundle.putSerializable("messageResponse", messages.get(getAdapterPosition()));
                bundle.putBoolean("sent", sent);
                context.startActivity(new Intent(context, TextMessageActivity.class).putExtras(bundle));



            }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

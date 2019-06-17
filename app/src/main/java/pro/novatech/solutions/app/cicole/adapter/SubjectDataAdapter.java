package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.ExamActivity;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class SubjectDataAdapter extends RecyclerView.Adapter<SubjectDataAdapter.ViewHolder>  {

    private ArrayList<SubjectResponse> subjects;
    private Context context;
    private  ArrayList<SubjectResponse> internal = new ArrayList<>();




    public SubjectDataAdapter(ArrayList<SubjectResponse> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
        internal.addAll(subjects);


    }

    public void search(String query){
        subjects.clear();
        if (query.length() == 0) {
            subjects.addAll(internal);

        }
        for(SubjectResponse subjectResponse : internal){

            if(subjectResponse.getName().toLowerCase().contains(query.toLowerCase()) ){
                subjects.add(subjectResponse);
            }
        }

        notifyDataSetChanged();
    }



    @Override
    public SubjectDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectDataAdapter.ViewHolder holder, int position) {

        SubjectResponse subjectResponse = subjects.get(position);
        holder.subject_name.setText(subjectResponse.getName().concat("( ").concat(subjectResponse.getCode())
        .concat(" )"));




    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {
        private TextView  subject_name;

        public ViewHolder(View view) {
            super(view);

            subject_name = (TextView)view.findViewById(R.id.subject_name);

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
            Bundle b = new Bundle();
            b.putSerializable("subject_response",subjects.get(getAdapterPosition()));
           context.startActivity(new Intent(context, ExamActivity.class).putExtras(b));
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

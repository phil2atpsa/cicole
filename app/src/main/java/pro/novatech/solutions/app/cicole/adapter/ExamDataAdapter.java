package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Exams.ExamsResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class ExamDataAdapter extends RecyclerView.Adapter<ExamDataAdapter.ViewHolder>  {

    private ArrayList<ExamsResponse> exams;
    private Context context;
    private  ArrayList<ExamsResponse> internal = new ArrayList<>();




    public ExamDataAdapter(ArrayList<ExamsResponse> exams, Context context) {
        this.exams = exams;
        this.context = context;
        internal.addAll(exams);


    }

    public void search(String query){

    }



    @Override
    public ExamDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exams, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamDataAdapter.ViewHolder holder, int position) {

        ExamsResponse examsResponse = exams.get(position);
        holder.exam_name.setText(examsResponse.getExam_name());
        holder.exam_group_name.setText(examsResponse.getExam_group_name());
        holder.exam_date.setText(examsResponse.getExam_date());

/*        holder.start_time.setText(examsResponse.getStart_time());
        holder.end_time.setText(examsResponse.getEnd_time());
        holder.maximum_marks.setText(Double.toString(examsResponse.getMaximum_marks()));
        holder.minimum_marks.setText(Double.toString(examsResponse.getMinimum_marks()));*/
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {
        private TextView  exam_name, exam_group_name, exam_date, start_time,end_time,maximum_marks, minimum_marks;

        public ViewHolder(View view) {
            super(view);

            exam_name = (TextView)view.findViewById(R.id.exam_name);
            exam_group_name = (TextView)view.findViewById(R.id.exam_group_name);
            exam_date = (TextView)view.findViewById(R.id.exam_date);


            start_time = (TextView)view.findViewById(R.id.start_time);
            end_time = (TextView)view.findViewById(R.id.end_time);
            maximum_marks = (TextView)view.findViewById(R.id.maximum_marks);
            minimum_marks = (TextView)view.findViewById(R.id.minimum_marks);


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


        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

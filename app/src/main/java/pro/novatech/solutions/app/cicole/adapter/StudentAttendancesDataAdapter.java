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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.StudentAttendanceActivity;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class StudentAttendancesDataAdapter extends RecyclerView.Adapter<StudentAttendancesDataAdapter.ViewHolder>  {

    private ArrayList<StudentResponse> students;
    private Context context;
    private  ArrayList<StudentResponse> internal = new ArrayList<>();




    public StudentAttendancesDataAdapter(ArrayList<StudentResponse> students, Context context) {
        this.students = students;
        this.context = context;
        internal.addAll(students);


    }

    public void search(String query){
        students.clear();
        if (query.length() == 0) {
            students.addAll(internal);

        }
        for(StudentResponse studentResponse : internal){

            if(studentResponse.getFirst_name().toLowerCase().contains(query.toLowerCase()) ||
                    studentResponse.getLast_name().toLowerCase().contains(query.toLowerCase()) ||
                    studentResponse.getInstitution().toLowerCase().contains(query.toLowerCase()) ||
                    studentResponse.getGrade().toLowerCase().contains(query.toLowerCase())){
                students.add(studentResponse);
            }
        }

        notifyDataSetChanged();
    }



    @Override
    public StudentAttendancesDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAttendancesDataAdapter.ViewHolder holder,
                                 int position) {

        StudentResponse studentResponse = students.get(position);

        StringBuilder student_name = new StringBuilder("");
        try {
            student_name.append(studentResponse.getFirst_name());
        } catch(NullPointerException e){}

        try {
            student_name.append(" "+studentResponse.getMiddle_name());
        } catch(NullPointerException e){}

        try {
            student_name.append(" "+studentResponse.getLast_name());
        } catch(NullPointerException e){}

        //Log.e("NAME","NAME : >>>>>>"+student_name.toString());

        holder.name.setText(student_name.toString());
        holder.institution.setText(studentResponse.getInstitution());

        holder.grade.setText(studentResponse.getGrade());

        try {
            JSONArray absent_days = new JSONArray(studentResponse.getAbsent_days());
            holder.absent_days.setText(absent_days.length() +" "+ context.getString(R.string.grid_item_absent));

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return students.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {

        public TextView  name, institution,grade,absent_days;

        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.student_name);
            institution = (TextView)view.findViewById(R.id.institution);
            grade = (TextView)view.findViewById(R.id.grade);
            absent_days = (TextView)view.findViewById(R.id.absent_days);


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
            b.putSerializable("student_response",students.get(getAdapterPosition()));
            context.startActivity(new Intent(context,
                    StudentAttendanceActivity.class).putExtras(b));
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

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

import pro.novatech.solutions.app.cicole.StudentActivity;
import pro.novatech.solutions.app.cicole.TeacherMarksActivity;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class StudentDataAdapter extends RecyclerView.Adapter<StudentDataAdapter.ViewHolder>  {

    private ArrayList<StudentResponse> students;
    private Context context;
    private  ArrayList<StudentResponse> internal = new ArrayList<>();

    private boolean  teacherView = false;





    public StudentDataAdapter(ArrayList<StudentResponse> students, Context context) {
        this.students = students;
        this.context = context;
        internal.addAll(students);


    }

    public void isTeacherView(boolean teacherView){
        this.teacherView = teacherView;

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
    public StudentDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row, parent, false);
        return new StudentDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentDataAdapter.ViewHolder holder, int position) {

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


        holder.name.setText(student_name.toString());
        holder.institution.setText(studentResponse.getInstitution());

        holder.grade.setText(studentResponse.getGrade());


    }

    @Override
    public int getItemCount() {
        return students.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener, View.OnLongClickListener {
        public TextView  name, institution,grade;

        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.student_name);
            institution = (TextView)view.findViewById(R.id.institution);
            grade = (TextView)view.findViewById(R.id.grade);


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
            if(!teacherView) {
                context.startActivity(new Intent(context, StudentActivity.class).putExtras(b));
            } else {
                context.startActivity(new Intent(context, TeacherMarksActivity.class).putExtras(b));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

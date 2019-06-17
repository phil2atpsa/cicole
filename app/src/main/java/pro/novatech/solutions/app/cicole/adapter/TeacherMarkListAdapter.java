package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.SuccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Teacher.Teacher;
import pro.novatech.solutions.kelasiapi.v1.Teacher.TeacherMarksResponse;

/**
 * Created by p.lukengu on 4/13/2017.
 */

public class TeacherMarkListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<TeacherMarksResponse> teacherMarksResponse;
    private List<Subject> _listDataHeader = new ArrayList<>();
    private HashMap<Subject, List<ExamMarks>> _listDataChild = new HashMap<>();
    private int student_id;
    private UserAccessResponse user;
    private boolean mark_saved;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);



    public TeacherMarkListAdapter(Context context, List<TeacherMarksResponse> teacherMarksResponse) {

        this._context = context;
        this.teacherMarksResponse = teacherMarksResponse;
        try {
            buildList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //this._listDataHeader = listDataHeader;
        //this._listDataChild = listChildData;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;

    }
    public void setUser( UserAccessResponse user) {
        this.user = user;

    }

    protected void buildList() throws JSONException {

      for(TeacherMarksResponse t : teacherMarksResponse){
            Subject subject = new Subject();
            subject.setSubject_code(t.getSubject_code());
            subject.setSubject_id(t.getSubject_id());
            subject.setSubject_name(t.getSubject_name());
          _listDataHeader.add(subject);
          JSONArray marks = new JSONArray(t.getExam_marks());
          List<ExamMarks> mark_list = new ArrayList<>();

          for(int i = 0; i < marks.length(); i++ ) {
              ExamMarks examMarks = new ExamMarks();
              JSONObject obj = marks.getJSONObject(i);
              examMarks.setExam_id(obj.optInt("exam_id"));
              examMarks.setInstitution_id(obj.optInt("institution_id"));
              examMarks.setLabel(obj.optString("label"));
              examMarks.setLevel_id(obj.optInt("level_id"));

              examMarks.setMarks(obj.get("marks") != null ? obj.optDouble("marks") : 0);

              examMarks.setMaximum_marks(obj.optDouble("maximum_marks"));
              examMarks.setName(obj.optString("name"));
              mark_list.add(examMarks);
          }
          _listDataChild.put(subject, mark_list);
      }

    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Subject subject = (Subject) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.mark_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(subject.getSubject_name().concat("(").concat(subject.getSubject_code()).concat(")"));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExamMarks examMarks = (ExamMarks) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.teacher_mark_list_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView label = (TextView) convertView.findViewById(R.id.label);
        final EditText mark = (EditText) convertView.findViewById(R.id.mark);
        ImageButton btn_submit = (ImageButton) convertView.findViewById(R.id.btn_submit);

        name.setText(examMarks.getName());
        label.setText(examMarks.getLabel());

        String value = String.valueOf(examMarks.getMarks());

        if(!Double.isNaN(examMarks.getMarks())) {
            mark.setText(String.valueOf(examMarks.getMarks()));
            mark.setEnabled(false);
            btn_submit.setEnabled(false);
            btn_submit.setVisibility(View.GONE);
        }  else {
            mark.setText("");
            mark.setEnabled(true);
            btn_submit.setVisibility(View.VISIBLE);
            btn_submit.setEnabled(true);
        }

        btn_submit.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if( mark.getText().toString().equals("")){
                    Toast.makeText(_context, _context.getResources().getString(R.string.mark_please), Toast.LENGTH_LONG).show();
                } else {

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("student_id", student_id);
                        obj.put("exam_id", examMarks.getExam_id());
                        obj.put("marks", Double.valueOf(mark.getText().toString()));

                        new Teacher(_context, new OnServiceResponseListener() {
                            @Override
                            public void onSuccess(Object object) {
                                SuccessResponse successResponse = (SuccessResponse) object;
                                Toast.makeText(_context, successResponse.getMessage(), Toast.LENGTH_LONG).show();
                                pcs.firePropertyChange("mark_saved", false, true);
                            }

                            @Override
                            public void onFailure(KelasiApiException e) {

                            }
                        }, true).Mark(obj, user.getAuth_key());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                }
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }


    class Subject {
        private int subject_id;
        private String subject_name;
        private String subject_code;

        public int getSubject_id() {
            return subject_id;
        }

        public void setSubject_id(int subject_id) {
            this.subject_id = subject_id;
        }

        public String getSubject_name() {
            return subject_name;
        }

        public void setSubject_name(String subject_name) {
            this.subject_name = subject_name;
        }

        public String getSubject_code() {
            return subject_code;
        }

        public void setSubject_code(String subject_code) {
            this.subject_code = subject_code;
        }
    }

    class ExamMarks{
        private String name;
        private String label;
        private double marks;
        private int exam_id;
        private double maximum_marks;
        private int institution_id;
        private int level_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getMarks() {
            return marks;
        }

        public void setMarks(double marks) {
            this.marks = marks;
        }

        public int getExam_id() {
            return exam_id;
        }

        public void setExam_id(int exam_id) {
            this.exam_id = exam_id;
        }

        public double getMaximum_marks() {
            return maximum_marks;
        }

        public void setMaximum_marks(double maximum_marks) {
            this.maximum_marks = maximum_marks;
        }

        public int getInstitution_id() {
            return institution_id;
        }

        public void setInstitution_id(int institution_id) {
            this.institution_id = institution_id;
        }

        public int getLevel_id() {
            return level_id;
        }

        public void setLevel_id(int level_id) {
            this.level_id = level_id;
        }
    }
}

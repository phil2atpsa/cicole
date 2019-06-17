package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.Mark.MarkResponse;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;

/**
 * Created by p.lukengu on 4/13/2017.
 */

public class MarkListAdapter  extends BaseExpandableListAdapter {

    private Context _context;
    private List<SubjectResponse> _listDataHeader;
    private HashMap<SubjectResponse, List<MarkResponse>> _listDataChild;

    public MarkListAdapter(Context context, List<SubjectResponse> listDataHeader,
                                 HashMap<SubjectResponse, List<MarkResponse>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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
        SubjectResponse subjectResponse = (SubjectResponse) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.mark_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(subjectResponse.getName().concat("(").concat(subjectResponse.getCode()).concat(")"));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MarkResponse markResponse = (MarkResponse) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.mark_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        StringBuilder mark = new StringBuilder(markResponse.getName().toLowerCase())
                .append(" (")
                .append(markResponse.getLabel())
                .append(" )")
                .append(" ")
                .append(String.valueOf(markResponse.getMarks()))
                .append(" out of ")
                .append(String.valueOf(markResponse.getMaximum_marks()));


        txtListChild.setText(mark.toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

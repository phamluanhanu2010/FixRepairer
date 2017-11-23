package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Sup on 6/23/16.
 */

public class AdtItemSkill extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<VtcModelFields>> listDataChild;

    private List<VtcModelFields> listChoice;
    private HashMap<String, VtcModelFields> listChoiceItem;

    public AdtItemSkill(Context context, List<VtcModelFields> listChoice) {
        this.context = context;
        this.listChoice = listChoice;
        listChoiceItem = new HashMap<>();
    }

    public void setData(List<String> listDataHeader, HashMap<String, List<VtcModelFields>> listChildData) {
        if (listChildData != null && listDataHeader != null) {
            for (String str : listDataHeader) {
                List<VtcModelFields> lstTmp = listChildData.get(str);
                if(lstTmp != null) {
                    for (VtcModelFields modelTmp : lstTmp) {
                        if (listChoice != null) {
                            for (VtcModelFields strChoice : listChoice) {
                                if (strChoice.getId().equals(modelTmp.getId())) {
                                    modelTmp.setChoice(true);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (listChoice != null) {
            for (VtcModelFields strChoice : listChoice) {
                if (strChoice != null) {
                    listChoiceItem.put(strChoice.getId(), strChoice);
                }
            }
        }

        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        notifyDataSetChanged();
    }


    @Override
    public VtcModelFields getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final VtcModelFields childText = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.tmp_skill_content, null);
        }

        final TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        final CheckBox cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);

        tv_title.setText(childText.getName());
        cb_check.setChecked(childText.isChoice());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_check.isChecked()) {
                    cb_check.setChecked(false);
                } else {
                    cb_check.setChecked(true);
                }

                initChoiceItem(cb_check.isChecked(), childText);
            }
        });

        cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChoiceItem(cb_check.isChecked(), childText);
            }
        });

        return convertView;
    }

    private void initChoiceItem(boolean isChoice, VtcModelFields child){
        if(listChoiceItem == null){
            listChoiceItem = new HashMap<>();
        }
        if(isChoice){
            listChoiceItem.put(child.getId(), child);
        }else {
            listChoiceItem.remove(child.getId());
        }
        child.setChoice(isChoice);

    }

    public List<VtcModelFields> getListChoiceItem(){
        if(listChoiceItem == null){
            return null;
        }
        return new ArrayList<>(listChoiceItem.values());
    }

    public void initClearData(){
        listDataHeader = null;
        listDataChild = null;
        listChoice = null;
        listChoiceItem = null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild == null || this.listDataHeader == null) {
            return 0;
        }

        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (this.listDataHeader == null) {
            return null;
        }
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (this.listDataHeader == null) {
            return 0;
        }
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.tmp_skill_group, null);
        }

        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        tv_title.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
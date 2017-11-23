package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Sup on 6/14/16.
 */
public class AdtSuggestLst extends ArrayAdapter<VtcModelTools> {

    private Context context;

    private LayoutInflater mLayoutInflater;

    private onChoiceSuggest onChoiceSuggest;

//    private ArrayList<VtcModelTools> items;
    private ArrayList<VtcModelTools> itemsAll;
    private ArrayList<VtcModelTools> suggestions;

    private int width = 0;
    private int height = 0;

    public AdtSuggestLst.onChoiceSuggest getOnChoiceSuggest() {
        return onChoiceSuggest;
    }

    public void setOnChoiceSuggest(AdtSuggestLst.onChoiceSuggest onChoiceSuggest) {
        this.onChoiceSuggest = onChoiceSuggest;
    }

    public AdtSuggestLst(Context context, ArrayList<VtcModelTools> itemLst, int width) {
        super(context, 0, itemLst);
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.width = width;
        this.height = width;
//        this.items = itemLst;
//        this.itemsAll = (ArrayList<VtcModelTools>) items.clone();
        this.itemsAll = itemLst;
        this.suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.tmp_choose_suggest, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VtcModelTools tools = itemsAll.get(position);

        if (tools != null) {

            viewHolder.check_choice.setChecked(tools.istStatus());

            viewHolder.tv_title.setText(tools.getName());


            ImageLoadAsync loadAsync = new ImageLoadAsync(context, viewHolder.img_icon, width, height);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(tools.gettThumb()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.check_choice.isChecked()) {
                        viewHolder.check_choice.setChecked(false);
//                        tools.settStatus(false);
//                        getOnChoiceSuggest().onClickUnChoice(tools);
                    } else {
                        viewHolder.check_choice.setChecked(true);
//                        tools.settStatus(true);
//                        getOnChoiceSuggest().onClickChoice(tools);
                    }
                }
            });

            viewHolder.check_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tools.settStatus(isChecked);

                    if (isChecked) {
                        getOnChoiceSuggest().onClickChoice(tools);
                    } else {
                        getOnChoiceSuggest().onClickUnChoice(tools);
                    }
                }
            });

        }
        return convertView;
    }


    class ViewHolder {

        private CheckBox check_choice;
        private TextView tv_title;
        private TextView tv_content;
        private ImageView img_icon;

        public ViewHolder(View v) {
            v.setTag(this);

            check_choice = (CheckBox) v.findViewById(R.id.check_choice);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            img_icon = (ImageView) v.findViewById(R.id.img_icon);

        }
    }

    public interface onChoiceSuggest {
        void onClickChoice(VtcModelTools sg);

        void onClickUnChoice(VtcModelTools sg);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((VtcModelTools) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (VtcModelTools customer : itemsAll) {
                    if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<VtcModelTools> filteredList = (ArrayList<VtcModelTools>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (VtcModelTools c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}

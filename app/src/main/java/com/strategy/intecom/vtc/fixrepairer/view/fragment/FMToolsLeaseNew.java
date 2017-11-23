package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtSuggestLst;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtToolsChoicelst;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.custom.listcontent.ListViewWrapContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 5/24/16.
 */
public class FMToolsLeaseNew extends AppCore implements View.OnClickListener{

    private View viewRoot;

    private ListViewWrapContent lv_tools;

    private AdtToolsChoicelst adtToolsChooselst;

    private MultiAutoCompleteTextView edt_search;

    private Button btn_no;
    private Button btn_yes;

    private TextView tv_city_district;

    private AdtSuggestLst adapter;

    private ArrayList<VtcModelTools> toolsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_lease_new, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initGetTools();
        initControler(view);
    }

    private void initGetTools() {
        toolsList = new ArrayList<>();
        if (getVtcToolsParentList() != null && getVtcToolsChildList() != null) {

            List<VtcModelTools> lst = getVtcToolsParentList();

            for (int i = 0; i < lst.size(); i++) {

                VtcModelTools lstTools = lst.get(i);
                if (lstTools != null) {
                    HashMap<String, List<VtcModelTools>> ls = getVtcToolsChildList();
                    List<VtcModelTools> modelTools = ls.get(lstTools.getName());

                    if (modelTools != null) {
                        toolsList.addAll(modelTools);
                    }
                }
            }
        }
    }

    private void initControler(View view){

        lv_tools = (ListViewWrapContent) view.findViewById(R.id.lv_tools);

        edt_search = (MultiAutoCompleteTextView) view.findViewById(R.id.edt_search);

        edt_search.setThreshold(1);

        edt_search.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer() );

        btn_no = (Button) view.findViewById(R.id.btn_no);
        btn_yes = (Button) view.findViewById(R.id.btn_yes);

        tv_city_district = (TextView) view.findViewById(R.id.tv_city_district);

        adtToolsChooselst = new AdtToolsChoicelst(getActivity(), (int) getActivity().getResources().getDimension(R.dimen.size_icon_in_app_menu));

        initEvent();
    }

    private void initEvent(){

        btn_no.setFocusableInTouchMode(true);
        btn_yes.setFocusableInTouchMode(true);
        tv_city_district.setOnClickListener(this);

        initData();
    }

    private void initData(){

        adapter = new AdtSuggestLst(getActivity(), toolsList, (int) getActivity().getResources().getDimension(R.dimen.size_icon_in_app_menu));

        adapter.setOnChoiceSuggest(new AdtSuggestLst.onChoiceSuggest() {
            @Override
            public void onClickChoice(VtcModelTools sg) {
                adtToolsChooselst.setData(sg);
            }

            @Override
            public void onClickUnChoice(VtcModelTools sg) {

            }
        });

        adtToolsChooselst.setOnClickItemTools(new AdtToolsChoicelst.onClickItemTools() {
            @Override
            public void onClickCountTools(VtcModelTools tools, boolean isUp) {

            }

            @Override
            public void onClickDelete(VtcModelTools tools) {

            }
        });

        edt_search.setAdapter(adapter);

        lv_tools.setAdapter(adtToolsChooselst);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_city_district:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_CHOICE_CITY_DISTRICT);
                break;
        }
    }

    @Override
    protected void cmdOnClickChoiceCity(String city, String district) {
        super.cmdOnClickChoiceCity(city, district);
        if(tv_city_district != null){
            tv_city_district.setText(city + ", " + district);
        }
    }
}
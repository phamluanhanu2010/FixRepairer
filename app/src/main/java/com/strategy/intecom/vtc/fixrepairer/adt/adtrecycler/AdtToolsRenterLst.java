package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;

import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtToolsRenterLst extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<VtcModelOrder> orderListMỵJob;

    private int typeDisplay = 1;

    public AdtToolsRenterLst(Context context, int typeDisplay) {
        this.context = context;
        this.typeDisplay = typeDisplay;
    }


    public void initSetData(List<VtcModelOrder> orderListMỵJob){
        this.orderListMỵJob = orderListMỵJob;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_tools_renter_list, parent, false);
            //inflate your layout and pass it to view holder
            VHItem vhItem = new VHItem(v);

            switch (typeDisplay) {

                case Const.TYPE_DISPLAY_JOB_HISTORY:

                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                    vhItem.tv_context.setVisibility(TextView.VISIBLE);
                    vhItem.tv_name.setVisibility(TextView.INVISIBLE);

                    break;

                case Const.TYPE_DISPLAY_JOB_RENTER:

                    vhItem.btn_promo.setVisibility(TextView.GONE);
                    vhItem.tv_context.setVisibility(TextView.GONE);
                    vhItem.tv_name.setVisibility(TextView.VISIBLE);

                    break;
                case Const.TYPE_DISPLAY_JOB_HISTORY_HEADER:

                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                    vhItem.tv_context.setVisibility(TextView.VISIBLE);
                    vhItem.tv_name.setVisibility(TextView.INVISIBLE);

                    break;

            }

            return vhItem;
        } else if (viewType == TYPE_HEADER) {

            switch (typeDisplay) {

                case Const.TYPE_DISPLAY_JOB_HISTORY:

                    View vHistory = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_tools_lease_renter, parent, false);

                    return new VHHeader(vHistory);

                case Const.TYPE_DISPLAY_JOB_RENTER:

                    View vRenter = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_tools_lease_renter, parent, false);

                    return new VHHeader(vRenter);
                case Const.TYPE_DISPLAY_JOB_HISTORY_HEADER:

                    View vHistoryHeader = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_job_finter_list, parent, false);

                    return new VHHeaderHistory(vHistoryHeader);
            }
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            String dataItem = getItem(position);

        } else if (holder instanceof VHHeader) {

        }else if (holder instanceof VHHeaderHistory) {

        }
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private String getItem(int position) {
        return "";
    }

    class VHItem extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_context;
        private TextView btn_promo;
        private TextView tv_name;

        public VHItem(View itemView) {
            super(itemView);
            this.view = itemView;
            tv_context = (TextView) itemView.findViewById(R.id.tv_context);
            btn_promo = (TextView) itemView.findViewById(R.id.btn_promo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        public VHHeader(View itemView) {
            super(itemView);

        }

    }

    class VHHeaderHistory extends RecyclerView.ViewHolder {

        private View view;
        private SearchView edt_search;
        private TextView tv_sort;

        public VHHeaderHistory(View itemView) {
            super(itemView);
            this.view = itemView;
            edt_search = (SearchView) itemView.findViewById(R.id.edt_search);
            tv_sort = (TextView) itemView.findViewById(R.id.tv_sort);
        }

    }
}


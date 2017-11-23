package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtToolsHistoryLst extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public AdtToolsHistoryLst(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_tools_history_list, parent, false);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_job_lst, parent, false);
            //inflate your layout and pass it to view holder
            VHItem vhItem = new VHItem(v);

            return vhItem;
        } else if (viewType == TYPE_HEADER) {

            View vs = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_tools_lease_renter, parent, false);

            //inflate your layout and pass it to view holder
            VHHeader vhHeader = new VHHeader(vs);
            return vhHeader;
        }


        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            String dataItem = getItem(position);
            //cast holder to VHItem and set data
        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
        }
    }

//    @Override
//    public int getItemCount() {
//        return data.length + 1;
//    }


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

        public VHItem(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the normal items
                    AppCore.showLog("----------------VHItem------------");
                }
            });

        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        public VHHeader(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the tmp_search_history

                    AppCore.showLog("----------------VHHeader------------");
                }
            });

        }

    }
}


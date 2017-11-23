package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtRatingReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private VtcModelUser vtcModelUser;

    public AdtRatingReview(Context context, VtcModelUser vtcModelUser) {
        this.context = context;
        this.vtcModelUser = vtcModelUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_rating_review_content, parent, false);
            //inflate your layout and pass it to view holder
            VHItem vhItem = new VHItem(v);

            return vhItem;
        } else if (viewType == TYPE_HEADER) {

            View vs = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_rating_review, parent, false);

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
        return 1;
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

        private TextView tv_title_rating_count;
        private RatingBar ratingBar;
        private TextView tv_count_nhanxet;
        private ProgressBar probar_5;
        private TextView tv_title_count_5;
        private ProgressBar probar_4;
        private TextView tv_title_count_4;
        private ProgressBar probar_3;
        private TextView tv_title_count_3;
        private ProgressBar probar_2;
        private TextView tv_title_count_2;
        private ProgressBar probar_1;
        private TextView tv_title_count_1;

        public VHHeader(View itemView) {
            super(itemView);

            tv_title_rating_count = (TextView) itemView.findViewById(R.id.tv_title_rating_count);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tv_count_nhanxet = (TextView) itemView.findViewById(R.id.tv_count_nhanxet);
            probar_5 = (ProgressBar) itemView.findViewById(R.id.probar_5);
            probar_4 = (ProgressBar) itemView.findViewById(R.id.probar_4);
            probar_3 = (ProgressBar) itemView.findViewById(R.id.probar_3);
            probar_2 = (ProgressBar) itemView.findViewById(R.id.probar_2);
            probar_1 = (ProgressBar) itemView.findViewById(R.id.probar_1);
            tv_title_count_5 = (TextView) itemView.findViewById(R.id.tv_title_count_5);
            tv_title_count_4 = (TextView) itemView.findViewById(R.id.tv_title_count_4);
            tv_title_count_3 = (TextView) itemView.findViewById(R.id.tv_title_count_3);
            tv_title_count_2 = (TextView) itemView.findViewById(R.id.tv_title_count_2);
            tv_title_count_1 = (TextView) itemView.findViewById(R.id.tv_title_count_1);


            if (vtcModelUser != null && vtcModelUser.getRate() != null) {
                ratingBar.setRating(vtcModelUser.getRate().getAvg_rate());
                tv_title_rating_count.setText(String.valueOf(vtcModelUser.getRate().getAvg_rate()));


                float rating_count = vtcModelUser.getRate().getTotal_rate();

                try {

                    float rating_5 = vtcModelUser.getRate().getFive_stars();

                    probar_5.setProgress((int) ((rating_5 / rating_count) * 100));
                    tv_title_count_5.setText(Utils.convertK((long) rating_5));

                    int rating_4 = vtcModelUser.getRate().getFour_stars();
                    probar_4.setProgress((int) ((rating_4 / rating_count) * 100));
                    tv_title_count_4.setText(Utils.convertK(rating_4));

                    int rating_3 = vtcModelUser.getRate().getThree_stars();
                    probar_3.setProgress((int) ((rating_3 / rating_count) * 100));
                    tv_title_count_3.setText(Utils.convertK(rating_3));

                    int rating_2 = vtcModelUser.getRate().getTwo_stars();
                    probar_2.setProgress((int) ((rating_2 / rating_count) * 100));
                    tv_title_count_2.setText(Utils.convertK(rating_2));

                    int rating_1 = vtcModelUser.getRate().getOne_star();
                    probar_1.setProgress((int) ((rating_1 / rating_count) * 100));
                    tv_title_count_1.setText(Utils.convertK(rating_1));

                }catch (Exception e){

                }

                tv_count_nhanxet.setText(context.getResources().getString(R.string.title_user_info_rating_review_count_rating, Utils.convertK((int) rating_count)));
            }
        }
    }
}

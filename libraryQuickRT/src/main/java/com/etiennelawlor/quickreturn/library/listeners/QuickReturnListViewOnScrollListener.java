package com.etiennelawlor.quickreturn.library.listeners;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;
import com.etiennelawlor.quickreturn.library.views.ScrollTabHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etiennelawlor on 7/10/14.
 */
public class QuickReturnListViewOnScrollListener implements AbsListView.OnScrollListener,ScrollTabHolder {

    // region Member Variables
//    private  QuickReturnViewType mQuickReturnViewType;
//    private  View mHeader;
//    private  int mMinHeaderTranslation;
//    private  View mFooter;
//    private  int mMinFooterTranslation;
//    private  boolean mIsSnappable; // Can Quick Return view snap into place?

    private int mPrevScrollY = 0;
    private int mHeaderDiffTotal = 0;
    private int mFooterDiffTotal = 0;
    private List<AbsListView.OnScrollListener> mExtraOnScrollListenerList = new ArrayList<AbsListView.OnScrollListener>();
    // endregion


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;
    // region Constructors

    private QuickReturnViewType mQuickReturnViewType;

    // Optional parameters - initialized to default values
    private View mHeader = null;
    private int mMinHeaderTranslation = 0;
    private View mFooter = null;
    private int mMinFooterTranslation = 0;
    private boolean mIsSnappable = false;

    public QuickReturnListViewOnScrollListener() {

    }

    public QuickReturnListViewOnScrollListener(View mHeader, View mFooter
    ) {
        this.mQuickReturnViewType = QuickReturnViewType.NORMAL;
        this.mHeader = mHeader;
        this.mFooter = mFooter;
        this.mIsSnappable = true;
    }

    public QuickReturnListViewOnScrollListener(QuickReturnViewType mQuickReturnViewType, View mHeader, int mMinHeaderTranslation
            , View mFooter, int mMinFooterTranslation, boolean mIsSnappable
    ) {
        this.mQuickReturnViewType = mQuickReturnViewType;
        this.mHeader = mHeader;
        this.mMinHeaderTranslation = mMinHeaderTranslation;
        this.mFooter = mFooter;
        this.mMinFooterTranslation = mMinFooterTranslation;
        this.mIsSnappable = mIsSnappable;
    }
    // endregion


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.d(getClass().getSimpleName(), "onScrollStateChanged() : scrollState - "+scrollState);
        // apply another list' s on scroll listener
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
            listener.onScrollStateChanged(view, scrollState);
        }
        if (scrollState == SCROLL_STATE_IDLE && mIsSnappable) {

            int midHeader = -mMinHeaderTranslation / 2;
            int midFooter = mMinFooterTranslation / 2;

            switch (mQuickReturnViewType) {
                case HEADER:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }
                    break;
                case FOOTER:
                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                case BOTH:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }

                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                case TWITTER:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }

                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                case NORMAL:
                    if (mHeader != null) {
                        ObjectAnimator animmHeader = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        animmHeader.setDuration(100);
                        animmHeader.start();
                    }

                    if (mFooter != null) {
                        ObjectAnimator animmFooter = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        animmFooter.setDuration(100);
                        animmFooter.start();
                    }
                    break;
            }

        }

    }

    @Override
    public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // apply extra on scroll listener
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
            listener.onScroll(listview, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        int scrollY = QuickReturnUtils.getScrollY(listview);
        int diff = mPrevScrollY - scrollY;

        Log.d(getClass().getSimpleName(), "onScroll() : scrollY - " + scrollY);
        Log.d(getClass().getSimpleName(), "onScroll() : diff - " + diff);
        Log.d(getClass().getSimpleName(), "onScroll() : mMinHeaderTranslation - " + mMinHeaderTranslation);
        Log.d(getClass().getSimpleName(), "onScroll() : mMinFooterTranslation - " + mMinFooterTranslation);

        if (diff != 0) {
            switch (mQuickReturnViewType) {
                case HEADER:
                    if (diff < 0) { // scrolling down
                        mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    break;
                case FOOTER:
                    if (diff < 0) { // scrolling down
                        mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                case BOTH:
                    if (diff < 0) { // scrolling down
                        mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
                        mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                case TWITTER:
                    if (diff < 0) { // scrolling down
                        if (scrollY > -mMinHeaderTranslation)
                            mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);

                        if (scrollY > mMinFooterTranslation)
                            mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                default:
                    break;
            }
        }

        mPrevScrollY = scrollY;


        // LoadMore Listview...
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If its still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isnt currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    // region Helper Methods
    public void registerExtraOnScrollListener(AbsListView.OnScrollListener listener) {
        mExtraOnScrollListenerList.add(listener);
    }

    // Defines the process for actually loading more data based on page
    public void onLoadMore(int page, int totalItemsCount) {

    }

    ;

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {

    }
    // endregion

    // region Inner Classes

   /* public static class Builder {
        // Required parameters
        private final QuickReturnViewType mQuickReturnViewType;

        // Optional parameters - initialized to default values
        private View mHeader = null;
        private int mMinHeaderTranslation = 0;
        private View mFooter = null;
        private int mMinFooterTranslation = 0;
        private boolean mIsSnappable = false;

        public Builder(QuickReturnViewType quickReturnViewType) {
            mQuickReturnViewType = quickReturnViewType;
        }

        public Builder header(View header){
            mHeader = header;
            return this;
        }

        public Builder minHeaderTranslation(int minHeaderTranslation){
            mMinHeaderTranslation = minHeaderTranslation;
            return this;
        }

        public Builder footer(View footer){
            mFooter = footer;
            return this;
        }

        public Builder minFooterTranslation(int minFooterTranslation){
            mMinFooterTranslation = minFooterTranslation;
            return this;
        }

        public Builder isSnappable(boolean isSnappable){
            mIsSnappable = isSnappable;
            return this;
        }

        public QuickReturnListViewOnScrollListener build() {
            return new QuickReturnListViewOnScrollListener(this);
        }
    }*/
}

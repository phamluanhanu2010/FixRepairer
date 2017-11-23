package com.strategy.intecom.vtc.fixrepairer.view.custom;

import android.view.View;

/**
 * Created by Mr. Ha on 6/30/16.
 */
public interface FragmentLifecycle {

    public void onPauseFragment();

    public void onResumeFragment(View viewRoot);
}

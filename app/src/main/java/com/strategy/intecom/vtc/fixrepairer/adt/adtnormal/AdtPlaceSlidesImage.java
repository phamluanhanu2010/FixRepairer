package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMPlaceSlideImage;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public class AdtPlaceSlidesImage extends FragmentPagerAdapter implements
        IconPagerAdapter {

    private Context context;

    private List<String> lsStrings;

    private int width = 0;
    private int height = 0;

    public AdtPlaceSlidesImage(FragmentManager fm, List<String> lsStrings, Context context, int width, int height) {
        super(fm);
        this.lsStrings = lsStrings;
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    public Fragment getItem(int position) {
        return new FMPlaceSlideImage(context, lsStrings.get(position), width, height);
    }

    @Override
    public int getCount() {
        if(lsStrings == null){
            return 0;
        }
        return lsStrings.size();
    }

    @Override
    public int getIconResId(int index) {
        return index;
    }

}
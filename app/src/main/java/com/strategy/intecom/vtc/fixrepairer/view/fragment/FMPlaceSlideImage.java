package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Mr. Ha on 6/1/16.
 */
@SuppressLint("ValidFragment")
public final class FMPlaceSlideImage extends Fragment {
    private String imageUrl;
    private Context context;

    private int width = 0;
    private int height = 0;

    public FMPlaceSlideImage(Context context, String imageUrl, int width, int height) {
        this.imageUrl = imageUrl;
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.tmp_sliding_image_item, container, false);

        ImageView image = (ImageView) viewRoot.findViewById(R.id.img_avatar);

        ImageLoadAsync loadAsync = new ImageLoadAsync(context, image, width , height);
        loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(imageUrl));

        return viewRoot;
    }
}

package com.strategy.intecom.vtc.fixrepairer.interfaces;

/**
 * Created by Mr. Ha on 5/27/16.
 */
public interface Callback {
    <T> void onHandlerCallBack(int key, T... t);
}

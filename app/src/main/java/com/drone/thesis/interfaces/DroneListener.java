package com.drone.thesis.interfaces;

import com.github.MakMoinee.library.interfaces.DefaultEventListener;

public interface DroneListener extends DefaultEventListener {
    void onDeleteItem(int position);

    @Override
    default void onDoubleClickListener() {
        DefaultEventListener.super.onDoubleClickListener();
    }

    @Override
    default void onDoubleClickListener(int position) {
        DefaultEventListener.super.onDoubleClickListener(position);
    }

    @Override
    default void onClickListener() {

    }
}

package com.example.library;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 当RecycleView Item发生两阶拉动的时候的回调
 */
public interface IItemOverSwipeCallback {

    void onItemOverScrollSwiped(RecyclerView.ViewHolder holder);

}

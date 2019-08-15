package com.example.library;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by tianrenzheng.
 */

public interface IItemOverSwipeCallback {

    /**
     * 两阶拉动，响应过度拉动的事件
     * @param holder
     */
    void onItemOverScrollSwiped(RecyclerView.ViewHolder holder);

}

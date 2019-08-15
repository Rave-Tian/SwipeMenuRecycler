package com.example.library;

import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 帮助处理Item Swipe动画和RecyclerView自带的插入、移除、移动动画同步问题
 */
public interface IMenuAnimationSync {

    /**
     * 判断当前Item是否有Swipe动画正在执行
     *
     * @return
     */
    boolean isAnimationRunning();
}

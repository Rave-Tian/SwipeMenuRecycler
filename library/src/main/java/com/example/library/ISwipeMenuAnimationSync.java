package com.example.library;

import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author rave
 * if you want to use animation in your swipe menu,you should implement this
 * interfae to synchronize your animation and recyclerview item animation.
 * {@link ItemTouchHelper#postDispatchSwipe(ItemTouchHelper.RecoverAnimation, int)}
 * {@link ItemTouchHelper#postDispatchMenuItemClick(RecyclerView.ViewHolder, MotionEvent)}
 */
public interface ISwipeMenuAnimationSync {

    /**
     * the onSwiped and onMenuItemClick callback will waiting for the swipe menu animation finish.
     * @return
     */
    boolean isAnimationRunning();
}

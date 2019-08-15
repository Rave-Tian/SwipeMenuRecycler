package com.example.library;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.tools.UITools;
import com.example.library.view.RecyclerItemSwipeWrapperView;


/**
 * Created by tianrenzheng.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final int SWIPE_FLAGS = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    public static final int SWIPE_FLAGS_OVER_SCROLL = ItemTouchHelper.OVER_SCROLL_LEFT | ItemTouchHelper.OVER_SCROLL_RIGHT | SWIPE_FLAGS;
    public static final int DRAG_FLAGS_VERTICAL = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    //    public static final int DRAG_FLAGS_ALL = DRAG_FLAGS_HORIZONTAL | SWIPE_FLAGS_OVER_SCROLL | DRAG_FLAGS_VERTICAL;
    public static final int SWIPE_FLAGS_ALL = SWIPE_FLAGS | SWIPE_FLAGS_OVER_SCROLL;

    private int mDragFlags;
    private IItemOverSwipeCallback mActionCallback;

    public ItemTouchHelperCallback(IItemOverSwipeCallback callback) {
        this(0, callback);
    }

    public ItemTouchHelperCallback(int dragFlags, IItemOverSwipeCallback callback) {
        mDragFlags = dragFlags;
        mActionCallback = callback;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.05f;
    }

//    @Override
//    public int getRightMenuWidth(int defaultValue) {
//        return mActionCallback.getRightMenuWidth();
////        return UITools.dp2px(240);
//    }
//
//    @Override
//    public int getLeftMenuWidth(int defaultValue) {
////        return UITools.dp2px(80);
//        return mActionCallback.getLeftMenuWidth();
//    }

    @Override
    public int getOverSwipeThresholdWidth(int defaultValue) {
//        return mActionCallback.getOverSwipeThresholdWidth();
        return UITools.dp2px(260);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, mDragFlags);
    }

    /**
     * @param viewHolder  The new ViewHolder that is being swiped or dragged. Might be null if
     *                    it is cleared.
     * @param actionState One of {@link ItemTouchHelper#ACTION_STATE_IDLE},
     *                    {@link ItemTouchHelper#ACTION_STATE_SWIPE} or
     *                    {@link ItemTouchHelper#ACTION_STATE_DRAG}.
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //长按拖拽时需要改变条目背景色什么的属性 请复写此方法，按照如下格式
        //if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null)
        //    viewHolder.itemView.setAlpha(0.7f);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        mActionCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
//        mActionCallback.onMoved(fromPos, toPos);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // onSwiped会在动画执行完成之后才会执行
        if (direction == ItemTouchHelper.OVER_SCROLL_LEFT) {
            if (mActionCallback != null) {
                mActionCallback.onItemOverScrollSwiped(viewHolder);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //在此恢复视图状态
        // clearView的时候讲MenuItem的状态重置。
        if (viewHolder.itemView instanceof RecyclerItemSwipeWrapperView) {
            ((RecyclerItemSwipeWrapperView) viewHolder.itemView).resetView();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder.itemView != null && viewHolder.itemView instanceof RecyclerItemSwipeWrapperView && Math.abs(((RecyclerItemSwipeWrapperView) viewHolder.itemView).getCurrentDx() - dX) > 0) {
                ((RecyclerItemSwipeWrapperView) viewHolder.itemView).operateChildDraw(dX);
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onRecoveryPreOpen(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX) {
        super.onRecoveryPreOpen(recyclerView, viewHolder, dX);
        if (viewHolder.itemView != null && viewHolder.itemView instanceof RecyclerItemSwipeWrapperView) {
            ((RecyclerItemSwipeWrapperView) viewHolder.itemView).operateChildDraw(dX);
        }
    }

    public static int makeMovementFlags(int dragFlags, int swipeFlags) {
        return makeFlag(0, swipeFlags | dragFlags) | makeFlag(1, swipeFlags) | makeFlag(2, dragFlags);
    }

    public static int makeFlag(int actionState, int directions) {
        return directions << actionState * 8;
    }

}

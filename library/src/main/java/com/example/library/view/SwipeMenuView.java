package com.example.library.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;


import com.example.library.ISwipeMenuAnimationSync;
import com.example.library.ItemTouchHelper;
import com.example.library.tools.UITools;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenuView extends FrameLayout implements ISwipeMenuAnimationSync {

    private FrameLayout contentLayout;
    private FrameLayout rightMenuLayout;
    private FrameLayout leftMenuLayout;

    private List<SwipeMenuItemView> rightSwipeMenuItems;
    private List<SwipeMenuItemView> leftSwipeMenuItems;
    private int height;
    private volatile int currentDx = 0;
    private float floatCurDx = 0f;
    private OverSwipeAnimation overSwipeAnimation;
    private boolean isOverScrolled = false;

    public SwipeMenuView(@NonNull Context context) {
        super(context);
        initView(context);
        this.overSwipeAnimation = new OverSwipeAnimation();
    }

    private void initView(final Context context) {
        contentLayout = new FrameLayout(context);
        contentLayout.setBackgroundColor(Color.WHITE);
        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.LEFT);
        addView(contentLayout, contentLp);

        rightMenuLayout = new FrameLayout(context);
        rightMenuLayout.setBackgroundColor(Color.BLUE);
        LayoutParams menuLp = new LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.RIGHT);
        addView(rightMenuLayout, menuLp);

        rightSwipeMenuItems = new ArrayList<>();

        leftMenuLayout = new FrameLayout(context);
        LayoutParams lefMenuLp = new LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);
        addView(leftMenuLayout, lefMenuLp);

        leftSwipeMenuItems = new ArrayList<>();
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setContentLayout(View contentLayoutView) {
        this.contentLayout.removeAllViews();
        LayoutParams contentLP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        this.contentLayout.addView(contentLayoutView, contentLP);
    }

    public View getOriginItemView() {
        if(contentLayout.getChildCount() > 0) {
            return this.contentLayout.getChildAt(0);
        }
        return null;
    }

    public void setLeftMenuLayout(final List<SwipeMenuItem> leftMenuItems) {
        this.leftSwipeMenuItems.clear();
        for (int i = 0; i < leftMenuItems.size(); i++) {
            SwipeMenuItemView temp = new SwipeMenuItemView(getContext());
            temp.setText(leftMenuItems.get(i).getText());
            temp.setAnimation(leftMenuItems.get(i).getLottieFileName());
            temp.setBackgroundColor(leftMenuItems.get(i).getBackgroundColor());
            final int index = i;
            temp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftMenuItems.get(index).getClickListener().onClick(v, (Integer) v.getTag(ItemTouchHelper.MENU_CLICKED_ITEM_TAG));
                }
            });

            leftSwipeMenuItems.add(temp);
            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, (i == leftMenuItems.size() - 1) ? Gravity.LEFT : Gravity.RIGHT);
            leftMenuLayout.addView(temp, layoutParams);
        }
    }

    public void setRightMenuLayout(final List<SwipeMenuItem> rightMenuItems) {
        this.rightSwipeMenuItems.clear();
        for (int i = 0; i < rightMenuItems.size(); i++) {
            SwipeMenuItemView temp = new SwipeMenuItemView(getContext());
            temp.setText(rightMenuItems.get(i).getText());
            temp.setAnimation(rightMenuItems.get(i).getLottieFileName());
            temp.setBackgroundColor(rightMenuItems.get(i).getBackgroundColor());
            final int index = i;
            temp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightMenuItems.get(index).getClickListener().onClick(v, (Integer) v.getTag(ItemTouchHelper.MENU_CLICKED_ITEM_TAG));
                }
            });
            temp.setInitLottieValueCallback(rightMenuItems.get(i).getInitLottieValueCallback());

            rightSwipeMenuItems.add(temp);
            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, (i == rightMenuItems.size() - 1) ? Gravity.RIGHT : Gravity.LEFT);
            rightMenuLayout.addView(temp, layoutParams);
        }
    }

    public int getLeftMenuWidth(){
        return height * leftSwipeMenuItems.size();
    }

    public int getRightMenuWidth(){
        return height * rightSwipeMenuItems.size();
    }

    /**
     * 重置MenuItem的布局
     */
    public void resetView() {
        this.currentDx = 0;
        this.floatCurDx = 0;

        isOverScrolled = false;
        contentLayout.setTranslationX(0);
        rightMenuLayout.getLayoutParams().width = 0;
        for (int i = 0; i < rightSwipeMenuItems.size(); i++) {
            rightSwipeMenuItems.get(i).getLayoutParams().width = 0;
            rightSwipeMenuItems.get(i).stopAnimation();
        }

        leftMenuLayout.getLayoutParams().width = 0;
        for (int i = 0; i < leftSwipeMenuItems.size(); i++) {
            leftSwipeMenuItems.get(i).getLayoutParams().width = 0;
            leftSwipeMenuItems.get(i).stopAnimation();
        }

        this.overSwipeAnimation.cancel();
        requestLayout();
    }

    public void operateChildDraw(float dX) {
        this.currentDx = (int) dX;
        this.floatCurDx = dX;

        if (dX >= 0) {
            if (leftSwipeMenuItems.size() > 0) {
                // 展开左边的菜单
                contentLayout.setTranslationX(this.currentDx);

                // 手势向右拉（展开左边菜单，read/unread）
                rightMenuLayout.getLayoutParams().width = 0;
                for (int i = 0; i < rightSwipeMenuItems.size(); i++) {
                    rightSwipeMenuItems.get(i).getLayoutParams().width = 0;
                }

                if ((int) Math.abs(dX) / leftSwipeMenuItems.size() > UITools.dp2px(40)) {
                    for (int i = 0; i < leftSwipeMenuItems.size(); i++) {
                        leftSwipeMenuItems.get(i).playAnimation();
                    }
                }

                leftMenuLayout.getLayoutParams().width = Math.abs(currentDx);
                if(leftSwipeMenuItems.size() > 0){
                    int subItemWidth = Math.abs(currentDx) / leftSwipeMenuItems.size();
                    leftMenuLayout.getChildAt(0).getLayoutParams().width = subItemWidth;
                }
            }
        } else {
            if (rightSwipeMenuItems.size() > 0) {
                // 展开右边菜单，pin/mute/delete）
                // step1:关闭左侧菜单
                leftMenuLayout.getLayoutParams().width = 0;

                if ((int) Math.abs(dX) / rightSwipeMenuItems.size() > UITools.dp2px(40)) {
                    for (int i = 0; i < rightSwipeMenuItems.size(); i++) {
                        rightSwipeMenuItems.get(i).playAnimation();
                    }
                }

                // step2:处理过度滑动
                if (Math.abs(currentDx) > UITools.dp2px(260)) {
                    if (!isOverScrolled) {
                        // 从非过度滑动到过度滑动的临界状态
                        overSwipeAnimation.cancel();
                        overSwipeAnimation.start(this.getMeasuredWidth());
                    } else {
                        // 在过度滑动的状态下继续滑动
                        if (overSwipeAnimation.getPlayedFlag() != OverSwipeAnimation.ANIMATION_PLAY_STATE_PLAYING) {
                            // 如果动画结束，以下逻辑接手处理布局
                            contentLayout.setTranslationX(currentDx);
                            rightMenuLayout.getLayoutParams().width = Math.abs(currentDx);
//                        lottieRight3.getLayoutParams().width = Math.abs(currentDx);

                            if (!rightSwipeMenuItems.isEmpty()) {
                                rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = Math.abs(currentDx);
                            }
                        } else {
                            // 动画还没结束，就交由动画来处理布局
                        }
                    }

                    // 转换状态
                    isOverScrolled = true;

                } else {
                    // Step3:处理非过度滑动
                    if (isOverScrolled) {
                        // 从过度滑动到非过度滑动的临界状态
                        overSwipeAnimation.cancel();
                        overSwipeAnimation.start(0);
                    } else {
                        if (overSwipeAnimation.getPlayedFlag() != OverSwipeAnimation.ANIMATION_PLAY_STATE_PLAYING) {
                            // 动画结束,以下逻辑接手处理布局
                            contentLayout.setTranslationX(currentDx);
                            rightMenuLayout.getLayoutParams().width = Math.abs(currentDx);
                            int subItemWidth = Math.abs(currentDx) / rightSwipeMenuItems.size();

                            int holdWidth = 0;
                            for (int i = 0; i < rightSwipeMenuItems.size() - 1; i++) {
                                rightSwipeMenuItems.get(i).getLayoutParams().width = subItemWidth;
                                ((LayoutParams) rightSwipeMenuItems.get(i).getLayoutParams()).leftMargin = holdWidth;
                                holdWidth += subItemWidth;
                            }

                            if (!rightSwipeMenuItems.isEmpty()) {
                                rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = Math.abs(currentDx) - holdWidth;
                            }

                        } else {
                            // 动画还没结束，交由动画来处理布局
                        }
                    }

                    isOverScrolled = false;
                }
            }
        }

        requestLayout();
    }

    /**
     * 这里需要返回高精度的值所以要返回浮点型。
     * 用来判断，在Swipe动画的时候是否有必要回调operateChildDraw
     *
     * @return
     */
    public float getCurrentDx() {
        return this.floatCurDx;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(widthMeasureSpec),
                        MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(
                        height,
                        MeasureSpec.EXACTLY
                )
        );
    }

    @Override
    public boolean isAnimationRunning() {
        return this.overSwipeAnimation.animationPlayState == OverSwipeAnimation.ANIMATION_PLAY_STATE_PLAYING;
    }

    private class OverSwipeAnimation implements Animator.AnimatorListener {

        private static final int ANIMATION_PLAY_STATE_NONE = 0;
        private static final int ANIMATION_PLAY_STATE_PLAYING = 1;
        private static final int ANIMATION_PLAY_STATE_PLAYED = 2;
        private volatile int animationPlayState = ANIMATION_PLAY_STATE_NONE;
        private ValueAnimator animator;
        private static final int FULL_ANIMATION_DURATION = 200;

        private int startMenuWidth;
        private int startWidth;
        private int endWidth;
        private int dx;

        private OverSwipeAnimation() {
            animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();

                    if (startWidth > endWidth) {
                        // 关闭过度拖动的动画
                        int rightMenuWidth = Math.abs(currentDx);
                        contentLayout.setTranslationX(currentDx);
                        rightMenuLayout.getLayoutParams().width = rightMenuWidth;

                        int rightMenuItemWidth = rightMenuWidth / rightSwipeMenuItems.size();

//                        int textViewRight3Min = rightMenuWidth - rightMenuItemWidth * 2;//避免当rightMenuWidth不能被Item count整除时产生缝隙
                        int holdWidth = 0;
                        for (int i = 0; i < rightSwipeMenuItems.size() - 1; i++) {
                            rightSwipeMenuItems.get(i).getLayoutParams().width = rightMenuItemWidth;
                            ((LayoutParams) rightSwipeMenuItems.get(i).getLayoutParams()).leftMargin = holdWidth;
                            holdWidth += rightMenuItemWidth;
                        }
                        int textViewRight3Min = rightMenuWidth - holdWidth;

                        int textViewRight3AnimationWidth = (int) (fraction * (endWidth - startWidth)) + startWidth;
                        if (textViewRight3AnimationWidth >= textViewRight3Min) {
//                            lottieRight3.getLayoutParams().width = Math.min(textViewRight3AnimationWidth, rightMenuWidth);
                            rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = Math.min(textViewRight3AnimationWidth, rightMenuWidth);
                            rightMenuLayout.requestLayout();
                        } else {
//                            lottieRight3.getLayoutParams().width = textViewRight3Min;
                            rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = textViewRight3Min;
                            rightMenuLayout.requestLayout();
                            animation.cancel();
                        }
                    } else {
                        // 打开过度拖动的动画

                        // right1和right2的Item的的最大宽度
                        int itemWith = startMenuWidth / rightSwipeMenuItems.size();

                        int holdWidth = 0;
                        for (int i = 0; i < rightSwipeMenuItems.size() - 1; i++) {
                            rightSwipeMenuItems.get(i).getLayoutParams().width = itemWith;
                            ((LayoutParams) rightSwipeMenuItems.get(i).getLayoutParams()).leftMargin = holdWidth;
                            holdWidth += itemWith;
                        }

                        int textViewRight3Min = startMenuWidth - holdWidth;// 避免浮点运算精度丢失，导致拖动时产生缝隙

                        int rightMenuAnimationWidth = (int) (fraction * (endWidth - startMenuWidth)) + startMenuWidth;
                        int textViewRight3AnimationWidth = (int) (fraction * (endWidth - textViewRight3Min)) + textViewRight3Min;
                        if (textViewRight3AnimationWidth <= rightMenuLayout.getLayoutParams().width) {
                            contentLayout.setTranslationX(Math.min(rightMenuAnimationWidth, Math.abs(currentDx)) * -1);
                            rightMenuLayout.getLayoutParams().width = Math.min(rightMenuAnimationWidth, Math.abs(currentDx));

                            rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = Math.max(textViewRight3Min, textViewRight3AnimationWidth);
                            rightMenuLayout.requestLayout();
                        } else {
                            // 解决动画 结束时的不对齐现象（不对齐的原因是fraction是浮点运算，转整形时精度丢失）
                            contentLayout.setTranslationX(Math.min(rightMenuAnimationWidth, Math.abs(currentDx)) * -1);
                            rightMenuLayout.getLayoutParams().width = Math.min(rightMenuAnimationWidth, Math.abs(currentDx));
                            rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width = Math.min(rightMenuAnimationWidth, Math.abs(currentDx));
                            rightMenuLayout.requestLayout();
                            animation.cancel();
                        }
                    }
                }
            });
            animator.addListener(this);
        }

        private int getPlayedFlag() {
            return animationPlayState;
        }

        private void start(int endWidth) {
            if (animationPlayState == ANIMATION_PLAY_STATE_NONE) {
                // 如果当前状态为动画空闲状态
//                this.startWidth = lottieRight3.getLayoutParams().width;
                this.startWidth = rightSwipeMenuItems.get(rightSwipeMenuItems.size() - 1).getLayoutParams().width;
                this.startMenuWidth = rightMenuLayout.getLayoutParams().width;
                this.endWidth = endWidth;
                this.dx = endWidth - startWidth;

                float animatorRate = (float) Math.abs(dx) / (float) SwipeMenuView.this.getMeasuredWidth();
                long duration = (long) (animatorRate * FULL_ANIMATION_DURATION);
                animator.setDuration(duration);
                animator.setDuration(FULL_ANIMATION_DURATION);
                animator.start();
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            animationPlayState = ANIMATION_PLAY_STATE_PLAYING;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animationPlayState = ANIMATION_PLAY_STATE_PLAYED;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        public void cancel() {
            animationPlayState = ANIMATION_PLAY_STATE_NONE;
            animator.cancel();
        }
    }

}

package com.example.library.view;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.library.IBaseSwipeMenuItemClickProcessor;
import com.example.library.tools.UITools;

public class SwipeMenuItemView extends FrameLayout implements IBaseSwipeMenuItemClickProcessor {

    public interface IInitLottieValueCallback {
        void initValueCallback(LottieAnimationView lottieAnimationView);
    }

    private LottieAnimationView lottieAnimationView;
    private TextView textView;
    private IInitLottieValueCallback initLottieValueCallback;
    private boolean animationPlayingFlag = false;

    public SwipeMenuItemView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(final Context context) {
        lottieAnimationView = new LottieAnimationView(context);
        lottieAnimationView.setVisibility(GONE);
        LayoutParams lottieAnimationLp = new LayoutParams(UITools.dp2px(30), UITools.dp2px(30), Gravity.LEFT | Gravity.TOP);
        addView(lottieAnimationView, lottieAnimationLp);

        textView = new TextView(context);
        textView.setSingleLine(true);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setVisibility(GONE);
        LayoutParams textViewLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM);
        addView(textView, textViewLp);
    }

    public void setAnimation(String animationRes) {
        if (null != animationRes && !animationRes.isEmpty()) {
            lottieAnimationView.setVisibility(VISIBLE);
            lottieAnimationView.setAnimation(animationRes);
        } else {
            lottieAnimationView.setVisibility(GONE);
        }
    }

    public void setText(String text) {
        if (null != text && !text.isEmpty()) {
            textView.setVisibility(VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(GONE);
        }
    }

    public void setTextColor(@ColorInt int textColor) {
        textView.setTextColor(textColor);
    }

    public void setTextSize(float textSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void playAnimation() {
        if (!animationPlayingFlag) {
            lottieAnimationView.playAnimation();
            animationPlayingFlag = true;
        }
    }

    public void stopAnimation() {
        lottieAnimationView.cancelAnimation();
        animationPlayingFlag = false;
    }

    public void setInitLottieValueCallback(IInitLottieValueCallback initLottieValueCallback) {
        this.initLottieValueCallback = initLottieValueCallback;
        if (this.initLottieValueCallback != null && lottieAnimationView != null) {
            initLottieValueCallback.initValueCallback(lottieAnimationView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            ((LayoutParams) lottieAnimationView.getLayoutParams()).leftMargin = (getMeasuredHeight() - lottieAnimationView.getMeasuredWidth()) / 2;
            int textViewWidth = 0, textViewHeight = 0;
            textView.measure(textViewWidth, textViewHeight);
            ((LayoutParams) textView.getLayoutParams()).leftMargin = (getMeasuredHeight() - textView.getMeasuredWidth()) / 2;

            int spaceHeight = getMeasuredHeight()
                    - ((lottieAnimationView.getVisibility() == View.VISIBLE) ? lottieAnimationView.getMeasuredHeight() : 0)
                    - ((textView.getVisibility() == View.VISIBLE) ? textView.getMeasuredHeight() : 0);
            int margin = ((lottieAnimationView.getVisibility() == VISIBLE && textView.getVisibility() == VISIBLE) ? UITools.dp2px(5) : 0);
            ((LayoutParams) lottieAnimationView.getLayoutParams()).topMargin = (spaceHeight - margin) / 2;
            ((LayoutParams) textView.getLayoutParams()).bottomMargin = (spaceHeight - margin) / 2;
        }
    }
}

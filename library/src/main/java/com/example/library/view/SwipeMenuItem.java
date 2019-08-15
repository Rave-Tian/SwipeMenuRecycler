package com.example.library.view;

import androidx.annotation.ColorInt;

import com.example.library.ISwipeMenuItemClickListener;


public class SwipeMenuItem {

    private String text;
    private String lottieFileName;
    private int textColor;
    private float textSize;
    private int backgroundColor;
    private ISwipeMenuItemClickListener clickListener;
    private SwipeMenuItemView.IInitLottieValueCallback initLottieValueCallback;

    private SwipeMenuItem() {
    }

    public String getText(){
        return text;
    }

    public String getLottieFileName(){
        return lottieFileName;
    }

    public int getBackgroundColor(){
        return backgroundColor;
    }

    public ISwipeMenuItemClickListener getClickListener(){
        return clickListener;
    }

    public SwipeMenuItemView.IInitLottieValueCallback getInitLottieValueCallback(){
        return initLottieValueCallback;
    }

    public static class Builder {
        private String text;
        private String lottieFileName;
        private int textColor;
        private float textSize;
        private int backgroundColor;
        private ISwipeMenuItemClickListener clickListener;
        private SwipeMenuItemView.IInitLottieValueCallback initLottieValueCallback;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setLottieFileName(String lottieFileName) {
            this.lottieFileName = lottieFileName;
            return this;
        }

        public Builder setTextColor(@ColorInt int color){
            this.textColor = color;
            return this;
        }

        public Builder setTextSize(float textSize){
            this.textSize = textSize;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder setClickListener(ISwipeMenuItemClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public Builder setInitLottieValueCallback(SwipeMenuItemView.IInitLottieValueCallback initLottieValueCallback){
            this.initLottieValueCallback = initLottieValueCallback;
            return this;
        }

        public SwipeMenuItem build() {
            SwipeMenuItem swipeMenuItem = new SwipeMenuItem();
            swipeMenuItem.text = this.text;
            swipeMenuItem.lottieFileName = this.lottieFileName;
            swipeMenuItem.textColor = this.textColor;
            swipeMenuItem.textSize = this.textSize;
            swipeMenuItem.backgroundColor = this.backgroundColor;
            swipeMenuItem.clickListener = this.clickListener;
            swipeMenuItem.initLottieValueCallback = this.initLottieValueCallback;
            return swipeMenuItem;
        }

    }


}

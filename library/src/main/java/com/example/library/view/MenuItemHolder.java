package com.example.library.view;

import androidx.annotation.ColorInt;

import com.example.library.IMenuItemClickListener;


public class MenuItemHolder {

    private String text;
    private String lottieFileName;
    private int textColor;
    private float textSize;
    private int backgroundColor;
    private IMenuItemClickListener clickListener;
    private MenuItemView.IInitLottieValueCallback initLottieValueCallback;

    private MenuItemHolder() {
    }

    public String getText() {
        return text;
    }

    public String getLottieFileName() {
        return lottieFileName;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public IMenuItemClickListener getClickListener() {
        return clickListener;
    }

    public MenuItemView.IInitLottieValueCallback getInitLottieValueCallback() {
        return initLottieValueCallback;
    }

    public static class Builder {
        private String text;
        private String lottieFileName;
        private int textColor;
        private float textSize;
        private int backgroundColor;
        private IMenuItemClickListener clickListener;
        private MenuItemView.IInitLottieValueCallback initLottieValueCallback;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setLottieFileName(String lottieFileName) {
            this.lottieFileName = lottieFileName;
            return this;
        }

        public Builder setTextColor(@ColorInt int color) {
            this.textColor = color;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder setClickListener(IMenuItemClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public Builder setInitLottieValueCallback(MenuItemView.IInitLottieValueCallback initLottieValueCallback) {
            this.initLottieValueCallback = initLottieValueCallback;
            return this;
        }

        public MenuItemHolder build() {
            MenuItemHolder menuItemHolder = new MenuItemHolder();
            menuItemHolder.text = this.text;
            menuItemHolder.lottieFileName = this.lottieFileName;
            menuItemHolder.textColor = this.textColor;
            menuItemHolder.textSize = this.textSize;
            menuItemHolder.backgroundColor = this.backgroundColor;
            menuItemHolder.clickListener = this.clickListener;
            menuItemHolder.initLottieValueCallback = this.initLottieValueCallback;
            return menuItemHolder;
        }

    }


}

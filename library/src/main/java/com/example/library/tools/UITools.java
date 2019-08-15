package com.example.library.tools;

import android.content.res.Resources;

public class UITools {

    public static int dp2px(int dip){
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dip * density +0.5f);
    }
}

package com.example.library;


import com.example.library.view.SwipeMenuItem;

import java.util.List;

public abstract class ISwipeMenuItemCreator {

    public abstract void onCreateMenu(List<SwipeMenuItem> leftMenu, List<SwipeMenuItem> rightMenu, int viewType);

}

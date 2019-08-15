package com.example.library;


import com.example.library.view.MenuItemHolder;

import java.util.List;

public abstract class IMenuItemCreator {

    public abstract void onCreateMenu(List<MenuItemHolder> leftMenu, List<MenuItemHolder> rightMenu, int viewType);

}

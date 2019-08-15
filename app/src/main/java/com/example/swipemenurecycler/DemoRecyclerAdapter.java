package com.example.swipemenurecycler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.IItemOverSwipeCallback;
import com.example.library.IMenuItemCreator;
import com.example.library.tools.UITools;
import com.example.library.view.MenuItemHolder;
import com.example.library.view.RecyclerItemSwipeWrapperView;

import java.util.ArrayList;

public class DemoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IItemOverSwipeCallback {

    private int counter = 1;
    private ArrayList<String> mData;
    private IMenuItemCreator mSwipeMenuItemCreator;

    public DemoRecyclerAdapter(ArrayList<String> data, IMenuItemCreator swipeMenuItemCreator) {
        this.mData = data;
        this.mSwipeMenuItemCreator = swipeMenuItemCreator;
    }

    public void refreshData(ArrayList<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView contentView = new TextView(parent.getContext());
        contentView.setPadding(0, 90, 0, 90);
        contentView.setText("test");

        if (mSwipeMenuItemCreator != null) {
            RecyclerItemSwipeWrapperView recyclerItemSwipeWrapperView = new RecyclerItemSwipeWrapperView(parent.getContext());
            recyclerItemSwipeWrapperView.setHeight(getItemHeight());
            recyclerItemSwipeWrapperView.setContentLayout(contentView);
            ArrayList<MenuItemHolder> leftMenuItems = new ArrayList<>();
            ArrayList<MenuItemHolder> rightMenuItems = new ArrayList<>();
            mSwipeMenuItemCreator.onCreateMenu(leftMenuItems, rightMenuItems, viewType);
            if (leftMenuItems != null && leftMenuItems.size() > 0) {
                recyclerItemSwipeWrapperView.setLeftMenuLayout(leftMenuItems);
            }
            if (rightMenuItems != null && rightMenuItems.size() > 0) {
                recyclerItemSwipeWrapperView.setRightMenuLayout(rightMenuItems);
            }

            return new DemoHolder(recyclerItemSwipeWrapperView);

        } else {
            return new DemoHolder(contentView);
        }
    }

    private int getItemHeight() {
        return UITools.dp2px(80);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (mSwipeMenuItemCreator == null) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(mData.get(position));
        } else {
            View originItemView = ((RecyclerItemSwipeWrapperView) holder.itemView).getOriginItemView();
            if (originItemView != null) {
                ((TextView) originItemView).setText(mData.get(position));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "Item Clicked :" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onItemOverScrollSwiped(RecyclerView.ViewHolder holder) {
        removeItem(holder.getAdapterPosition());
    }

    private boolean inRange(int position) {
        return position >= 0 && position < mData.size();
    }

    public void addItem(int position) {
        if (inRange(position)) {
            this.mData.add(position, "add item :" + counter++);
            this.notifyItemInserted(position);
            this.notifyItemRangeChanged(position, mData.size() - position);
        }
    }

    public void removeItem(int position) {
        if (inRange(position)) {
            this.mData.remove(position);
            this.notifyItemRemoved(position);
            if (position != mData.size() - 1) {
                notifyItemRangeChanged(position, this.mData.size() - position);
            }
        }
    }

    public void moveItem(int fromPos, int toPos) {
        if ((0 <= fromPos) && (fromPos <= this.mData.size() - 1)
                && (0 <= toPos) && (toPos <= this.mData.size() - 1)
                && fromPos > toPos) {
            String fromValue = this.mData.get(fromPos);
            this.mData.add(toPos, fromValue);
            this.mData.remove(fromPos + 1);
            notifyItemMoved(fromPos, toPos);
            this.notifyItemRangeChanged(toPos, fromPos - toPos + 1);
        }
    }

    public static class DemoHolder extends RecyclerView.ViewHolder {

        public DemoHolder(View itemView) {
            super(itemView);
        }
    }
}

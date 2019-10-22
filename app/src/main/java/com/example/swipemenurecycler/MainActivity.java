package com.example.swipemenurecycler;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.example.library.IMenuItemClickListener;
import com.example.library.IMenuItemCreator;
import com.example.library.ItemTouchHelper;
import com.example.library.ItemTouchHelperCallback;
import com.example.library.view.MenuItemHolder;
import com.example.library.view.MenuItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DemoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyler = findViewById(R.id.recycler);
        ArrayList<String> list = initList();
        adapter = new DemoRecyclerAdapter(list, new IMenuItemCreator() {
            @Override
            public void onCreateMenu(List<MenuItemHolder> leftMenu, List<MenuItemHolder> rightMenu, int viewType) {
                rightMenu.add(new MenuItemHolder.Builder()
                        .setText("pin")
                        .setLottieFileName("cell_right_pin.json")
                        .setBackgroundColor(Color.parseColor("#61BC79"))
                        .setClickListener(new IMenuItemClickListener() {
                            @Override
                            public void onClick(View v, int pos) {
                                adapter.moveItem(pos, 1);
                            }
                        }).build());
                String menuTitle = viewType == 0 ? "Mute" : "Unmute";
                rightMenu.add(new MenuItemHolder.Builder()
                        .setText(menuTitle)
                        .setLottieFileName("cell_right_mute.json")
                        .setBackgroundColor(Color.parseColor("#FFBB52"))
                        .setClickListener(new IMenuItemClickListener() {
                            @Override
                            public void onClick(View v, int pos) {

                            }
                        })
                        .setInitLottieValueCallback(new MenuItemView.IInitLottieValueCallback() {
                            @Override
                            public void initValueCallback(LottieAnimationView lottieAnimationView) {
                                KeyPath path1 = new KeyPath("background-change", "路径", "填充 1");
                                KeyPath path2 = new KeyPath("background-change2", "椭圆形", "填充 1");

                                lottieAnimationView.addValueCallback(
                                        path1,
                                        LottieProperty.COLOR,
                                        new LottieValueCallback<Integer>() {
                                            @Nullable
                                            @Override
                                            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                                                return Color.parseColor("#FFBB52");
                                            }
                                        });
                                lottieAnimationView.addValueCallback(
                                        path2,
                                        LottieProperty.COLOR,
                                        new LottieValueCallback<Integer>() {
                                            @Nullable
                                            @Override
                                            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                                                return Color.parseColor("#FFBB52");
                                            }
                                        });
                            }
                        })
                        .build());
                rightMenu.add(new MenuItemHolder.Builder()
                        .setText("delete")
                        .setLottieFileName("cell_right_delete.json")
                        .setBackgroundColor(Color.parseColor("#FF5454"))
                        .setClickListener(new IMenuItemClickListener() {
                            @Override
                            public void onClick(View v, int pos) {
                                adapter.removeItem(pos);
                            }
                        }).build());

                leftMenu.add(new MenuItemHolder.Builder()
                        .setText("Unread")
                        .setLottieFileName("cell_left_unRead.json")
                        .setBackgroundColor(Color.parseColor("#FF5454"))
                        .setClickListener(new IMenuItemClickListener() {
                            @Override
                            public void onClick(View v, int pos) {
                                Toast.makeText(MainActivity.this, "unread clicked", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build());
            }
        });

        recyler.setLayoutManager(new LinearLayoutManager(this));
        recyler.addItemDecoration(new DividerItemDecoration(this, 1));
        recyler.setAdapter(adapter);

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(ItemTouchHelperCallback.SWIPE_FLAGS_OVER_SCROLL, adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyler);

        Button btnAdd = findViewById(R.id.add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(5);
            }
        });

        Button btnRemove = findViewById(R.id.remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeItem(5);
            }
        });

        Button btnMove = findViewById(R.id.move);
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.moveItem(5, 1);
            }
        });
    }

    private ArrayList<String> initList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 115; i++) {
            list.add("Mock Item " + i);
        }
        return list;
    }
}

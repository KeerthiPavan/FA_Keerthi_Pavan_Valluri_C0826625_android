package com.example.fa_keerthi_pavan_valluri_c0826625_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    public ArrayList newList;
    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    public static boolean updateButton = false;
    public static int updatePosition;
    LocationAdapter.RecyclerViewClickListener listener;
    public static boolean fromFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerView);
//        listView = findViewById(R.id.favListView);
        newList = MainActivity.arrayList;

        setAdapter();

        locationAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void setAdapter() {
        setOnClickListner();
        locationAdapter = new LocationAdapter(newList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(locationAdapter);
    }

    private void setOnClickListner() {
        listener = new LocationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                updatePosition = position;
                fromFavorites = true;
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }

    String deletedPlace = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT  | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedPlace = newList.get(position).toString();
                    newList.remove(position);
                    locationAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView,deletedPlace,Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newList.add(position, deletedPlace);
                            locationAdapter.notifyItemInserted(position);
                        }
                    }).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    updatePosition = position;
                    updateButton = true;
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
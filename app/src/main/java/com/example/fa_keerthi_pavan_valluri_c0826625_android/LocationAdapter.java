package com.example.fa_keerthi_pavan_valluri_c0826625_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {
    private ArrayList<String>  newList;
    private RecyclerViewClickListener listener;

    public LocationAdapter(ArrayList newList, RecyclerViewClickListener listener) {
        this.newList = newList;
        this.listener = listener;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView placeText;

        public MyViewHolder(final View view) {
            super(view);
            placeText = view.findViewById(R.id.locationLable);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public LocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loc_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.MyViewHolder holder, int position) {
        String name = newList.get(position).toString();
        holder.placeText.setText(name);
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}

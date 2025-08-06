package com.github.jura120596.molodec.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.Event;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.helpers.LastItemListener;

public class AdapterEvents extends RecyclerView.Adapter {
    public onEventClickListener clickListener;
    public LastItemListener lastItemListener;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataBASE.EVENT_JSON_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView dateTv, placeTv, titleTv, pointsTv;
        ConstraintLayout addPeopleBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            addPeopleBtn = itemView.findViewById(R.id.bt_scanner);
            dateTv = itemView.findViewById(R.id.tv_event_time);
            placeTv = itemView.findViewById(R.id.tv_event_content);
            titleTv = itemView.findViewById(R.id.tv_event_title);
            pointsTv = itemView.findViewById(R.id.tv_event_points);
            if (DataBASE.user.isAdmin()) {
                itemView.setOnLongClickListener(this);
                itemView.setOnClickListener(this);
            }
        }
        public void bindView(int position){
            if (position == DataBASE.EVENT_JSON_LIST.size() - 1 && lastItemListener != null) lastItemListener.onLastItemOpened(position);
            Event event = DataBASE.EVENT_JSON_LIST.get(position);
            dateTv.setText(""+event.getDate());
            placeTv.setText(""+event.getPlace());
            pointsTv.setText(""+event.getPoints());
            titleTv.setText(""+event.getTitle());
            addPeopleBtn.setOnClickListener(view -> clickListener.onAddPeople(position));
            addPeopleBtn.setVisibility(Event.canAddParticipant(event )&& (DataBASE.user.isAdmin() || DataBASE.user.isCurator()) ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(),view);
            return false;
        }

    }
    public void setOnItemClickListener(onEventClickListener clickListener){
        this.clickListener = clickListener;
    }

    public void setLastItemListener(LastItemListener lastItemListener) {
        this.lastItemListener = lastItemListener;
    }

    public abstract static class onEventClickListener {
        public abstract void onItemClick(int position, View view);
        public void onItemLongClick(int position,View view) {};
        public void onAddPeople(int position) {};
    }
}

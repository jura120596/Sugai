package ru.sugai.village.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;
import ru.sugai.village.data.UserRequest;
import ru.sugai.village.helpers.LastItemListener;

import java.util.ArrayList;
import java.util.List;


public class UserRequestsAdapter extends RecyclerView.Adapter {

    public static ClickListener clickListener;
    public LastItemListener lastItemListener;
    public Context context;
    public List<UserRequest> items = new ArrayList<>();

    public UserRequestsAdapter(Context context, List<UserRequest> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_request_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        TextView authorTv, typeTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.from_name);
            typeTv = itemView.findViewById(R.id.type_name);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            if (position == items.size() - 1 && lastItemListener!= null) lastItemListener.onLastItemOpened(position);
            UserRequest ur = items.get(position);
            authorTv.setText(ur.getUser().getFull_name() + " (Запрос №" + ur.getId() + ")");
            typeTv.setText(ur.getType() != null ? ur.getType().getName() : "");

        }



        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(),view);

            return false;
        }

        @Override
        public void onClick(View view) {
            onLongClick(view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        UserRequestsAdapter.clickListener = clickListener;
    }

    public void setLastItemListener(LastItemListener lastItemListener) {
        this.lastItemListener = lastItemListener;
    }

    public abstract static class ClickListener {
        public abstract void onItemLongClick(int position, View v);
    }
}

package ru.sugai.village.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;
import ru.sugai.village.data.BusJSON;
import ru.sugai.village.data.database.DataBASE;


public class AdapterBus extends RecyclerView.Adapter {
    public static ClickListener clickListener;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataBASE.BUS_JSON_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView time, bus_title,bus_place;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.bus_time);
            bus_title = itemView.findViewById(R.id.tv_event_title);
            bus_place = itemView.findViewById(R.id.bus_place);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindView(int position){
            BusJSON bus = DataBASE.BUS_JSON_LIST.get(position);
            time.setText(bus.getPlace());
            bus_title.setText(bus.getTime());
            bus_place.setText(bus.getTitle());

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);


        }


        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

    }
    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterBus.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    private View noData(View view,ViewGroup viewGroup){


        return view;
    }

}

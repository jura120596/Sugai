package ru.sugai.village.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;
import ru.sugai.village.data.Event;
import ru.sugai.village.data.History;
import ru.sugai.village.data.MapObject;
import ru.sugai.village.data.database.DataBASE;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder> {


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ((MyViewHolder)holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return DataBASE.HISTORY_JSON_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView time, points,mapId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_history_time);
            points = itemView.findViewById(R.id.tv_history_points);
            mapId = itemView.findViewById(R.id.tv_history_mapObjectId);
        }

        public void bindView(int position){
            History item = DataBASE.HISTORY_JSON_LIST.get(position);
            time.setText(item.getCreated_at());
            int p = item.getPoints();
            MapObject map_object = item.getMap_object();
            this.points.setText((map_object != null ? -1*p : "+" + p) +" баллов благодарности");
            if (map_object != null) {
                mapId.setText("Место: "+ map_object.getName());
                this.points.setTextColor(this.points.getContext().getColor(R.color.like));
            } else {
                Event e = item.getEvent();
                mapId.setText("Мероприятие: " + (e!=null ? e.getTitle() : "" + item.getVillage_event_id()));
                this.points.setTextColor(this.points.getContext().getColor(R.color.accept));
            }

        }

        @Override
        public void onClick(View view) {

        }
    }
}

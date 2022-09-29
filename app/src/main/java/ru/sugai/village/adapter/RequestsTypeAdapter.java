package ru.sugai.village.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;
import ru.sugai.village.data.UserRequestType;
import ru.sugai.village.data.database.DataBASE;


public class RequestsTypeAdapter extends RecyclerView.Adapter {

    public static ClickListener clickListener;
    public Context context;

    public RequestsTypeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_type,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return DataBASE.REQUEST_TYPEJSON_LIST.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_request_content);
//            itemView.setOnLongClickListener(this);
        }

        public void bindView(int position){
            UserRequestType appeal = DataBASE.REQUEST_TYPEJSON_LIST.get(position);
            content.setText("Тип: "+appeal.getName());
        }



        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(),view);

            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RequestsTypeAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemLongClick(int position, View v);
    }
}

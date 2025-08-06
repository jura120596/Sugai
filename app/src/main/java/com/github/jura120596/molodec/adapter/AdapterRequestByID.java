package com.github.jura120596.molodec.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.UserRequestType;
import com.github.jura120596.molodec.data.database.DataBASE;


public class AdapterRequestByID extends RecyclerView.Adapter {

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
        return DataBASE.REQUEST_TYPEJSON_byID_LIST.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_request_content);
        }

        public void bindView(int position) {
            UserRequestType appeal = DataBASE.REQUEST_TYPEJSON_byID_LIST.get(position);
            content.setText("ID" + appeal.getId() + "\nОбращение: " + appeal.getName());
        }
    }
}

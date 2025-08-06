package com.github.jura120596.molodec.ui.hisory;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.adapter.AdapterHistory;
import com.github.jura120596.molodec.data.History;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

    private AdapterHistory adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        adapter = new AdapterHistory();
        recyclerView = view.findViewById(R.id.recycler_history);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getHistory();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getHistory();
    }

    public void getHistory(){
        Call<ServerListResponse<History>> getHistory = Retrofit.getInstance().getApi().getEventHistory("Bearer " + DataBASE.token);
        getHistory.enqueue(new Callback<ServerListResponse<History>>() {
            @Override
            public void onResponse(Call<ServerListResponse<History>> call, Response<ServerListResponse<History>> response) {

                if(response.code()==200){
                    try {

                        DataBASE.HISTORY_JSON_LIST.clear();
                        DataBASE.HISTORY_JSON_LIST.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                        Log.d(CONST.SERVER_LOG,response.body().getData().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<History>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
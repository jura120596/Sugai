package com.github.jura120596.molodec.ui.appeal;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.LoginActivity;
import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.adapter.AppealsAdapter;
import com.github.jura120596.molodec.data.Appeal;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.helpers.LastItemListener;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.ui.news.NewsEditFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppealFragment extends Fragment  {


    public static final int MODE_MY = 1;
    public static final String MODE = "appeal";
    AppealsAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton actionButton;
    int mode = 0;
    int page = 0;
    private int last_page = -1;

    public AppealFragment() {
    }
    public AppealFragment(int mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appeals,container,false);
        recyclerView = view.findViewById(R.id.recycler_appeal_byrequest);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppealsAdapter(getContext(), recyclerView);
        recyclerView = view.findViewById(R.id.recycler_appeal_byrequest);
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
        actionButton = view.findViewById(R.id.fab_addNews);
        actionButton.setVisibility(View.INVISIBLE);
        if (mode == MODE_MY) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NewsEditFragment editFragment = new NewsEditFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("mode", MODE);
                    editFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFragment).addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        } else if ((DataBASE.user.getRole() & User.VIILAGE_USER_ROLE) > 1) {
            actionButton.setVisibility(View.INVISIBLE);
        }
        adapter.setClickListener(new AppealsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
            }

            @Override
            public void onItemLongClick(int position, View view) {
            }
        });
        adapter.setLastItemListener(new LastItemListener() {
            @Override
            public void onLastItemOpened(int position) {
                getAppeals(page + 1);
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppeals();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAppeals();
    }

    private void getAppeals() {
        getAppeals(1);
    }
    private void getAppeals(int page) {
        if (last_page > 0 && last_page < page) return;
        Call<ServerListResponse<Appeal>> getNewsList = Retrofit.getInstance().getApi().getAppeals(
                "Bearer " + LoginActivity.userToken(getActivity().getBaseContext()), mode == MODE_MY ? "me" : null, page + "");
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        getNewsList.enqueue(new Callback<ServerListResponse<Appeal>>() {
            @Override
            public void onResponse(Call<ServerListResponse<Appeal>> call, Response<ServerListResponse<Appeal>> response) {
                if (response.isSuccessful() && AppealFragment.this.page == page - 1) {
                    AppealFragment.this.page = page;
                    AppealFragment.this.last_page = response.body().getLast_page();
                    if (page == 1) {
                        DataBASE.APPEALS_LIST.clear();
                        adapter.notifyDataSetChanged();
                    }
                    List<Appeal> data = response.body().getData();
                    DataBASE.APPEALS_LIST.addAll(data);
                    adapter.notifyDataSetChanged();
                    Log.d(CONST.SERVER_LOG, data.toString());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<Appeal>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }
    public void update(){
        adapter.notifyDataSetChanged();
    }
}
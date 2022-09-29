package ru.sugai.village.ui.news;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ru.sugai.village.R;
import ru.sugai.village.adapter.NewsAdapter;
import ru.sugai.village.data.News;
import ru.sugai.village.data.ServerListResponse;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {


    NewsAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton addBtn;
    private int page = 0;
    private int last_page = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        adapter = new NewsAdapter(getContext());
        addBtn = view.findViewById(R.id.fab_addNews);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new NewsEditFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        addBtn.setVisibility(DataBASE.user.isAdmin() ? View.VISIBLE : View.GONE);
        adapter.setClickListener(new NewsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                this.onItemLongClick(position, view);
            }

            @Override
            public void onItemLongClick(int position, View view) {
                if (!DataBASE.user.isCurator() && !DataBASE.user.isAdmin()) return;
                NewsEditFragment editFragment = new NewsEditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                editFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFragment).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        recyclerView = view.findViewById(R.id.recycler_appeal_byrequest);
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });
        adapter.setLastItemListener((p) -> {
            getNews(page + 1);
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getNews();


    }

    private void getNews() {
        getNews(1);
    }

    private void getNews(int page) {
        if (last_page > 0 && last_page < page) return;
        Call<ServerListResponse<News>> getNewsList = Retrofit.getInstance().getApi().getNewsList(page);
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        getNewsList.enqueue(new Callback<ServerListResponse<News>>() {
            @Override
            public void onResponse(Call<ServerListResponse<News>> call, Response<ServerListResponse<News>> response) {
                if (response.isSuccessful()) {
                    NewsFragment.this.page = page;
                    NewsFragment.this.last_page = response.body().getLast_page();
                    if (page == 1) DataBASE.NEWS_JSON_LIST.clear();
                    DataBASE.NEWS_JSON_LIST.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Сервер не доступен", Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<News>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Ошибка запроса", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
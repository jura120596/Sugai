package com.github.jura120596.molodec.ui.User;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.adapter.AdapterUserList;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.ui.components.AppEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersListFragment extends Fragment {

    AdapterUserList adapterUserList ;
    RecyclerView recyclerView;
    AppEditText search;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler;
    int page = 0;
    int last_page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_userList);
        search = view.findViewById(R.id.user_search);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapterUserList = new AdapterUserList((AppCompatActivity) getActivity());
        adapterUserList.setLastItemListener((p)->{
            getUsers(page+1);
        });
        recyclerView.setAdapter(adapterUserList);
        adapterUserList.setOnItemClickListener(new AdapterUserList.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                onItemLongClick(position, v);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                UserEditFragment fragment = new UserEditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos",position);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        getUsers();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });
        TextWatcher w = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (handler != null) handler.removeCallbacksAndMessages(null);
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUsers();
                    }
                }, 2000);
            }
        };
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (search.getRight() - search.getEt().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (handler!=null) handler.removeCallbacksAndMessages(null);
                        getUsers();
                        return true;
                    }
                }
                return false;
            }
        });
        search.addTextChangedListener(w);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsers();
    }

    private void getUsers() {
        getUsers(1);
    }
    private void getUsers(int page){
        if (last_page > 0 && last_page < page) return;
        Call<ServerListResponse<User>> getUsers = Retrofit.getInstance().getApi().getUsers("Bearer "+ DataBASE.token, search.getText());
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        getUsers.enqueue(new Callback<ServerListResponse<User>>() {
            @Override
            public void onResponse(Call<ServerListResponse<User>> call, Response<ServerListResponse<User>> response) {
                System.out.println(response.code());
                if (page == 1) DataBASE.USERS_LIST.clear();
                UsersListFragment.this.page = page;
                UsersListFragment.this.last_page = response.body().getLast_page();
                DataBASE.USERS_LIST.addAll(response.body().getData());
                Log.d(CONST.SERVER_LOG,DataBASE.USERS_LIST.toString());
                adapterUserList.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<User>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
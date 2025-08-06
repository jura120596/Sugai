package com.github.jura120596.molodec.ui.request;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.MainActivity;
import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.adapter.RequestsTypeAdapter;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.UserRequestType;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.helpers.SwipeHelper;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestTypesFragment extends Fragment {


    FloatingActionButton addTypeBtn;
    RequestsTypeAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_appeal, container, false);
        getItems();
        addTypeBtn =view.findViewById(R.id.add_type);
        recyclerView = view.findViewById(R.id.recycler_appeal_byrequest);
        adapter = new RequestsTypeAdapter(getActivity());
        SwipeHelper swh = new SwipeHelper(getActivity(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Удалить",
                        0,
                        Color.parseColor("#FF8585"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                delete(DataBASE.REQUEST_TYPEJSON_LIST.get(pos));
                            }
                        }
                ));
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swh);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItems();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        addTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RequestTypeEditFragment editFragment = new RequestTypeEditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos",-1);
                editFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    public void getItems(){
        Call<ServerListResponse<UserRequestType>> call = Retrofit.getInstance().getApi().getRequestTypes();
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        call.enqueue(new Callback<ServerListResponse<UserRequestType>>() {
            @Override
            public void onResponse(Call<ServerListResponse<UserRequestType>> call, Response<ServerListResponse<UserRequestType>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Ошибка получения списка типов запроса", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataBASE.REQUEST_TYPEJSON_LIST.clear();
                DataBASE.REQUEST_TYPEJSON_LIST.addAll(response.body().getData());
                Log.d(CONST.SERVER_LOG, DataBASE.REQUEST_TYPEJSON_LIST.toString());
                afterLoad();
            }

            @Override
            public void onFailure(Call<ServerListResponse<UserRequestType>> call, Throwable t) {
                t.printStackTrace();
                afterLoad();
            }
        });
    }
    private void delete(UserRequestType typeItem) {
        Call<ResponseBody> deleteResp = Retrofit.getInstance().getApi().deleteRequestType("Bearer " + MainActivity.userToken(getActivity()), typeItem.getId());
        deleteResp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
                    DataBASE.REQUEST_TYPEJSON_LIST.remove(typeItem);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void afterLoad() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
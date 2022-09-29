package ru.sugai.village.ui.bus;

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

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.R;
import ru.sugai.village.adapter.AdapterBus;
import ru.sugai.village.data.BusJSON;
import ru.sugai.village.data.ServerListResponse;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentBus extends Fragment {
    private AdapterBus adapterBus;
    private RecyclerView recyclerView;
    private FloatingActionButton addbutton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bus_admin, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBus();
            }
        });
        addbutton = view.findViewById(R.id.floatingActionButton_addBus);
        recyclerView = view.findViewById(R.id.recycler_bus);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new FragmentBusAdd()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if ((DataBASE.user.getRole() & CONST.CURATOR_ROLE) == 0 && DataBASE.user.getRole() < CONST.ADMIN_ROLE) {
            addbutton.setVisibility(View.INVISIBLE);
            addbutton.setVisibility(View.GONE);
        }

        adapterBus = new AdapterBus();
        adapterBus.setOnItemClickListener(new AdapterBus.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                BusEditFragment editFragment = new BusEditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                editFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onItemLongClick(int position, View v) {
            }
        });
        recyclerView.setAdapter(adapterBus);
        getBus();

    }


    private void getBus() {
        Call<ServerListResponse<BusJSON>> getBus = Retrofit.getInstance().getApi().getBusList();
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        getBus.enqueue(new Callback<ServerListResponse<BusJSON>>() {
            @Override
            public void onResponse(Call<ServerListResponse<BusJSON>> call, Response<ServerListResponse<BusJSON>> response) {
                if (response.code() == 200) {
                    try {
                        DataBASE.BUS_JSON_LIST.clear();
                        DataBASE.BUS_JSON_LIST.addAll(response.body().getData());
                        Log.d(CONST.SERVER_LOG, "" + DataBASE.BUS_JSON_LIST);
                        adapterBus.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<BusJSON>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
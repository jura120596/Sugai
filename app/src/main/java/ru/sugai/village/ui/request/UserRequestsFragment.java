package ru.sugai.village.ui.request;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.R;
import ru.sugai.village.adapter.UserRequestsAdapter;
import ru.sugai.village.data.ServerListResponse;
import ru.sugai.village.data.UserRequest;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.helpers.CurrentToTopHelper;
import ru.sugai.village.helpers.SwipeHelper;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.retrofit.call.ValidateCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequestsFragment extends Fragment {

    FloatingActionButton addBtn;
    UserRequestsAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private int role = CONST.LIBRARIAN_ROLE;
    private int page = 0;
    private int last_page = 0;
    private String bearer;;
    private boolean transformstarted;
    private long ANIMATION_DURATION;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bearer = "Bearer " + DataBASE.token;
        View view = inflater.inflate(R.layout.fragment_user_requests_items, container, false);
        if (getArguments() != null) role = getArguments().getInt("role");
        loadItems();
        addBtn = view.findViewById(R.id.add_request_btn);
        addBtn.setVisibility(DataBASE.user.isUser() ? View.VISIBLE : View.GONE);
        recyclerView = view.findViewById(R.id.recycler_appeal_byrequest);
        adapter = new UserRequestsAdapter(getActivity(), getItemsList());
        adapter.setOnItemClickListener(new UserRequestsAdapter.ClickListener() {
            @Override
            public void onItemLongClick(int position, View v) {
                transformToSingleItem(position);
            }

        });
        SwipeHelper swh = new SwipeHelper(getActivity(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Удалить",
                        0,
                        Color.parseColor("#FF8585"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                deleteItem(getItemsList().get(pos));
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
                loadItems();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, UserRequestEditFragment.newInstance(null, role)).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        adapter.setLastItemListener((p) -> {
            loadItems(page+1);
        });

        return view;
    }

    private void showItem(int adapterPosition) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, UserRequestShowFragment.newInstance(getItemsList().get(0)))
                .addToBackStack(null)
                .commit();
    }

    private void transformToSingleItem(int adapterPosition) {
        transformToSingleItem(adapterPosition, () -> {
            showItem(adapterPosition);
        });
    }

    private void transformToSingleItem(int adapterPosition, Runnable run) {
        if (transformstarted) return;
        transformstarted = true;
        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(0F, 1F);
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        CurrentToTopHelper updater = new CurrentToTopHelper(adapterPosition, width);
        updater.attachToRecycler(recyclerView);
        animator.addUpdateListener(valueAnimator -> {
            updater.update((Float) valueAnimator.getAnimatedValue());
        });
        ANIMATION_DURATION = 300L;
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
        Handler handler = new Handler();
        addBtn.setVisibility(View.INVISIBLE);
        handler.postDelayed(() -> {
            animator.removeAllUpdateListeners();
            removeAndNotifyOtherItems(adapterPosition, run);
            transformstarted = false;
        }, ANIMATION_DURATION);
    }

    private void removeAndNotifyOtherItems(int adapterPosition, Runnable run) {
        int size = getItemsList().size();
        for (int i = 0; i < size; i++) {
            if (adapterPosition != i) {
                getItemsList().remove(i < adapterPosition ? 0 : 1);
                adapter.notifyItemRemoved(i < adapterPosition ? 0 : 1);
            }
        }
        (new Handler()).postDelayed(run, 500);
    }

    private void deleteItem(UserRequest userRequest) {
        Call<ResponseBody> call = Retrofit.getApi().deleteUserRequest(bearer, userRequest.getId() + "");
        call.enqueue(new ValidateCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    getItemsList().remove(userRequest);
                    adapter.notifyDataSetChanged();
                } else onError();
            }

            @Override
            public Context getContext() {
                return UserRequestsFragment.this.getContext();
            }
        });
    }

    public void loadItems() {
        loadItems(1);
    }
    public void loadItems(int page) {
        if (last_page > 0 && last_page < page) return;
        Call<ServerListResponse<UserRequest>> call = Retrofit.getApi().getUserRequests(bearer, role, page);
        if (DataBASE.token.length() < 10) return;
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        call.enqueue(new Callback<ServerListResponse<UserRequest>>() {
            @Override
            public void onResponse(Call<ServerListResponse<UserRequest>> call, Response<ServerListResponse<UserRequest>> response) {
                if (response.isSuccessful()) {
                    UserRequestsFragment.this.page = page;
                    UserRequestsFragment.this.last_page = response.body().getLast_page();
                    if (page == 1) getItemsList().clear();
                    getItemsList().addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    (new Handler()).postDelayed(() -> {
                        CurrentToTopHelper.resetState(recyclerView, getItemsList().size());
                    }, 500);
                    Log.d(CONST.SERVER_LOG, getItemsList().toString());
                } else onError();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerListResponse<UserRequest>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                onError();
            }
        });
    }

    private List<UserRequest> getItemsList() {
        return role == CONST.LIBRARIAN_ROLE ? DataBASE.REQUEST_LIB_LIST : DataBASE.REQUEST_ADMIN_LIST;
    }

    public void onError() {
        Context context = getContext();
        if (context != null) Toast.makeText(context, "Ошибка сервера", Toast.LENGTH_SHORT).show();
    }

}
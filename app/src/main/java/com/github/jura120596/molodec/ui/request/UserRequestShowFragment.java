package com.github.jura120596.molodec.ui.request;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.adapter.MessagesAdapter;
import com.github.jura120596.molodec.data.Message;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.UserRequest;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 *
 */
public class UserRequestShowFragment extends Fragment {

    private static final String ARG_PARAM1 = "pos";
    private UserRequest item = null;
    MessagesAdapter adapter;
    private View itemInfoCard = null;
    RecyclerView chat;
    ArrayList<Message> list = new ArrayList<Message>();
    EditText msgET;

    public UserRequestShowFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item Parameter 1.
     * @return A new instance of fragment UserRequestShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRequestShowFragment newInstance(UserRequest item) {
        UserRequestShowFragment fragment = new UserRequestShowFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (UserRequest) getArguments().getSerializable(ARG_PARAM1);
        }
        loadMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_request_show, container, false);
        chat = view.findViewById(R.id.messages_view);
        adapter = new MessagesAdapter(getContext(), list);
        chat.setAdapter(adapter);
        LinearLayoutManager layoutManager = (LinearLayoutManager) chat.getLayoutManager();
        layoutManager.setReverseLayout(true);
        itemInfoCard = view.findViewById(R.id.user_request_type_info_card);
        msgET = view.findViewById(R.id.msgEt);
        View btn = view.findViewById(R.id.sendMsgBtn);
        btn.setOnClickListener((v) -> sendMessage());
        TextView authorTv = itemInfoCard.findViewById(R.id.from_name);
        TextView typeTv = itemInfoCard.findViewById(R.id.type_name);
        TextView date = itemInfoCard.findViewById(R.id.type_date);
        TextView text = itemInfoCard.findViewById(R.id.type_content);
        TextView address = itemInfoCard.findViewById(R.id.address_name);
        TextView phone = itemInfoCard.findViewById(R.id.phone_tv);
        TextView email = itemInfoCard.findViewById(R.id.email_tv);
        if (itemInfoCard != null) {
            View chevron = itemInfoCard.findViewById(R.id.user_request_chevron);
            if (chevron != null) chevron.setVisibility(View.GONE);
            authorTv.setText(item.getUser().getFull_name() + " (Запрос №" + item.getId() + ")");
            typeTv.setText(item.getType() != null ? item.getType().getName() : "");
            text.setText(item.getText());
            date.setText(item.getDate());
            email.setText(item.getUser().getEmail());
            phone.setText("+7" + item.getUser().getPhone());
            address.setText(item.getUser().getAddress());
            text.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            if (!DataBASE.user.isUser()){
                view.findViewById(R.id.phone_lbl).setVisibility(View.VISIBLE);
                view.findViewById(R.id.email_lbl).setVisibility(View.VISIBLE);
                view.findViewById(R.id.adr_label).setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
            }
            if (item.getUser().getId() == DataBASE.user.getId()) {
                authorTv.setVisibility(View.GONE);
            }
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) chat.getLayoutParams();
        lp.topMargin = view.getHeight();
        return view;
    }

    public void loadMessages() {
        Call<ServerListResponse<Message>> call = Retrofit.getApi().getUserRequestMessages("Bearer " + DataBASE.token, item.getId() + "");
        call.enqueue(new ValidateCallback<ServerListResponse<Message>>() {
            @Override
            public void onSuccess(Call<ServerListResponse<Message>> call, Response<ServerListResponse<Message>> response) {
                list.clear();
                list.addAll(response.body().getData());
                System.out.println(list.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public Context getContext() {
                return UserRequestShowFragment.this.getContext();
            }
        });
    }

    public void sendMessage() {
        Message message = new Message();
        String text = msgET.getText().toString();
        if (text.length() < 1) {
            Toast.makeText(getContext(), "Cообщение не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        message.setText(text);
        Call<ServerListResponse<Message>> call = Retrofit.getApi().sendUserRequestMessage("Bearer " + DataBASE.token, item.getId() + "", message);
        call.enqueue(new ValidateCallback<ServerListResponse<Message>>() {
            public void onSuccess(Call<ServerListResponse<Message>> call, Response<ServerListResponse<Message>> response) {
                msgET.setText("");
                list.clear();
                list.addAll(response.body().getData());
                System.out.println(list.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void on500(Call<ServerListResponse<Message>> call, Response<ServerListResponse<Message>> response) {
                try {
                    String s = (response.errorBody().string());
                    JSONObject jsonObject = new JSONObject(s);
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    e.printStackTrace();
                    super.on500(call, response);
                }
            }

            @Override
            public void on422(Call<ServerListResponse<Message>> call, Response<ServerListResponse<Message>> response, ValidationResponse errors) {
                Toast.makeText(getContext(), errors.getError("text"), Toast.LENGTH_SHORT).show();
            }

            public Context getContext() {
                return UserRequestShowFragment.this.getContext();
            }
        });
    }
}
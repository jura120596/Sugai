package com.github.jura120596.molodec.ui.request;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.ServerItemResponse;
import com.github.jura120596.molodec.data.UserRequest;
import com.github.jura120596.molodec.data.UserRequestType;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;
import com.github.jura120596.molodec.ui.components.AppButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRequestEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRequestEditFragment extends RequestTypesFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ITEM_INDEX_PARAM = "item";
    private static final String ROLE_PARAM = "param2";

    // TODO: Rename and change types of parameters
    private UserRequest item;
    private int roleParam;
    private TextInputLayout requestTypes;
    private AppButton saveBtn;
    private ArrayAdapter arrayAdapter;
    private ArrayList<UserRequestType> items;
    private TextInputLayout requestText;
    private AutoCompleteTextView typeTV;
    private UserRequest message;

    public UserRequestEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserRequestEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRequestEditFragment newInstance(UserRequest param1, int param2) {
        UserRequestEditFragment fragment = new UserRequestEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEM_INDEX_PARAM, param1);
        args.putInt(ROLE_PARAM, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (UserRequest) getArguments().getSerializable(ITEM_INDEX_PARAM);
            roleParam = getArguments().getInt(ROLE_PARAM);
        }
        if (DataBASE.REQUEST_TYPEJSON_LIST.isEmpty()) {
            getItems();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        message = new UserRequest();
        View view = inflater.inflate(R.layout.fragment_user_request_edit, container, false);
        items = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items);
        typeTV = (AutoCompleteTextView) ((TextInputLayout) (view.findViewById(R.id.menu))).getEditText();
        typeTV.setAdapter(arrayAdapter);
        typeTV.setOnItemClickListener((adapterView, view1, i, l) -> {
            int id = DataBASE.REQUEST_TYPEJSON_LIST.get(i).getId();
            message.setType_id(id);
        });
        update();
        requestTypes = (((TextInputLayout) (view.findViewById(R.id.menu))));
//        requestTypes.setError("adapter");
        requestText = (TextInputLayout) (view.findViewById(R.id.user_request_text));
//        requestText.setError("adapter");
        saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this );
        return view;
    }

    private void update() {
        items.clear();
        for (UserRequestType t : DataBASE.REQUEST_TYPEJSON_LIST) {
            items.add(t);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLoad() {
        update();
    }

    @Override
    public void onClick(View view) {
        message.setRole(roleParam);
        message.setText(requestText.getEditText().getText().toString());
        Call<ServerItemResponse<UserRequest>> c = Retrofit.getApi().addUserRequests("Bearer " + DataBASE.token, message);
        c.enqueue(new ValidateCallback<ServerItemResponse<UserRequest>>() {
            @Override
            public void onSuccess(Call<ServerItemResponse<UserRequest>> call, Response<ServerItemResponse<UserRequest>> response) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void on422(Call<ServerItemResponse<UserRequest>> call, Response<ServerItemResponse<UserRequest>> response, ValidationResponse errors) {
                String e = errors.getError("type");
                String e2 = errors.getError("text");
                if (e == null && e2 == null) super.on422(call, response, errors);
                if (e != null) requestTypes.setError(e);
                if (e2 != null) requestText.setError(e2);
            }

            @Override
            public Context getContext() {
                return UserRequestEditFragment.this.getContext();
            }
        });
    }
}
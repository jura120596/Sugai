package ru.sugai.village.ui.request;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.sugai.village.MainActivity;
import ru.sugai.village.R;
import ru.sugai.village.data.UserRequestType;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.retrofit.call.ValidateCallback;
import ru.sugai.village.retrofit.responses.ObjectResponse;
import ru.sugai.village.retrofit.responses.ValidationResponse;
import ru.sugai.village.ui.components.AppEditText;

import retrofit2.Call;
import retrofit2.Response;


public class RequestTypeEditFragment extends Fragment implements View.OnClickListener {

    TextView textView;
    AppEditText et;
    Button button;
    private UserRequestType typeItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_edit, container, false);
        int pos = getArguments().getInt("pos");
        typeItem = pos == -1 ? null : DataBASE.REQUEST_TYPEJSON_LIST.get(pos);
        textView = view.findViewById(R.id.type_tv);
        et = (AppEditText) view.findViewById(R.id.type_et);
        if (typeItem != null) {
            textView.setText("ID: " + typeItem.getId() + "\nТип: " + typeItem.getName());
            et.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            textView.setVisibility(View.INVISIBLE);
            et.setVisibility(View.VISIBLE);
        }
        button = view.findViewById(R.id.bt_fragment_edit);
        button.setOnClickListener(this);
        if (typeItem != null) {
            button.setText(R.string.remove_title);
        } else {
            button.setText(R.string.save_title);
        }

        return view;
    }

    public void onClick(View view) {
        create();
    }

    private void create() {
        Call<ObjectResponse<UserRequestType>> call = Retrofit.getInstance().getApi().addRequestType("Bearer " + MainActivity.userToken(getActivity()), et.getText().toString());
        call.enqueue(new ValidateCallback<ObjectResponse<UserRequestType>>() {
            @Override
            public void on422(Call<ObjectResponse<UserRequestType>> call, Response<ObjectResponse<UserRequestType>> response, ValidationResponse errors) {
                String error = errors.getError("name");
                if (error != null) et.setError(error);
            }

            @Override
            public void onSuccess(Call<ObjectResponse<UserRequestType>> call, Response<ObjectResponse<UserRequestType>> response) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }

            @Override
            public Context getContext() {
                return getActivity();
            }
        });
    }


}
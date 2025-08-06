package com.github.jura120596.molodec.ui.bus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;
import com.github.jura120596.molodec.ui.components.AppEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;


public class FragmentBusAdd extends Fragment {

    private Button button;
    private AppEditText title,place, time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_add, container, false);
        button = view.findViewById(R.id.bt_fragment_busAdd);
        title = view.findViewById(R.id.etv_fragment_busAdd_title);
        place = view.findViewById(R.id.etv_fragment_busAdd_place);
        time = view.findViewById(R.id.etv_fragment_busAdd_time);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<JsonObject> addBus = Retrofit.getInstance().getApi().addBusEvent("Bearer " + DataBASE.token,title.getText().toString(),place.getText().toString(),time.getText().toString());
                addBus.enqueue(new ValidateCallback<JsonObject>() {
                    @Override
                    public void on422(Call<JsonObject> call, Response<JsonObject> response, ValidationResponse errors) {
                        String e = errors.getError("title");
                        if (e != null) title.setError(e);
                        e = errors.getError("place");
                        if (e != null) place.setError(e);
                        e = errors.getError("time");
                        if (e != null) time.setError(e);
                    }

                    @Override
                    public void onSuccess(Call<JsonObject> call, Response<JsonObject> response) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }

                    @Override
                    public Context getContext() {
                        return getActivity();
                    }
                });
            }
        });



    return view;
    }

}
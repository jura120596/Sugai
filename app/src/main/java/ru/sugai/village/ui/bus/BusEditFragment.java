package ru.sugai.village.ui.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.sugai.village.R;
import ru.sugai.village.data.BusJSON;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusEditFragment extends Fragment {
    private TextView title,place,time;
    private Button button;
    private  int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_edit, container, false);
        Bundle bundle = getArguments();
        time = view.findViewById(R.id.etv_fragment_busedit_time);
        title = view.findViewById(R.id.etv_fragment_requestedit);
        place = view.findViewById(R.id.etv_fragment_busedit_place);
        button = view.findViewById(R.id.bt_fragment_busedit);

        BusJSON busJSON = DataBASE.BUS_JSON_LIST.get(bundle.getInt("pos"));
        title.setText("Номер маршрута: "+busJSON.getTitle());
        place.setText("Маршрут: "+busJSON.getPlace());
        time.setText("Время отправления: "+busJSON.getTime());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Call<JsonObject> deleteBusEvent = Retrofit.getInstance().getApi().deleteBusEvent("Bearer " + DataBASE.token,busJSON.getId());
                    deleteBusEvent.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.code()==200){
                                Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        return view;
    }
}
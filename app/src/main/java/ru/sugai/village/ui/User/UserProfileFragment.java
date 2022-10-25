package ru.sugai.village.ui.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.R;
import ru.sugai.village.data.User;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.retrofit.responses.ObjectResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private FloatingActionButton editBt;
    private ImageView qrCode;
    TableLayout tl;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);

        String url = CONST.SERVER_URl + "/storage/" + DataBASE.user.getQr();
        qrCode = view.findViewById(R.id.iv_prof_QrCode);
        Picasso.get().load(url)
                .placeholder(R.drawable.ic_person)
                .into(qrCode);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        qrCode.setMinimumWidth(dm.widthPixels * 3 / 4);
//        qrCode.setMaxWidth(dm.widthPixels * 3 / 4);
//        qrCode.setMinimumHeight(dm.widthPixels * 3 / 4);
//        qrCode.setMaxHeight(dm.widthPixels * 3 / 4);
        tl = view.findViewById(R.id.table);

        editBt = view.findViewById(R.id.floatBt_editProf);

        printUser(DataBASE.user);
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new UserProfileEditFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void printUser(User user) {

        TextView label = (TextView) view.findViewById(R.id.surlbl);
        TextView viewById = (TextView) view.findViewById(R.id.surname);
        viewById.setText(user.getSecond_name());
        TextView viewById1 = (TextView) view.findViewById(R.id.name);
        viewById1.setText(user.getName());
        TextView viewById2 = (TextView) view.findViewById(R.id.lastname);
        viewById2.setText(user.getLast_name());
        TextView viewById3 = (TextView) view.findViewById(R.id.phone);
        viewById3.setText(user.getPhone());
        TextView viewById4 = (TextView) view.findViewById(R.id.email);
        viewById4.setText(user.getEmail());
        TextView viewById5 = (TextView) view.findViewById(R.id.address);
        viewById5.setText((!user.getAddress().isEmpty() ? user.getAddress() : "не указан"));
        TextView viewById6 = (TextView) view.findViewById(R.id.profile_points_tv);
        viewById6.setText(user.getPoints()+"");
        TextView viewById7 = (TextView) view.findViewById(R.id.card_id);
        if (user.getCard_id() != null && !user.getCard_id().isEmpty())
            viewById7.setText(user.getCard_id());
        viewById7.setText(("не выдана"));
        TextView viewById8 = (TextView) view.findViewById(R.id.region);
        viewById8.setText(("не указан"));
        TextView viewById9 = (TextView) view.findViewById(R.id.district1);
        viewById9.setText(("не указан"));
        if (user.getDistrict() != null) {
            viewById9.setText(user.getDistrict().getName());
            viewById8.setText(user.getDistrict().getRegion().getName());
        }
        TextView viewById10 = (TextView) view.findViewById(R.id.district2);
        viewById10.setText(("не выбрано"));
        if (user.getVillage() != null) viewById10.setText(user.getVillage().getName());
        viewById.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById1.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById2.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById3.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById4.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById5.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById6.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById7.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById9.setMaxWidth(tl.getWidth() - label.getWidth());
        viewById10.setMaxWidth(tl.getWidth() - label.getWidth());
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<ObjectResponse<User>> getProfileData = Retrofit.getInstance().getApi().getProfile("Bearer " + DataBASE.token);
        getProfileData.enqueue(new Callback<ObjectResponse<User>>() {
            @Override
            public void onResponse(Call<ObjectResponse<User>> call, Response<ObjectResponse<User>> response) {
                if (response.isSuccessful()) {
                    User reponseUser = response.body().getData();
                    DataBASE.user = reponseUser;
                    Log.d(CONST.SERVER_LOG, "USERS " + DataBASE.user);
                    printUser(reponseUser);
                }
            }

            @Override
            public void onFailure(Call<ObjectResponse<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
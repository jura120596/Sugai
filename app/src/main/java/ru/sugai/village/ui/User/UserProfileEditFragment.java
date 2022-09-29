package ru.sugai.village.ui.User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.sugai.village.R;
import ru.sugai.village.data.User;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.ui.components.AppEditText;
import ru.sugai.village.ui.signup.SignUpViewModelFactory;
import ru.sugai.village.ui.signup.UserFormResult;
import ru.sugai.village.ui.signup.UserFormState;
import ru.sugai.village.ui.signup.UserFormViewModel;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class UserProfileEditFragment extends Fragment implements View.OnClickListener {

    private AppEditText name, second_name, last_name, password, password_confirmation, address;
    private Button saveBtn;
    private UserFormViewModel viewModel;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist_edit, container, false);
        viewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(UserFormViewModel.class);
        viewModel.setContext(getActivity());
        name = view.findViewById(R.id.etv_prof_edit_name);
        second_name = view.findViewById(R.id.etv_prof_edit_second_name);
        last_name = view.findViewById(R.id.etv_prof_edit_last_name);
        password = view.findViewById(R.id.etv_prof_edit_password);
        password_confirmation = view.findViewById(R.id.etv_prof_edit_password_confirmation);
        address = view.findViewById(R.id.etv_prof_edit_adress);
        user = DataBASE.user;
        name.setText(user.getName());
        second_name.setText(user.getSecond_name());
        last_name.setText(user.getLast_name());
        address.setText(user.getAddress());
        saveBtn = view.findViewById(R.id.bt_editProf);
        saveBtn.setOnClickListener(this);
        viewModel.getSignUpFormState().observe(getActivity(), new Observer<UserFormState>() {
            @Override
            public void onChanged(@Nullable UserFormState userForm) {
                if (userForm == null) {
                    return;
                }
                saveBtn.setEnabled(userForm.isDataValid());
                userForm.setError(userForm.getNameError(), name);
                userForm.setError(userForm.getLastNameError(), last_name);
                userForm.setError(userForm.getSecondNameError(), second_name);
                userForm.setError(userForm.getAddressError(), address);
                userForm.setError(userForm.getPasswordError(), password);
                userForm.setError(userForm.getPassword_confirmationError(), password_confirmation);
            }
        });
        viewModel.getUserEditResult().observe(getActivity(), new Observer<UserFormResult>() {
            @Override
            public void onChanged(UserFormResult userFormResult) {
                if (userFormResult == null) {
                    return;
                }
                if (userFormResult.getSuccess() != null) {
                    DataBASE.user = userFormResult.getSuccess();
                    Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(UserProfileEditFragment.this.getId(), new UserProfileFragment()).commit();
                }
            }
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.profileDataChanged(
                        name.getText().toString(),
                        second_name.getText().toString(),
                        last_name.getText().toString(),
                        address.getText().toString(),
                        password.getText().toString(),
                        password_confirmation.getText().toString()
                );
            }
        };
        name.addTextChangedListener(watcher);
        last_name.addTextChangedListener(watcher);
        second_name.addTextChangedListener(watcher);
        address.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        password_confirmation.addTextChangedListener(watcher);
        return view;
    }

    @Override
    public void onClick(View view) {
        User value = viewModel.getUserData().getValue();
        Call<ResponseBody> editProfile = Retrofit.getInstance().getApi().editProfile("Bearer " + DataBASE.token, value);
        viewModel.sendRequest(editProfile);
    }
}
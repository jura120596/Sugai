package com.github.jura120596.molodec.ui.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import com.github.jura120596.molodec.data.District;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.databinding.ActivitySignUpBinding;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.ui.components.AppEditText;

public class SignUpActivity extends AppCompatActivity  implements TextWatcher{

    private UserFormViewModel viewModel;
    private ActivitySignUpBinding binding;
    private AutoCompleteTextView districtMenu;
    ArrayList<District> districts = new ArrayList<>();
    ArrayAdapter<District> districtAdapter;
    private AutoCompleteTextView villageMenu;
    ArrayList<District> villages = new ArrayList<>();
    ArrayAdapter<District> villagesAdapter;
    private AppEditText usernameEditText,nameEt,secondNameEt, lastNameEt, addressEt,phoneEt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadDistricts();
        viewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(UserFormViewModel.class);
        viewModel.setContext(this);
        usernameEditText = (AppEditText) binding.username;
        nameEt = (AppEditText) binding.name;
        secondNameEt = (AppEditText) binding.secondName;
        lastNameEt = (AppEditText) binding.lastName;
        addressEt = (AppEditText) binding.address;
        phoneEt = (AppEditText) binding.phone;
        TextInputLayout menu = (binding.menu);
        TextInputLayout menu2 = (binding.menu2);
        villageMenu = (AutoCompleteTextView) menu2.getEditText();
        districtMenu = (AutoCompleteTextView) menu.getEditText();
        final Button signupBtn = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        viewModel.getSignUpFormState().observe(this, new Observer<UserFormState>() {
            @Override
            public void onChanged(@Nullable UserFormState userForm) {
                if (userForm == null) {
                    return;
                }
                signupBtn.setEnabled(userForm.isDataValid());
                userForm.setError(userForm.getUsernameError(), usernameEditText);
                userForm.setError(userForm.getNameError(), nameEt);
                userForm.setError(userForm.getLastNameError(), lastNameEt);
                userForm.setError(userForm.getSecondNameError(), secondNameEt);
                userForm.setError(userForm.getAddressError(), addressEt);
                userForm.setError(userForm.getPhoneError(), phoneEt);
                userForm.setError(userForm.getDistrictIdError(), menu);
                userForm.setError(userForm.getVillageIdError(), menu2);
                userForm.setError(userForm.getFormError(), binding.textView3);
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getSignupResult().observe(this, new Observer<UserFormResult>() {
            @Override
            public void onChanged(@Nullable UserFormResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                User value = viewModel.getUserData().getValue();
                if (value != null)
                    setResult(Activity.RESULT_OK, new Intent().putExtra("email", value.getEmail()));
                else setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateFormData(viewModel.getUserData().getValue().getDistrict_id(),viewModel.getUserData().getValue().getVillage_id());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        secondNameEt.addTextChangedListener(afterTextChangedListener);
        lastNameEt.addTextChangedListener(afterTextChangedListener);
        addressEt.addTextChangedListener(afterTextChangedListener);
        phoneEt.addTextChangedListener(afterTextChangedListener);
        nameEt.addTextChangedListener(afterTextChangedListener);
        menu.getEditText().addTextChangedListener(this);
        districtMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer did = null;
                if (i < districts.size()) {
                    did = districts.get(i).getId();
                    updateFormData(did, null);
                }
                villageMenu.clearListSelection();
                loadDistricts(null, 2, did);
            }
        });
        villageMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < villages.size()) {
                    updateFormData(viewModel.getUserData().getValue().getDistrict_id(), villages.get(i).getId());
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                viewModel.sendRequest(Retrofit.getInstance().getApi().registration(viewModel.getUserData().getValue()));
            }
        });
        phoneEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                viewModel.sendRequest(Retrofit.getInstance().getApi().registration(viewModel.getUserData().getValue()));
            }
            return false;
        });
    }

    private void updateFormData(Integer did, Integer vid){
        viewModel.loginDataChanged(
                usernameEditText.getText().toString(),
                nameEt.getText().toString(),
                secondNameEt.getText().toString(),
                lastNameEt.getText().toString(),
                addressEt.getText().toString(),
                phoneEt.getText().toString(),
                null, null, null, null, null, did, vid
        );
    }


    private void updateUiWithUser(User model) {
        String welcome = model.getName() + ", проверяйте почту и возвращайтесь!";
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    Handler districtsLoader;
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (districtsLoader!=null) {
            districtsLoader.removeCallbacksAndMessages(null);
        }
        districtsLoader = (new Handler());
        districtsLoader.postDelayed(() -> {
            loadDistricts(charSequence.toString(), 1,null);
        }, 500);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void loadDistricts() {
        loadDistricts(null, 1, null);
    }
    public void loadDistricts(String name, Integer level, Integer pdi) {
        Call<ServerListResponse<District>> call = Retrofit.getInstance().getApi().loadDistricts(name, level, pdi);
        call.enqueue(new ValidateCallback<ServerListResponse<District>>() {
            @Override
            public void onSuccess(Call<ServerListResponse<District>> call, Response<ServerListResponse<District>> response) {
                switch (level) {
                    case 1:
                        districts = (ArrayList<District>) response.body().getData();
                        districtAdapter = new ArrayAdapter<District>(getBaseContext(), android.R.layout.simple_list_item_1, districts);;
                        districtMenu.setAdapter(districtAdapter);
                        break;
                    case 2:
                        villages = (ArrayList<District>) response.body().getData();
                        villagesAdapter = new ArrayAdapter<District>(getBaseContext(), android.R.layout.simple_list_item_1, villages);;
                        villageMenu.setAdapter(villagesAdapter);
                        break;
                }
            }

            @Override
            public Context getContext() {
                return SignUpActivity.this;
            }
        });
    }
}
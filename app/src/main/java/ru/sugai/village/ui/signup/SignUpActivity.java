package ru.sugai.village.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.sugai.village.data.User;
import ru.sugai.village.databinding.ActivitySignUpBinding;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.ui.components.AppEditText;

public class SignUpActivity extends AppCompatActivity {

    private UserFormViewModel viewModel;
    private ActivitySignUpBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(UserFormViewModel.class);
        viewModel.setContext(this);
        final AppEditText usernameEditText = (AppEditText) binding.username;
        final AppEditText nameEt = (AppEditText) binding.name;
        final AppEditText secondNameEt = (AppEditText) binding.secondName;
        final AppEditText lastNameEt = (AppEditText) binding.lastName;
        final AppEditText addressEt = (AppEditText) binding.address;
        final AppEditText phoneEt = (AppEditText) binding.phone;
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
                viewModel.loginDataChanged(
                        usernameEditText.getText().toString(),
                        nameEt.getText().toString(),
                        secondNameEt.getText().toString(),
                        lastNameEt.getText().toString(),
                        addressEt.getText().toString(),
                        phoneEt.getText().toString()
                );
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        secondNameEt.addTextChangedListener(afterTextChangedListener);
        lastNameEt.addTextChangedListener(afterTextChangedListener);
        addressEt.addTextChangedListener(afterTextChangedListener);
        phoneEt.addTextChangedListener(afterTextChangedListener);
        nameEt.addTextChangedListener(afterTextChangedListener);

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

    private void updateUiWithUser(User model) {
        String welcome = model.getName() + ", проверяйте почту и возвращайтесь!";
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}